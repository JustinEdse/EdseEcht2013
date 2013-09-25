/*
 * Author(s): Haochi Chen, modified by Justin Edse
 * Title: Tweet.java
 * Purpose: This class provides a basis for a tweet and the information that it contains.
 * This is very handy when reading in the twitter data from the CSV file.
 * Dates: September 2012, July 2013
 */
package edse.edu.com;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class Tweet
{

	String tweet_id; // a user's tweet id
	Date time; // a time listed in GMT format
	double lat; // the user's latitude measure
	double lon; // the user's longitude measure
	double goog_y; // user's google y coordinate
	double goog_x; // user's google x coordinate
	String sender_id; // the user's sender ID
	String sender_name; // the sending user's screen name
	String source; // what type of device was the tweet sent from
	String reply_to_user_id; // ID who the sending user is replying to
	String reply_to_tweet_id; // tweet ID the user is replying to
	String place_id; // contains more info about where the tweet came from (ie.
						// from a city, neighborhood, urban area)
	String tweet_text; // the user's twitter text

	/**
	 * Provided toString method for overriding purposes
	 */
	public String toString()
	{
		return String.format("<Tweet ID: " + tweet_id + " time ID: "
				+ time.toString() + " Latitude: " + lat + "Longitude: " + lon
				+ " Google X Measure: " + goog_x + " Google Y Meas: " + goog_y
				+ " Sender ID: " + sender_id + " Sender Name " + sender_name
				+ " Sender Source Device " + source + " Reply to user id "
				+ reply_to_user_id + " Reply to tweet ID: " + reply_to_tweet_id
				+ " Place ID: " + place_id + " Tweet Text: " + tweet_text);

	}

	/**
	 * The getter for the specific tweet ID
	 * 
	 * @return tweet id of type String
	 */
	public String getTweet_id()
	{
		return tweet_id;
	}

	/**
	 * The setter for the tweet ID
	 * 
	 * @param tweet_id
	 *            of type String
	 */
	public void setTweet_id(String tweet_id)
	{
		this.tweet_id = tweet_id;
	}

	/**
	 * The getter for the time of tweet
	 * 
	 * @return time of the tweet in type Date
	 */
	public Date getTime()
	{
		return time;
	}

	/**
	 * The setter for the time the tweet happened
	 * 
	 * @param time
	 *            of type Date
	 */
	public void setTime(Date time)
	{
		this.time = time;
	}

	/**
	 * The getter for the user's latitude
	 * 
	 * @return latitude of type double
	 */
	public double getLat()
	{
		return lat;
	}

	/**
	 * The setter for the user's latitude at the tweet time
	 * 
	 * @param lat
	 *            of type double
	 */
	public void setLat(double lat)
	{
		this.lat = lat;
	}

	/**
	 * The getter for the user's longitude at the time of the tweet.
	 * 
	 * @return lon of type double
	 */
	public double getLon()
	{
		return lon;
	}

	/**
	 * The setter for the user's longitude at the time of the tweet.
	 * 
	 * @param lon
	 *            of type double
	 */
	public void setLon(double lon)
	{
		this.lon = lon;
	}

	/**
	 * The getter for the tweet google x coordinate
	 * 
	 * @return goog_x of type double
	 */
	public double getGoog_x()
	{
		return goog_x;
	}

	/**
	 * The setter for the tweet google x coordinate
	 * 
	 * @param goog_x
	 *            of type double
	 */
	public void setGoog_x(double goog_x)
	{
		this.goog_x = goog_x;
	}

	/**
	 * The getter for the tweet google y coordinate
	 * 
	 * @return goog_y of type double
	 */
	public double getGoog_y()
	{
		return goog_y;
	}

	/**
	 * The setter for the tweet google y coordinate
	 * 
	 * @param goog_y
	 *            of type double
	 */
	public void setGoog_y(double goog_y)
	{
		this.goog_y = goog_y;
	}

	/**
	 * The getter for the sender's ID
	 * 
	 * @return sender_id of type String
	 */
	public String getSender_id()
	{
		return sender_id;
	}

	/**
	 * The setter of the tweet sender's ID
	 * 
	 * @param sender_id
	 *            of type String
	 */
	public void setSender_id(String sender_id)
	{
		this.sender_id = sender_id;
	}

	/**
	 * The getter of the sending user's screen name
	 * 
	 * @return sender_name of type String
	 */
	public String getSender_name()
	{
		return sender_name;
	}

	/**
	 * The setter of the sending user's screen name
	 * 
	 * @param sender_name
	 *            of type String
	 */
	public void setSender_name(String sender_name)
	{
		this.sender_name = sender_name;
	}

	/**
	 * The getter for the source the tweet was send from (ie. Android, IOS).
	 * 
	 * @return source of type String
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * The setter of the source the tweet was sent from.
	 * 
	 * @param source
	 *            of type String
	 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/**
	 * The getter for the ID the user was replying to in their tweet
	 * 
	 * @return reply_to_user_id of type String
	 */
	public String getReply_to_user_id()
	{
		return reply_to_user_id;
	}

	/**
	 * The setter for the ID the user was replying to in their tweet
	 * 
	 * @param reply_to_user_id
	 *            of type String
	 */
	public void setReply_to_user_id(String reply_to_user_id)
	{
		this.reply_to_user_id = reply_to_user_id;
	}

	/**
	 * The getter for tweet ID the user was replying to
	 * 
	 * @return reply_to_tweet_id of type String
	 */
	public String getReply_to_tweet_id()
	{
		return reply_to_tweet_id;
	}

	/**
	 * The getter for tweet ID the user was replying to
	 * 
	 * @param reply_to_tweet_id
	 *            of type String
	 */
	public void setReply_to_tweet_id(String reply_to_tweet_id)
	{
		this.reply_to_tweet_id = reply_to_tweet_id;
	}

	/**
	 * The getter for where the user is sending their tweet from (ie. from a
	 * city, neighborhood)
	 * 
	 * @return place_id of type String
	 */
	public String getPlace_id()
	{
		return place_id;
	}

	/**
	 * The setter for where the user is sending their tweet from
	 * 
	 * @param place_id
	 *            of type String
	 */
	public void setPlace_id(String place_id)
	{
		this.place_id = place_id;
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
