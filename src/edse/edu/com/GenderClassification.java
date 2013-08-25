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
		StringBuffer tweetText = new StringBuffer();
		
		//This is kind of a wacky way of doing this but, since the list of twitter4j didn't have text returned
		//as well, I needed to grab the each users tweets from the movedUsers List that I already had.
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
		    		//NOW CALL CALL THE CLASSIFIER AND DATASET TO DETERMINE WHETHER THE TEXT
		    		//WAS WRITTEN BY A MALE OR FEMALE.
		    	}
		    }
		}
		
		//now have all tweet text per user in a String Buffer.
		
		return 0;
	}
}
