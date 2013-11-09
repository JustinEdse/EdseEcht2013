/*
 * Author: Justin Edse
 * Title: TweetSentiment.java
 * Purpose: Here we want to take each user from the male and female array lists, give their texts to the ViralHeat via HTTP and
 * then get back the result as to whether their tweet text was positive or negative.
 * Date: September 2013
 */

package edse.edu.com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.CharMatcher;
import com.google.gson.Gson;

public class TweetSentiment
{
	// The viral heat API key, 5000 calls a day are allowed.
	private final static String VIRALHEAT_KEY = "Te49jExsy72pQyub1xG";

	// static variables for keeping track of positive and negative tweets. In
	// addition a small number of tweets returned null from
	// viral heat so I incremented those into the two unknownM and unknownF
	// variables. This way the program will keep running,
	// provide meaningful feedback, and not crash.

	static int malePositive = 0;
	static int maleNegative = 0;
	static int femalePositive = 0;
	static int femaleNegative = 0;
	static int unknownM = 0;
	static int unknownF = 0;
	static Map<String, Integer> wordFreqMale = new HashMap<>();
	static Map<String, Integer> wordFreqFemale = new HashMap<>();
	static Map<String, Integer> totalFreq = new HashMap<>();
	static int countPrinted = 0;

	/**
	 * The empty default constructor of the class.
	 */
	public TweetSentiment()
	{

	}

