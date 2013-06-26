package edse.edu.com;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;


public class FileIndexer
{
	static IndexWriter indexWriter = null;
	public FileIndexer()
	{
		
		//This class is to handle any Lucene manipulation
		//and other utilities with indexes, documents/CSV files.
	
		try
		{
			//language specific analyzers?
			//Making the index to store documents/tweets in.1st param specifies directory where index will be created.
			//third param means create a new index if not created
			indexWriter = new IndexWriter("C:/Users/User/workspace", new StandardAnalyzer(), true );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	public static void TweetsToIndex(List<User> users)
	{
		//Now do a for each loop for each tweet object and
		//add it as a document to the index.
		//TYPE HERE EITHER USERS OR TWEET, HAVE TO POSSIBLY RECODE THIS SECTION SOME
		
		for(User tweet: users)
		{
			//List<Tweet> me = tweet.getTweets();
			//for(Tweet t : me)
			//{
				
			//}
			Document tweetDoc = new Document();
			//ALL TIMES ARE IN GMT!
			/*
			tweetDoc.add(new LongField("tweet_id", tweet.getTweetID(), Field.Store.YES));
			tweetDoc.add(new Field("time", tweet.getTimeID().toString(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new DoubleField("lat", tweet.getLat(), Field.Store.YES));
			tweetDoc.add(new DoubleField("long", tweet.getTweetLongit(), Field.Store.YES));
			tweetDoc.add(new DoubleField("goog_x", tweet.getGoogleX(), Field.Store.YES));
			tweetDoc.add(new DoubleField("goog_y", tweet.getGoogleX(), Field.Store.YES));
			tweetDoc.add(new IntField("sender_id", tweet.getSenderID(), Field.Store.YES));
			tweetDoc.add(new Field("sender_name", tweet.getSenderName(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("source", tweet.getSenderSource(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new IntField("reply_to_user_id", tweet.getRepToUserID(), Field.Store.YES));
			tweetDoc.add(new LongField("reply_to_tweet_id", tweet.getRepToTweetID(), Field.Store.YES));
			tweetDoc.add(new Field("place_id", tweet.getPlaceID(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("tweet_text", tweet.getTweetText(), Field.Store.YES, Field.Index.TOKENIZED));
			
			try
			{
				indexWriter.addDocument(tweetDoc);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			*/
			
			
		}
	}
}
