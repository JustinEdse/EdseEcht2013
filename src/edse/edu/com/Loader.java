/*
 * Author(s): Haochi Chen, Modified by Justin Edse
 * Title: Loader.java
 * Purpose: This class loads in Twitter and user information from a CSV file containing a large amount of data. It makes use
 * of the Super CSV library external jar and a CellProcessor class to parse each line of the file. After this there are several
 * methods in the class that clean up the input and group the information in helpful ways (ie. A list of users and their tweets or
 * a map of users). Lastly the class also output whether a twitter user was moving away or towards an epicenter, in this case
 * the Boston bombing event that took place on April 15th, 2013. This method is still there but the project has shifted more towards
 * figuring out whether a twitter user is male or female and then analyzing their text for emotion.
 * Dates: September 2012, July 2013
 */
package edse.edu.com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.math.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class Loader
{
	/**
	 * This is the initial variable to measure latitude close to central Boston.
	 */
	static double centerLat = 0.0;

	/**
	 * This is the initial variable for longitude for the center of Boston.
	 */
	static double centerLon = 0.0;

	/**
	 * This is the initial radius measure for a circle of X miles around an
	 * area.
	 */
	static double radius = 0.0;

	/**
	 * @param args
	 *            Standard String arguments for the main method
	 * @throws Exception
	 *             Exception argument for errors.
	 */
	public static void main(String[] args) throws Exception
	{
		/**
		 * Initial list of Tweet type to hold a users tweets.
		 */
		List<Tweet> tweets = null;

		/**
		 * Assigning tweets to return value from the method loadTweetsFromFile,
		 * which brings in the csv file containing information such as user
		 * name, their location, device, tweet text, and more.
		 */
		tweets = Loader.loadTweetsFromFile("C://csvfileedit.csv");

		/**
		 * Assigning a list of users to the return value from the method
		 * tweetsToUsers in the User class. This method returns a list of users
		 * and their tweets.
		 */
		List<User> users = User.tweetsToUsers(tweets);
		List<User> movedUsers = new ArrayList<User>();

		// Adding users to an arraylist.
		for (User user : users)
		{

			movedUsers.add(user);

		}

		// Calling distance and direction method to operate on each users tweets
		// and distance from bomb location x.
		Loader.DirectMoved(movedUsers);

		// for each loop to check and filter
		// users who have tweeted a certain number of times.
		System.out.println(users.size());
		System.out.println(movedUsers.size());
		NameValidation.check_name(movedUsers);

		// MOVED USERS IS WHAT WE WANT TO CHECK REAL NAMES FOR.
		System.exit(0);

	}

	/**
	 * This method sets up each of the cell processors in collaboration with
	 * Super CSV to parse in the data fields from the CSV file.
	 * 
	 * @return
	 */
	private static CellProcessor[] getProcessors()
	{
		// Using an external JAR called supercsv to easily work
		// To help manipulate SuperCSV I used this link:
		// http://supercsv.sourceforge.net/examples_reading.html

		final CellProcessor[] processors = new CellProcessor[] { null,
				new ParseDate("yyyy-MM-dd HH:mm:ss"),
				// GMT and contains no time zone.
				new ParseDouble(),// tweet latitude
				new ParseDouble(),// tweet longitude
				new ParseDouble(),// google x coordinate
				new ParseDouble(),// google y coordinate
				null, // senderID
				new NotNull(), // string senderName
				null, // string sendSource what device
				null, // reply to user id
				null, // reply to tweet id
				null,// string place id
				new NotNull(),// tweet text

		};

		return processors;
	}

	/**
	 * This method loads tweets from a file via a String path.
	 * 
	 * @param path
	 *            The String path of where the CSV is located
	 * @return The return value here is a list of tweets
	 * @throws Exception
	 *             This exception is here to take care of any I/O leaks
	 */
	public static List<Tweet> loadTweetsFromFile(String path) throws Exception
	{
		ICsvBeanReader beanReader = null;

		List<Tweet> tweets = new ArrayList<Tweet>();

		// latitude and longitude settings for the given site close to the
		// Boston marathon
		centerLat = 42.3497630;
		centerLon = -71.0785170;
		radius = 25;

		try
		{
			beanReader = new CsvBeanReader(new FileReader(path),
					CsvPreference.STANDARD_PREFERENCE);

			// header elements at the top of the csv file. Those need to be
			// accounted for.
			final String[] header = { null, "time", "lat", "lon", "goog_x",
					"goog_y", null, "sender_name", null, null, null, null,
					"tweet_text" };
			final CellProcessor[] processors = getProcessors();

			// In the .csv file the time is organized like this, 2013-04-15
			// hh:mm:ss in GMT.
			// Let's filter from 18:45 to 23:15 GMT
			// This is from 2:00PM to 7:00PM EST on April 15th.
			// The variables below are constructed with the java.sql Timestamp
			// class and each tweet that comes along is check to see if it is
			// within the
			// acceptable range of being after 2:00PM but before 7:00PM.

			/**
			 * Using SimpleDateFormat to set up a date to parse the file with.
			 */
			Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse("2013-04-15 18:00:00");
			Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse("2013-04-15 23:59:00");

			// Below the greatCircle method from the User class is being used.
			// Doing this allows the tweets selected to be from a circular sized
			// area using
			// latitude and longitude as a measure.
			Tweet tweet = beanReader.read(Tweet.class, header, processors);
			while (tweet != null)
			{
				if (tweet.getLat() < 400
						&& User.greatCircle(centerLat, centerLon,
								tweet.getLat(), tweet.getLon()) < radius
						&& (tweet.getTime().after(startTime) && tweet.getTime()
								.before(endTime)))
				{
					tweets.add(tweet);
				}
				try
				{
					tweet = beanReader.read(Tweet.class, header, processors);
				} catch (SuperCsvConstraintViolationException e)
				{
					System.out.println("Skipped");
					e.printStackTrace();
				}
			}
		} finally
		{
			// closing the bean reader no matter what
			if (beanReader != null)
			{
				beanReader.close();
			}
		}
		return tweets;
	}

	/**
	 * This method takes tweets and totals them by username.
	 * 
	 * @param tweets
	 *            the List of tweets variable passed in as a parameter
	 * @return The return value here is a HashMap type of <username, their
	 *         tweets>
	 */
	public static HashMap<String, List<Tweet>> totalByUserName(
			List<Tweet> tweets)
	{
		HashMap<String, List<Tweet>> map = new HashMap<String, List<Tweet>>();

		for (Tweet tweet : tweets)
		{
			if (!map.containsKey(tweet.sender_name))
			{
				map.put(tweet.sender_name, new ArrayList<Tweet>());
			}

			List<Tweet> userTweets = map.get(tweet.sender_name);
			userTweets.add(tweet);
		}

		return map;
	}

	/**
	 * This method helps filter the tweets per username and iterates through the
	 * map to achieve this type of behavior.
	 * 
	 * @param tweetsByUserName
	 * @return The return value in this method is a map
	 */
	public static Map<String, List<Tweet>> filterTweet(
			Map<String, List<Tweet>> tweetsByUserName)
	{
		Iterator iterator = tweetsByUserName.entrySet().iterator();

		while (iterator.hasNext())
		{// don't know if we still want tweets that users have tweeted less than
			// two anymore. probably not.
			Map.Entry pairs = (Map.Entry) iterator.next();
			if (((ArrayList<Tweet>) pairs.getValue()).size() < 2)
			{
				iterator.remove();
			}
		}

		return tweetsByUserName;
	}

	/**
	 * This method calculates whether a user was moving away or towards the
	 * epicenter by comparing their previous and next moves in a certain
	 * direction.
	 * 
	 * @param lastLat
	 *            The user's last latitude move
	 * @param nextLat
	 *            The user's next move in a latitude
	 * @param lastLon
	 *            The user's last longitude move
	 * @param nextLon
	 *            The user's lat
	 * @return This returns how far the user moved in miles
	 */
	public static double distMiles(double lastLat, double nextLat,
			double lastLon, double nextLon)
	{
		double moveLatit = nextLat - lastLat;
		double moveLon = nextLon - lastLon;
		// http://andrew.hedges.name/experiments/haversine/
		double a = Math.pow((Math.sin(moveLatit / 2)), 2) + Math.cos(lastLat)
				* Math.cos(nextLat) * Math.pow((Math.sin(moveLon / 2)), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distMiles = 3961 * c; // 3961 miles for radius of the Earth.

		return distMiles;
	}

	/**
	 * This method determines whether the user is moving away or towards the
	 * epicenter of the Boston area. Each user's previous and next distance is
	 * compared and measured to see whether their movement was greater or less
	 * than last time. Then looking at the overall difference between the last
	 * move and current move, a moving direction can determined.
	 * 
	 * @param users
	 *            This is a parameter that represents the list of users who
	 *            moved a certain distance.
	 * @throws IOException
	 *             A general IO exception to catch any error that may occur.
	 */
	public static void DirectMoved(List<User> users) throws IOException
	{
		// as usual User type means: username, <list> user tweets.
		// for each twitter user in the array list
		// for each tweet after their second one, how far are they from bomb
		// location x, output to the console window.
		List<Tweet> tweetsPerUser = new ArrayList<Tweet>();
		List<Double> keepTrackOfDistanceMeasure = new ArrayList<Double>();
		List<Tweet> holdLastTweet = new LinkedList<Tweet>();
		Queue<Tweet> distances = new LinkedList<Tweet>();
		double overallDistance = 0;

		for (User username : users)
		{

			tweetsPerUser = username.getTweets();

			for (Tweet tweet : tweetsPerUser)
			{
				// distances queue
				distances.add(tweet);
			}
			// the set
			Tweet initialEpicenter = new Tweet();
			initialEpicenter.setLat(42.3497630);
			initialEpicenter.setLon(-71.0785170);
			holdLastTweet.add(initialEpicenter);

			while (distances.size() > 0)
			{
				Tweet tweetToCompare = distances.remove();

				// distances is the queue.

				Tweet tweetFromSet = holdLastTweet.remove(0);
				holdLastTweet.add(tweetToCompare);
				// Now call the haversine function to get the distance and store
				// it in the keepTrackOfDistances.

				double distanceReturned = distMiles(tweetFromSet.getLat(),
						tweetToCompare.getLat(), tweetFromSet.getLon(),
						tweetToCompare.getLon());

				// index 0 previous distance to compare.
				// index 1 should be next distance.
				keepTrackOfDistanceMeasure.add(distanceReturned);

				if (keepTrackOfDistanceMeasure.size() == 2)
				{
					// this means we have a first location and a second
					// location. We can now see whether the second location
					// is greater or less than the first point.
					overallDistance = distanceReturned;
					distanceReturned += keepTrackOfDistanceMeasure.get(1);

					if (distanceReturned > overallDistance)
					{

						System.out.println("+");
					} else
					{

						System.out.println("-");
					}

				}

			}

			// clear distance measures for next user.

			distances.clear();
			holdLastTweet.clear();
			overallDistance = 0;
			keepTrackOfDistanceMeasure.clear();

		}

	}

}