	/**
	 * This method just gets back the positive or negative results for the M/F
	 * tweet text and increments the correct integer value.
	 * 
	 * @param mUsers
	 *            the list collection of male users
	 * @param fUsers
	 *            the list collection of female users
	 */
	public static void DetermineSentiment(Collection<edse.edu.com.User> mUsers,
			Collection<edse.edu.com.User> fUsers)
	{

		for (User male : mUsers)
		{
			List<Tweet> tweets = male.getTweets();

			// going through the list of male users and calling the function to
			// deal with the viral heat API.
			for (Tweet t : tweets)
			{
				try
				{
					if ((t.getTweet_text().toString() != null))
					{
						String result = textSentiment(t.getTweet_text()
								.toString());

						if (result.equals("positive"))
						{
							malePositive += 1;
						} else if (result.equals("negative"))
						{
							maleNegative += 1;
						} else if (result.equals("unknown"))
						{
							unknownM += 1;
						}
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// going through the female user list and calling the function to get
		// the tweet sentiment for each text
		for (User female : fUsers)
		{
			List<Tweet> tweets = female.getTweets();

			for (Tweet ftweet : tweets)
			{
				try
				{
					if ((ftweet.getTweet_text().toString() != null))
					{
						String result = textSentiment(ftweet.getTweet_text()
								.toString());

						if (result.equals("positive"))
						{
							femalePositive += 1;
						} else if (result.equals("negative"))
						{
							femaleNegative += 1;
						} else if (result.equals("unknown"))
						{
							unknownF += 1;
						}
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// These are the final print out values for the positive, negative, and
		// unknown tweets
		System.out
				.println("=========================FINAL INTEGER VALUES==============================\n");
		System.out.println("===============Number of positive male tweets: "
				+ malePositive + "===============\n");
		System.out.println("===============Number of negative male tweets: "
				+ maleNegative + "===============\n");
		System.out.println("===============Number of positive female tweets: "
				+ femalePositive + "===============\n");
		System.out.println("===============Number of negative female tweets: "
				+ femaleNegative + "===============\n");

	}

	public static void CommonWordFrequency(Collection<edse.edu.com.User> users,
			boolean readyPrint, String which) throws IOException
	{
		// get frequency of words used by females.
		// layout is equal to K = word, V = occurrence
		
		BufferedWriter writer = null;
		boolean finished = false;
		

		if (readyPrint == false)
		{

			for (edse.edu.com.User u : users)
			{
				for (Tweet tweet : u.getTweets())
				{

					String twText = tweet.getTweet_text();
					
					//String filteredTweet = twText.replaceAll("\\s[0-9]+\\s", " ");
					String filterOutUserName = twText.replaceAll("@[A-Za-z0-9_]+\\s{1}", "");
			
					String[] wordArray = filterOutUserName.split(" ");
					for (String word : wordArray)
					{
						if (which.equals("MALE"))
						{
							if (wordFreqMale.containsKey(word))
							{
								wordFreqMale.put(word,
										wordFreqMale.get(word) + 1);
							} else
							{
								wordFreqMale.put(word, 1);
							}
						} else if (which.equals("FEMALE"))
						{
							if (wordFreqFemale.containsKey(word))
							{
								wordFreqFemale.put(word,
										wordFreqFemale.get(word) + 1);
							} else
							{
								wordFreqFemale.put(word, 1);
							}
						}

						if (totalFreq.containsKey(word))
						{
							totalFreq.put(word, totalFreq.get(word) + 1);
						} else
						{
							totalFreq.put(word, 1);
						}
					}
				}
			}
		}

		if (readyPrint == true)
		{
			countPrinted++;
			// Here I am just trying to shorten the code up and not have to
			// worry about
			// whether a male or female map is being dealt with. Just assign the
			// variable
			// appropriately below.
			Map<String, Integer> tempMap = null;
			try
			{
				if (which.equals("FEMALE"))
				{
					writer = new BufferedWriter(new FileWriter(
							"C://outfemale.txt"));
					tempMap = wordFreqFemale;
				} else if (which.equals("MALE"))
				{
					writer = new BufferedWriter(new FileWriter(
							"C://outMale.txt"));
					tempMap = wordFreqMale;

				}
				
				for (Map.Entry<String, Integer> outerEntry : totalFreq
						.entrySet())
				{
					for (Map.Entry<String, Integer> innerEntry : tempMap
							.entrySet())
					{

						if (outerEntry.getKey().equals(innerEntry.getKey()))
						{
							writer.write(outerEntry.getKey() + "\t"
									+ innerEntry.getValue() + "\t"
									+ outerEntry.getValue());
							writer.newLine();
						}
					}
				}

				writer.flush();
				writer.close();
				
				if(countPrinted ==2 ) {finished = true;}
				
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(finished == true && countPrinted == 2)
			{
				TweetSentiment.MaleFemaleComboData(wordFreqMale, wordFreqFemale, totalFreq);
			}
		}
		
		
	}

	
	
	
	public static void MaleFemaleComboData(Map<String,Integer> mMap, Map<String,Integer> fMap, Map<String,Integer> totalF) throws IOException
	{
		BufferedWriter out = null;
		try
		{
			out = new BufferedWriter(new FileWriter("C://combinedGender.txt"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int fValue = 0;
		int mValue = 0;
		for(Map.Entry<String, Integer> total :totalF.entrySet())
		{
		
			
			//fValue = mMap.get(total.getKey());
			//mValue = fMap.get(total.getKey());
			
			if(!fMap.containsKey(total.getKey()))
			{
				fValue = 0;
			}
			else
			{
				fValue = fMap.get(total.getKey());
			}
			
			if(!mMap.containsKey(total.getKey()))
			{
				mValue = 0;
			}
			else
			{
				mValue = mMap.get(total.getKey());
			}
			
			
			out.write(total.getKey() + "\t" + mValue + "\t" + fValue);
			out.newLine();
			
		}
		
	   out.flush();
	   out.close();
	}
	public static String textSentiment(String tweet) throws IOException
	{

		// http://www.viralheat.com/api/sentiment/review.json?api_key=API_KEY&text=i%20dont%20like
		StringBuffer togetherString = new StringBuffer();
		String concat = null;
		String splitString[] = tweet.split(" ");

		for (String sub : splitString)
		{
			concat = sub + "%20";

			togetherString.append(concat);
		}

		// string passed to viral heat must be URI encoded and have the string
		// being
		// the test must have a limit of roughly 300 characters.
		String finalString = togetherString.toString();
		finalString = finalString.substring(0,
				Math.min(finalString.length(), 300));

		String url = "https://www.viralheat.com/api/sentiment/review.json?api_key="
				+ VIRALHEAT_KEY + "&text=" + finalString;
		String jsonTxt = NetUtil.httpGet(url);

		@SuppressWarnings("rawtypes")
		Map jsonJavaRootObject = new Gson().fromJson(jsonTxt, Map.class);

		// System.out.println(jsonJavaRootObject);

		String mood = null;

		if (jsonJavaRootObject != null)
		{
			mood = (String) jsonJavaRootObject.get("mood");
		} else
		{
			mood = "unknown";
		}

		System.out.println("============Tweet Computed was " + mood
				+ " =====================");
		return mood;
	}
}
