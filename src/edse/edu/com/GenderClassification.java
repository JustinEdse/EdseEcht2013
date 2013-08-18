package edse.edu.com;

public class GenderClassification
{
	GenderClassification()
	{
		
	}
	
	public int CheckGender(User twitterUser)
	{
		StringBuffer tweetText = null;
		
		for(Tweet tweet : twitterUser.getTweets())
		{
			tweetText.append(tweet.tweet_text);
		}
		
		//now have all tweet text per user in a String Buffer.
		
		return 0;
	}
}
