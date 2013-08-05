package edse.edu.com;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class Tweet
{
	
    String tweet_id;
    Date time; //sent according to GMT!
	double lat;
	double lon;
	double goog_y;//Google coordinates for mapping
	double goog_x;
	String sender_id;
	String sender_name;
	String source;//What device was the tweet sent from?
	String reply_to_user_id;
	String reply_to_tweet_id;
	String place_id; //Contains more info about where the tweet came from.
	String tweet_text;
	
	

	public String toString()
	{
		return String.format("<Tweet ID: " + tweet_id + " time ID: " + time.toString() + " Latitude: " +
				lat + "Longitude: " + lon + " Google X Measure: " + goog_x +
				" Google Y Meas: " + goog_y + " Sender ID: " + sender_id + " Sender Name " + sender_name +
				" Sender Source Device " + source + " Reply to user id " + reply_to_user_id +
				" Reply to tweet ID: " + reply_to_tweet_id + " Place ID: " + place_id + " Tweet Text: " + tweet_text);
		
	}
	
	   public String getTweet_id()
	    {
			return tweet_id;
		}
	   
	   public void setTweet_id(String tweet_id)
	   {
		   this.tweet_id = tweet_id;
	   }
	   
	   public Date getTime()
	    {
			return time;
		}
	   
	   public void setTime(Date time)
	   {
		   this.time = time;
	   }
	   
	   public double getLat()
	    {
			return lat;
		}
	   
	   public void setLat(double lat)
	   {
		   this.lat= lat;
	   }
	   
	   public double getLon()
	    {
			return lon;
		}
	   
	   public void setLon(double lon)
	   {
		   this.lon = lon;
	   }
	   
	   public double getGoog_x()
	    {
			return goog_x;
		}
	   
	   public void setGoog_x(double goog_x)
	   {
		   this.goog_x = goog_x;
	   }
	   
	   public double getGoog_y()
	    {
			return goog_y;
		}
	   
	   public void setGoog_y(double goog_y)
	   {
		   this.goog_y = goog_y;
	   }
	   
	   public String getSender_id()
	    {
			return sender_id;
		}
	   
	   public void setSender_id(String sender_id)
	   {
		    this.sender_id = sender_id;
	   }
	   
	   public String getSender_name()
	    {
			return sender_name;
		}
	   
	   public void setSender_name(String sender_name)
	   {
		   this.sender_name = sender_name;
	   }
	   
	   public String getSource()
	    {
			return source;
		}
	   
	   public void setSource(String source)
	   {
		   this.source = source;
	   }
	   
	   public String getReply_to_user_id()
	    {
			return reply_to_user_id;
		}
	   
	   public void setReply_to_user_id(String reply_to_user_id)
	   {
		   this.reply_to_user_id = reply_to_user_id;
	   }
	   
	   public String getReply_to_tweet_id()
	    {
			return reply_to_tweet_id;
		}
	   
	   public void setReply_to_tweet_id(String reply_to_tweet_id)
	   {
		   this.reply_to_tweet_id = reply_to_tweet_id;
	   }
	   
	   public String getPlace_id()
	    {
			return place_id;
		}
	   
	   public void setPlace_id(String place_id)
	   {
		   this.place_id = place_id;
	   }
	   
	   public String getTweet_text()
	   {
		   return tweet_text;
	   }
	   
	   public void setTweet_text(String tweet_text)
	   {
		   this.tweet_text = tweet_text;
	   }
}

		