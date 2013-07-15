package edse.edu.com;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.math.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;


public class Loader
{
	static double centerLat = 0.0; //latitude and longitude close to Boston marathon bomb, only on static instance of locations.
	static double centerLon = 0.0;
	static double radius = 0.0; 
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		//Loading a certain number of tweets from one day or several days
		//related to a natural disaster, the Boston bombings, or something
		//else. From here we need to perform textual analysis on the tweets 
		//within a given area. If we choose to go the route of the Boston 
		//bombings perhaps we can try and uncover a couple of different things.
		//1. I know that several rumors were reported over twitter and even picked
		//up by real news outlets (an explosion at the JFK library in Boston, 
		//That there were 5 bombs planted that day, jumping to conclusions that
		//the terrorists were foreign/not living in the U.S or were middle eastern,
		//etc. Is there a way that misinformation vs actual fact could be measured?
		//Is there a way to tell how much influence certain users or how many people
		//a rumor spreads to vs when the news outlets pick up the story.
		//Could this be put into an analysis to see percentages or graphs of facts
		//and non facts that came out of the attacks. Just kicking different ideas
		//around.
		List<Tweet> tweets = null;
		
		
		    
		    
			//If doing anlaysis on the boston marathon and aftermath, the file could be from April 15th or over the course
			//of a couple days close to that.//load one day for now.//filter .csv file to 18:49 GMT.
			tweets = Loader.loadTweetsFromFile("./src/all.csv");
			List<User> users = User.tweetsToUsers(tweets);
			List<User> movedUsers = new ArrayList<User>();
			
			for(User user: users)
			{
				if(user.hasMove())
				{
					movedUsers.add(user);
				}
			}
			
		//Calling distance and direction method to operate on each users tweets and distance from bomb location x.
		Loader.DirectMoved(movedUsers);
		
		
	
		//for each loop to check and filter
		//users who have tweeted a certain number of times.
		System.out.println(users.size());
		System.out.println(movedUsers.size());
		
