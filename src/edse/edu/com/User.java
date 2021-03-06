/*
 * Author(s): Haochi Chen, modified by Justin Edse
 * Title: User.java
 * Purpose: This class provides a basis for a tweet and the information that it contains.
 * This is very handy when reading in the twitter data from the CSV file.
 * Dates: September 2012, July 2013
 */

package edse.edu.com;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class User
{
	private String username;
	private List<Tweet> tweets;
	private int movement = 0;
	private String description;
	private String realName;
	
	public User(String username, List<Tweet> tweets)
	{
		this.username = username;
		this.tweets = tweets;
		this.movement = 0;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return this.description;
	}
	
	public void setRealName(String realName)
	{
		this.realName = realName;
	}
	public String getRealName()
	{
		return this.realName;
	}
	
	
	
	public String getUserName()
	{
		return this.username;
	}
	
	public List<Tweet> getTweets()
	{
		return this.tweets;
	}

	
	public static List<User> tweetsToUsers(List<Tweet> tweets)
	{
		Map<String, List<Tweet>> tweetsByUserName = Loader.totalByUserName(tweets);
		Map<String, List<Tweet>> filteredOut = Loader.filterTweet(tweetsByUserName);
		
		
		List<User> users = new ArrayList<User>();
		
		Iterator<Entry<String, List<Tweet>>> iterator = filteredOut.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Map.Entry<String, List<Tweet>> pairs = iterator.next();
			String userName = pairs.getKey();
			List<Tweet> userTweets = pairs.getValue();
			User user = new User(userName, userTweets);
			users.add(user);
			
			
		}
		/////////////////////////////////////////////////////////////////////////
		//GIVE THIS LIST OF TWEETS TO THE FILEINDEX CLASS TO CREATE DOCS AND PUT INTO LUCENE INDEX.
		//SLIGHT REDESIGN MAY NEED TO BE DONE IN FILEINDEXER....
		//FileIndexer.TweetsToIndex(users);
		
		return users;
	}
}
