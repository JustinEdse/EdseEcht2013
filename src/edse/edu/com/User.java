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
	
	public User(String username, List<Tweet> tweets)
	{
		this.username = username;
		this.tweets = tweets;
		
	}
	
	public String getUserName()
	{
		return this.username;
	}
	
	public List<Tweet> getTweets()
	{
		return this.tweets;
	}
	/*
	public boolean hasMove()
	{
		Tweet tweet = this.tweets.get(0);
		boolean moved = false;

		for(Tweet secondTweet: this.tweets.subList(1, this.tweets.size())){
			double dist = greatCircle(tweet.tweetLatit, tweet.tweetLongit, secondTweet.tweetLatit, secondTweet.tweetLongit);

			if(dist > 1){
				moved = true;
				break;	
			}	
			//tweet = secondTweet;
		}
		return moved;
	}
*/
	public static double greatCircle(double lat1, double lng1, double lat2, double lng2)
	{
		// aka checking distance between two points
		double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;
	    return dist;
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
		
		return users;
	}
}