		//gson on hacoci's starts here!
		/*
		 try {
			File file = new File("./src/movedUsers.json");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(gson.toJson(movedUsers));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		 */
		Loader.ConductQuery();
		
		
	}
	private static CellProcessor[] getProcessors()
	{
		//Using an external JAR  called supercsv to easily work
		//with the csv file.//not sure which fields are completely
		//mandatory yet. All of them? To help manipulate SuperCSV I
		//used this link: http://supercsv.sourceforge.net/examples_reading.html
		final CellProcessor[] processors = new CellProcessor[]
				{
				    null, //big int tweet id
				    new ParseDate("yyyy-MM-dd HH:mm:ss"), //time stamp from tweet is in
				    //GMT and contains no time zone.
				    new ParseDouble(),//tweet latitude
				    new ParseDouble(),//tweet longitude
				    new ParseDouble(),//google x coordinate
				    new ParseDouble(),//google y coordinate
				    new ParseInt(), //senderID
				    null, //string senderName
				    null, //string sendSource what device
				    new ParseInt(), //reply to user id
				    null, //reply to tweet id
				    null,//string place id
				    null,//tweet text
				
				};
		
			return processors;
	}
	
	public static List<Tweet> loadTweetsFromFile(String path) throws Exception
	{
		ICsvBeanReader beanReader = null;
		
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		//latitude and longitude settings for the given site
		centerLat = 42.3497630; //latitude and longitude close to Boston marathon
		centerLon = -71.0785170;
		radius = 25; //double check unit!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		try
		{
			beanReader = new CsvBeanReader(new FileReader(path), CsvPreference.STANDARD_PREFERENCE);
			
			//header elements at the top of the csv file. Those need to be accounted for.
			final String[] header = {null, "timeID", "tweetLatit", "tweetLongit", "googxMeas", "googyMeas", "senderID", 
					null, null, "repToUserID", null, null, null};
			final CellProcessor[] processors = getProcessors();
			
			// In the .csv file the time is organized like this, 2013-04-15 hh:mm:ss in GMT.
			// Let's filter from 18:45 to 22:15 GMT
			// This is from 2:45PM to 6:15PM EST on April 15th.
			// The variables below are constructed with the java.sql Timestamp class and
			// each tweet that comes along is check to see if it is within the acceptable range
			// of being after 2:45 but before 6:15. 
			
			Timestamp startTime = Timestamp.valueOf("2013-04-15 18:45:00");
			Timestamp endTime = Timestamp.valueOf("2013-04-15 22:30:00");
			
			Tweet tweet = beanReader.read(Tweet.class, header, processors);
			while(tweet != null)
			{
				if(tweet.getLat() < 400 && User.greatCircle(centerLat, centerLon, tweet.getLat(), tweet.getTweetLongit()) < radius && (tweet.getTimeID().after(startTime) && tweet.getTimeID().before(endTime)))
				{
					tweets.add(tweet);
				}
				try
				{
					tweet = beanReader.read(Tweet.class, header, processors);
				}
				catch(SuperCsvConstraintViolationException e)
				{
					System.out.println("Skipped");
				}
		}
	}
		finally
		{
			if(beanReader != null)
			{
				beanReader.close();
			}
		}
			return tweets;
	}
	
	public static HashMap<String, List<Tweet>> totalByUserName(List<Tweet> tweets)
	{
		HashMap<String, List<Tweet>> map = new HashMap<String, List<Tweet>>();
		
		for(Tweet tweet: tweets)
		{
			if(!map.containsKey(tweet.senderName))
			{
				map.put(tweet.senderName, new ArrayList<Tweet>());
			}
			
			List<Tweet> userTweets = map.get(tweet.senderName);
			userTweets.add(tweet);
		}
		
		return map;
	}
	
	//STILL WANTING TO REMOVE CERTAIN TWEETS IF NOT MOVED......
	public static Map<String, List<Tweet>> filterTweet(Map<String, List<Tweet>> tweetsByUserName)
	{
		Iterator iterator = tweetsByUserName.entrySet().iterator();
		
		while(iterator.hasNext())
		{//don't know if we still want tweets that users have tweeted less than two anymore. probably not.
			Map.Entry pairs = (Map.Entry)iterator.next();
			if(((ArrayList<Tweet>) pairs.getValue()).size() < 2)
			{
				iterator.remove();
			}
		}
		
		return tweetsByUserName;
	}
	
	public static double distMiles(double lastLat, double nextLat, double lastLon, double nextLon)
	{
		double moveLatit = nextLat - lastLat;
		double moveLon = nextLon - lastLon;
		//http://andrew.hedges.name/experiments/haversine/
		double a =  Math.pow((Math.sin(moveLatit/2)), 2) + Math.cos(lastLat) * Math.cos(nextLat) * Math.pow((Math.sin(moveLon/2)),2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distMiles = 3961 * c; //3961 miles for radius of the Earth.
	    
		return distMiles;
	}
	public static void DirectMoved(List<User> users)
	{
		//as usual User type means: username, <list> user tweets.
		// for each twitter user in the array list
		// for each tweet after their second one, how far are they from bomb location x? output to console.
		List<Tweet> tweetsPerUser = new ArrayList<Tweet>();
	
		int i = 0;
		
		//Map<Double, Double> userGroupedLatLon = new HashMap();
		Map<String, List<Integer>> userMovement = new HashMap();
		for(User username : users)
		{
			System.out.println("User = " + username.toString() + "\n");
			
			tweetsPerUser = username.getTweets();
			Tweet lastTweet = tweetsPerUser.get(i);
			Tweet nextTweet = tweetsPerUser.get(i+1);
			double distance = 0;
			double otherDistance = 0;
			
			
			// get initial tweet distance from the epicenter first.
			// After this look at their next tweet and check if it adds to the miles
			// they were away from the epicenter initially. If it is then that means
			// the person is moving away, if it is less then they are moving towards
			// the epicenter. This is a repeating action and each movement should be
			// taken care of in this loop, checking each user's list of tweets and 
			// outputting them into the console window.
			
			//twitter user's initial distance in miles away from the epicenter.
			if(i == 0)
			{
				//This initial measurement of closeness to the epicenter is needed because
				// if you just had 2 tweets and one distance figure you couldn't tell if that
				// existing distance figure was being added to or subtracted from (ie. going towards a point).
				
			    lastTweet.setTweetLatit(-71.0785170);
			    lastTweet.setTweetLongit(42.3497630);
				
			    otherDistance = Loader.distMiles(lastTweet.getLat(), nextTweet.getLat(), lastTweet.getTweetLongit(), nextTweet.getTweetLongit());
			}
				
				otherDistance = distance; // assign distance to otherDistance so we can compare later.
				
				distance += Loader.distMiles(lastTweet.getLat(), nextTweet.getLat(), lastTweet.getTweetLongit(), nextTweet.getTweetLongit());
			    
			
			    
				//double moveLongit = nextTweet.getTweetLongit() - lastTweet.getTweetLongit();
				//double moveLatit = nextTweet.getLat() - lastTweet.getLat();
				//lastTweet.set
				
				// Also thinking about assigning 1's to all users moving away and 0's to the ones moving inward.
				if(i != 0 && otherDistance != 0)
				{
					if(distance > otherDistance)
					{
						//this means we moved positive miles
						System.out.println("With this tweet the user moved" + (distance - otherDistance) + " positive miles from before");
					}
					else if(distance < otherDistance)
					{
						System.out.println("With this tweet the user moved" + (distance - otherDistance) + " negative miles from before");
					}
				}
			
			  i++;
			
			
		}
	}
	
	
	public static void ConductQuery() throws ParseException
	{
		
		// Putting together a string to search through the multiple documents.
		
	
		
		try
		{	//18:49  GMT, 2:49PM April 15th.
			String queryString = "(marathon and boston) AND prayer";
			String otherQuery = "tweet_text: (+boston + finish line";
			String escapeQuery = "tweet_text: that\\s";
			BasicSearch newSearch = new BasicSearch();
			
			Hits returnedHits = newSearch.performSearch(queryString);
			
			@SuppressWarnings("unchecked")
			Iterator<Hit> iteratorHits = returnedHits.iterator();
			
			System.out.println("The following number of results were found:\n " + 
					returnedHits.length());
			
			// while loop to iterate through the hits.
			
			while(iteratorHits.hasNext())
			{
				Hit hit = iteratorHits.next();
				
				Document returnedDoc = hit.getDocument();
				System.out.println(returnedDoc.get("time") + " " + 
						returnedDoc.get("tweet_text") +
						" (" + hit.getScore() + ")");
				
				// From here the score gives the user a good indication
				// of the quality of each document that was associated
				// with each hit.
				
			}
			
			
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	


}
