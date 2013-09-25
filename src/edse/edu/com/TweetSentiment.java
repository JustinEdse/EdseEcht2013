package edse.edu.com;


import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.gson.Gson;




public class TweetSentiment
{
	private final static String VIRALHEAT_KEY = "Te49jExsy72pQyub1xG"; // 5000 Calls

	static int malePositive = 0;
	static int maleNegative = 0;
	static int femalePositive = 0;
	static int femaleNegative = 0;
	static int unknownM = 0;
	static int unknownF = 0;
	
	public TweetSentiment()
	{
		
	}
	
	public static void DetermineSentiment(Collection<edse.edu.com.User> mUsers, Collection<edse.edu.com.User> fUsers)
	{
		
		
		for(User male : mUsers)
		{
			List<Tweet> tweets = male.getTweets();
			
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
		
	System.out.println("FINAL INTEGER VALUES\n");
	System.out.println(malePositive + "\n");
	System.out.println(maleNegative + "\n");
	System.out.println(femalePositive + "\n");
	System.out.println(femaleNegative + "\n");
	System.out.println(unknownM + "\n");
	System.out.println(unknownF + "\n");
		
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
			            
			                String finalString = togetherString.toString();
							finalString = finalString.substring(0, Math.min(finalString.length(), 300));
			             
			             String url = "https://www.viralheat.com/api/sentiment/review.json?api_key="
			                     + VIRALHEAT_KEY + "&text=" + finalString;
			             String jsonTxt = NetUtil.httpGet(url);
			             // String jsonTxt =
			             // "{\"prob\":0.806548944920931,\"mood\":\"positive\",\"text\":\"happy\"}";
			             //		System.out.println("jsonTxt:  " + jsonTxt);
			             @SuppressWarnings("rawtypes")
						Map jsonJavaRootObject = new Gson().fromJson(jsonTxt, Map.class);
			             
			            // UserResponse textInfo = new Gson().fromJson(jsonTxt, UserResponse.class);
			             //Object me = new Gson().fromJson(jsonTxt, Object.class);
			             
			            // System.out.println(textInfo.toString());
			             //System.out.println(textInfo.getProb());
			             //System.out.println(me.toString());
			             System.out.println(jsonJavaRootObject);
			             String mood = null;
			             //double prob = (double) jsonJavaRootObject.get("prob");
			             if(jsonJavaRootObject != null)
			             {
			             mood = (String) jsonJavaRootObject.get("mood");
			             }
			             else
			             {
			            	 mood = "unknown";
			             }
			             //System.out.println(prob);
			             
			             return mood;
	 }
}
