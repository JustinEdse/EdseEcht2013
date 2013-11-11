/*
 * Author(s): Haochi Chen, modified by Justin Edse
 * Title: Tweet.java
 * Purpose: This class provides a basis for a tweet and the information that it contains.
 * This is very handy when reading in the twitter data from the CSV file.
 * Dates: September 2012, July 2013
 */
package edse.edu.com;


public class Tweet
{

	
	int sender_id; // the user's sender ID
	String screen_name; // the sending user's screen name
	String tweet_text; // the user's twitter text

	/**
	 * Provided toString method for overriding purposes
	 */
	public String toString()
	{
		return String.format(" Sender ID: " + sender_id + " Sender Name " + screen_name
				+ "Tweet Text: " + tweet_text);

	}

	
	/**
	 * The getter for the sender's ID
	 * 
	 * @return sender_id of type String
	 */
	public int getSender_id()
	{
		return sender_id;
	}

	/**
	 * The setter of the tweet sender's ID
	 * 
	 * @param sender_id
	 *            of type String
	 */
	public void setSender_id(int sender_id)
	{
		this.sender_id = sender_id;
	}

	/**
	 * The getter of the sending user's screen name
	 * 
	 * @return screen_name of type String
	 */
	public String getScreen_name()
	{
		return screen_name;
	}

	/**
	 * The setter of the sending user's screen name
	 * 
	 * @param screen_name
	 *            of type String
	 */
	public void setScreen_name(String screen_name)
	{
		this.screen_name = screen_name;
	}

	

	/**
	 * The getter for the text in the sending user's tweet.
	 * 
	 * @return tweet_text of type String
	 */
	public String getTweet_text()
	{
		return tweet_text;
	}

	/**
	 * The setter for the text in the sending user's tweet.
	 * 
	 * @return tweet_text of type String
	 */
	public void setTweet_text(String tweet_text)
	{
		this.tweet_text = tweet_text;
	}
}
