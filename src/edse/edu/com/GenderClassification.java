package edse.edu.com;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import twitter4j.User;

public class GenderClassification
{
	GenderClassification()
	{
		
	}
	
	public static double CheckGender(Map<Integer,twitter4j.User> userMap, List<edse.edu.com.User>movedUsers)
	{
		StringBuffer tweetText = null;
		
		
		for(edse.edu.com.User mdUser : movedUsers)
		{
			
		    for(User user  : userMap.values())
		    {
		    	if(mdUser.getUserName().equals(user.getScreenName()))
		    	{
		    		//get all text from each mdUser.
		    		for(Tweet tweetString : mdUser.getTweets())
		    		{
		    			tweetText.append(tweetString.getTweet_text());
		    		}
		    	}
		    }
		}
		
		//now have all tweet text per user in a String Buffer.
		
		return 0;
	}
}
