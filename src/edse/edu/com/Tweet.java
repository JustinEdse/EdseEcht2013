package edse.edu.com;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Tweet
{
	
    long tweetID;
   Timestamp timeID; //sent according to GMT!
	double tweetLatit;
	double tweetLongit;
	double googyMeas;//Google coordinates for mapping
	double googxMeas;
	int senderID;
	String senderName;
	String sendSource;//What device was the tweet sent from?
	int repToUserID;
	long repToTweetID;
	String placeID; //Contains more info about where the tweet came from.
	String tweetText;
	
	

	public String toString()
	{
		return String.format("<Tweet ID: " + tweetID + " time ID: " + timeID.toString() + " Latitude: " +
				tweetLatit + "Longitude: " + tweetLongit + " Google X Measure: " + googxMeas +
				" Google Y Meas: " + googxMeas + " Sender ID: " + senderID + " Sender Name " + senderName +
				" Sender Source Device " + sendSource + " Reply to user id " + repToUserID +
				" Reply to tweet ID: " + repToTweetID + " Place ID: " + placeID + " Tweet Text: " + tweetText);
		
	}
	
	   public Long getTweetID()
	    {
			return tweetID;
		}
	   
	   public void setTweetID(Long tweetID)
	   {
		   this.tweetID = tweetID;
	   }
	   
	   public Timestamp getTimeID()
	    {
			return timeID;
		}
	   
	   public void setTimeID(Timestamp timeID)
	   {
		   this.timeID = timeID;
	   }
	   
	   public double getLat()
	    {
			return tweetLatit;
		}
	   
	   public void setTweetLatit(double tweetLatit)
	   {
		   this.tweetLatit = tweetLatit;
	   }
	   
	   public double getTweetLongit()
	    {
			return tweetLongit;
		}
	   
	   public void setTweetLongit(double tweetLongit)
	   {
		   this.tweetLongit = tweetLongit;
	   }
	   
	   public double getGoogleX()
	    {
			return googxMeas;
		}
	   
	   public void setGoogXMeas(double googxMeas)
	   {
		   this.googxMeas = googxMeas;
	   }
	   
	   public double getGoogleYMeas()
	    {
			return googyMeas;
		}
	   
	   public void setGoogleYMeas(double googyMeas)
	   {
		   this.googyMeas = googyMeas;
	   }
	   
	   public int getSenderID()
	    {
			return senderID;
		}
	   
	   public void setSenderID(int senderID)
	   {
		    this.senderID = senderID;
	   }
	   
	   public String getSenderName()
	    {
			return senderName;
		}
	   
	   public void setSenderName(String senderName)
	   {
		   this.senderName = senderName;
	   }
	   
	   public String getSenderSource()
	    {
			return sendSource;
		}
	   
	   public void setSenderSource(String senderSource)
	   {
		   this.sendSource = senderSource;
	   }
	   
	   public int getRepToUserID()
	    {
			return repToUserID;
		}
	   
	   public void setRepToUserID(int repToUserID)
	   {
		   this.repToUserID = repToUserID;
	   }
	   
	   public long getRepToTweetID()
	    {
			return repToTweetID;
		}
	   
	   public void setRepToTweetID(long repToTweetID)
	   {
		   this.repToTweetID = repToTweetID;
	   }
	   
	   public String getPlaceID()
	    {
			return placeID;
		}
	   
	   public void setPlaceID(String placeID)
	   {
		   this.placeID = placeID;
	   }
	   
	   public String getTweetText()
	   {
		   return tweetText;
	   }
	   
	   public void setTweetText(String tweetText)
	   {
		   this.tweetText = tweetText;
	   }
}

		