/*
 * Author: Justin Edse
 * Title: TweetSentiment.java
 * Purpose: Here we want to take each user from the male and female array lists, give their texts to the ViralHeat via HTTP and
 * then get back the result as to whether their tweet text was positive or negative.
 * Date: September 2013
 */

package edse.edu.com;


import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.gson.Gson;




public class TweetSentiment
{
	// The viral heat API key, 5000 calls a day are allowed.
	private final static String VIRALHEAT_KEY = "Te49jExsy72pQyub1xG"; 

	//static variables for keeping track of positive and negative tweets. In addition a small number of tweets returned null from 
	//viral heat so I incremented those into the two unknownM and unknownF variables. This way the program will keep running,
	//provide meaningful feedback, and not crash.
	
	static int malePositive = 0;
	static int maleNegative = 0;
	static int femalePositive = 0;
	static int femaleNegative = 0;
	static int unknownM = 0;
	static int unknownF = 0;
	
	/**
	 * The empty default constructor of the class.
	 */
	public TweetSentiment()
	{
		
	}
	
	/**
	 * This method just gets back the positive or negative results for the M/F tweet text and increments
	 * the correct integer value. 
	 * @param mUsers the list collection of male users
	 * @param fUsers the list collection of female users
	 */
	public static void DetermineSentiment(Collection<edse.edu.com.User> mUsers, Collection<edse.edu.com.User> fUsers)
	{
		
		
		for(User male : mUsers)
		{
			List<Tweet> tweets = male.getTweets();
			
			// going through the list of male users and calling the function to deal with the viral heat API.
			for(Tweet t : tweets)
			{
				try
				{
					if((t.getTweet_text().toString() != null)) 
					{
					String result = textSentiment(t.getTweet_text().toString());
					
					if(result.equals("positive"))
					{
						malePositive += 1;
					}
					else if(result.equals("negative"))
					{ 
						maleNegative += 1;
					}
					else if(result.equals("unknown"))
					{
						unknownM += 1;
					}
					}
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		// going through the female user list and calling the function to get the tweet sentiment for each text
		for(User female : fUsers)
		{
			List<Tweet> tweets = female.getTweets();
			
			for(Tweet ftweet : tweets)
			{
				try
				{
					if((ftweet.getTweet_text().toString() != null)) 
					{
					String result = textSentiment(ftweet.getTweet_text().toString());
					
					if(result.equals("positive"))
					{
						femalePositive += 1;
					}
					else if(result.equals("negative"))
					{ 
						femaleNegative += 1;
					} else if(result.equals("unknown"))
					{
						unknownF += 1;
					}
					}
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		}
	
	// These are the final print out values for the positive, negative, and unknown tweets
	System.out.println("=========================FINAL INTEGER VALUES==============================\n");
	System.out.println("===============Number of positive male tweets: " + malePositive + "===============\n");
	System.out.println("===============Number of negative male tweets: " + maleNegative + "===============\n");
	System.out.println("===============Number of positive female tweets: " + femalePositive + "===============\n");
	System.out.println("===============Number of negative female tweets: " + femaleNegative + "===============\n");

		
	}
	
	public static String textSentiment(String tweet) throws IOException
	{
		 
		
		//http://www.viralheat.com/api/sentiment/review.json?api_key=API_KEY&text=i%20dont%20like
		StringBuffer togetherString = new StringBuffer();
		String concat = null;
			             String splitString[] = tweet.split(" ");
			             
			             for(String sub : splitString)
			             {
			            	 concat = sub + "%20";
			            	 
			            	 togetherString.append(concat);
			             }
			            
			             // string passed to viral heat must be URI encoded and have the string being
			             // the test must have a limit of roughly 300 characters.
			                String finalString = togetherString.toString();
							finalString = finalString.substring(0, Math.min(finalString.length(), 300));
			             
			             String url = "https://www.viralheat.com/api/sentiment/review.json?api_key="
			                     + VIRALHEAT_KEY + "&text=" + finalString;
			             String jsonTxt = NetUtil.httpGet(url);
			        
			             @SuppressWarnings("rawtypes")
						Map jsonJavaRootObject = new Gson().fromJson(jsonTxt, Map.class);
			             
			         
			             
			         
			             //System.out.println(jsonJavaRootObject);
			          
			             String mood = null;
			        
			             if(jsonJavaRootObject != null)
			             {
			             mood = (String) jsonJavaRootObject.get("mood");
			             }
			             else
			             {
			            	 mood = "unknown";
			             }
			           
			             System.out.println("============Tweet Computed was " + mood + " =====================");
			             return mood;
	 }
}
