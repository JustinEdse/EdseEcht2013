package edse.edu.com;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	/**
	 * @param args
	 */
	public static void main(String[] args)
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
		try
		{
			//If doing anlaysis on the boston marathon and aftermath, the file could be from April 15th or over the course
			//of a couple days close to that.
			tweets = Loader.loadTweetsFromFile("./src/all.csv");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<User> users = User.tweetsToUsers(tweets);
		//List<User> movedUsers = new ArrayList<User>();
		
		//for each loop to check and filter
		//users who have tweeted a certain number of times.
		System.out.println(users.size());
		
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
				    new ParseDate("yyyy-MM-dd HH:mm:ss"), //time stamp from tweet
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
		double centerLat = 42.3497630; //latitude and longitude close to Boston marathon
		double centerLon = -71.0785170;
		double radius = 300;
		
		try
		{
			beanReader = new CsvBeanReader(new FileReader(path), CsvPreference.STANDARD_PREFERENCE);
			
			//header elements at the top of the csv file. Those need to be accounted for.
			final String[] header = {null, "timeID", "tweetLatit", "tweetLongit", "googxMeas", "googyMeas", "senderID", 
					null, null, "repToUserID", null, null, null};
			final CellProcessor[] processors = getProcessors();
			
			Tweet tweet = beanReader.read(Tweet.class, header, processors);
			while(tweet != null)
			{
				if(tweet.getLat() < 400 && User.greatCircle(centerLat, centerLon, tweet.getLat(), tweet.getTweetLongit()) < radius)
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
		{
			Map.Entry pairs = (Map.Entry)iterator.next();
			if(((ArrayList<Tweet>) pairs.getValue()).size() < 2)
			{
				iterator.remove();
			}
		}
		
		return tweetsByUserName;
	}


}