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
		tweets = Loader.loadTweetsFromFile("C://Users//edse4_000//Desktop//safe latest//shared_with_Justin_copy//Games_tweet.txt");

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

		
		

		// for each loop to check and filter
		// users who have tweeted a certain number of times.
		System.out.println(users.size());
		System.out.println(movedUsers.size());
		int check = 0;
		for(User u : users)
		{
			System.out.println(u.getUserName());
			System.out.println(check);
			check++;
			
			
		}
		
		
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

		final CellProcessor[] processors = new CellProcessor[] 
				{ 
				new ParseInt(), // string sender_id
				new NotNull(), // string screen_name
				new NotNull()// tweet_text

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


		try
		{
			beanReader = new CsvBeanReader(new FileReader(path),
					CsvPreference.STANDARD_PREFERENCE);

			// header elements at the top of the csv file. Those need to be
			// accounted for.
			final String[] header = {"sender_id", "screen_name",
					"tweet_text"};
			final CellProcessor[] processors = getProcessors();

			
			Tweet tweet = beanReader.read(Tweet.class, header, processors);
			while (tweet != null)
			{
				tweets.add(tweet);
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
			if (!map.containsKey(tweet.screen_name))
			{
				map.put(tweet.screen_name, new ArrayList<Tweet>());
			}

			List<Tweet> userTweets = map.get(tweet.screen_name);
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
				//iterator.remove();
			}
		}

		return tweetsByUserName;
	}

	
	

	

}
