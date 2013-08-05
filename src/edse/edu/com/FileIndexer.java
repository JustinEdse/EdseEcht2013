package edse.edu.com;


import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;


public class FileIndexer
{
	static IndexWriter indexWriter = null;
	static Document tweetDoc = null;

	
	public FileIndexer()
	{
	
		//This class is to handle any Lucene manipulation
		//and other utilities with indexes, documents/CSV files.
	
		try
		{
			
			//exploring multiple analyzers?
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
		//for each user in the list
		for(User tweet: users)
		{
			//for each user get their list of tweets and add a new document based on each
			//individuals tweets. This way things are effectively still grouped by username.
			for(Tweet t : tweet.getTweets())
			{
				//MAKE EACH DOCUMENT AN AGGREGATION AN INDIVIDUAL USERS TWEETS?
			
			tweetDoc = new Document();
			//ALL TIMES ARE IN GMT!
			
			tweetDoc.add(new Field("tweet_id", t.getTweet_id(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("time", t.getTime().toString(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new DoubleField("lat", t.getLat(), Field.Store.YES));
			tweetDoc.add(new DoubleField("long", t.getLon(), Field.Store.YES));
			tweetDoc.add(new DoubleField("goog_x", t.getGoog_x(), Field.Store.YES));
			tweetDoc.add(new DoubleField("goog_y", t.getGoog_y(), Field.Store.YES));
			tweetDoc.add(new Field("sender_id", t.getSender_id(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("sender_name", t.getSender_name(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("source", t.getSource(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("reply_to_user_id", t.getReply_to_user_id(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("reply_to_tweet_id", t.getReply_to_tweet_id(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("place_id", t.getPlace_id(), Field.Store.YES, Field.Index.TOKENIZED));
			tweetDoc.add(new Field("tweet_text", t.getTweet_text(), Field.Store.YES, Field.Index.TOKENIZED));
			}
			try
			{
				indexWriter.addDocument(tweetDoc);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
	}
		
}
