/*
 * Author: Justin Edse
 * Title: GenderClassification.java
 * Purpose: To effectively separate the twitter users handled in the NameValidation.java class
 * into male and female groups. This is done by first putting male and female matches into array lists and then
 * calling the other classifier classes to predict each unknown user's sex.
 * Date: August, September 2013
 */

package edse.edu.com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import twitter4j.User;

public class GenderClassification
{

	static List<twitter4j.User> maleList = new ArrayList<>();
	static List<twitter4j.User> femaleList = new ArrayList<>();

	GenderClassification()
	{
		// default constructor...
	}

	/**
	 * This method just separates male, female, and unknown users into groups.
	 * 
	 * @param userMap
	 *            A map containing a user and their integer value of whether
	 *            they are known to be male (1) female (0), -1 or -2 unknown.
	 *            Remember -1 represents a real name found in both files and -2
	 *            means the name was not detected in either male or female name
	 *            file.
	 * @param movedUsers
	 *            This list is needed to keep track of the original size of
	 *            users.
	 * @throws IOException 
	 */
	public static void CheckGender(Map<twitter4j.User, Integer> userMap,
			List<edse.edu.com.User> movedUsers) throws IOException
	{

		System.out.println(userMap.size());
		List<Map<twitter4j.User, Integer>> unknownUserList = new ArrayList<>();

		Map<twitter4j.User, Integer> unknown = new HashMap<>();

		// GO AHEAD AND SEPARATE OUT NOW AND ASSIGN A NEW MAP TO THE UPDATED
		// UNKKNOWN USERS MAP BEFORE BEGINNING CLASSIFICATION.
		for (Entry<User, Integer> user : userMap.entrySet())
		{

			int mFVal = user.getValue();

			twitter4j.User userInMap = user.getKey();

			if (mFVal == 1)
			{

				maleList.add(userInMap);
			} else if (mFVal == 0)
			{
				femaleList.add(userInMap);
			} else if (mFVal == -2)
			{

				unknown.put(userInMap, mFVal);
				unknownUserList.add(unknown);
			}

		}

		 System.out.println("UNKNOWN USER LIST " + unknownUserList.size());
		System.out.println("maleLIST " + maleList.size());
		System.out.println("femaleLIST " + femaleList.size());

		// GenderClassification convert = new GenderClassification();

		// Uncomment these lines to call the method to build the training set
		// and make an arff file.
		// If this is wanted then multiple lines need to be uncommented down in
		// the MakeTrainingSet method.
		// At the moment the output goes correctly to the file but minor editing
		// needs to be done
		// since there are occasionally a number of very few line breaks that
		// happen. This was
		// corrected manually by myself in the file.

		// convert.MakeTrainingSet(0.0, femaleList,
		// movedUsers);
		// convert.MakeTrainingSet(1.0, maleList,
		// movedUsers);

		GenderClassification.CheckUnknowns(unknown, movedUsers);

	}

	/**
	 * This method takes each unknown user's information and sends it to the
	 * FilteredClassifier.java class for prediction on whether the user is male
	 * or female.
	 * 
	 * @param unknownMap
	 *            Map of the unknown users and their -1 or -2 value.
	 * @param movedUsers
	 *            To keep track of original user size.
	 * @throws IOException 
	 */
	public static void CheckUnknowns(Map<twitter4j.User, Integer> unknownMap,
			List<edse.edu.com.User> movedUsers) throws IOException
	{
		StringBuffer tweetText = new StringBuffer();

		List<twitter4j.User> otherMaleList = new ArrayList<>();
		List<twitter4j.User> otherFemaleList = new ArrayList<>();

		// This is kind of a wacky way of doing this but, since the list of
		// twitter4j didn't have text returned
		// as well, I needed to grab the each users tweets from the movedUsers
		// List that I already had.
		String textGiven = null;
		System.out.println("SIZE OF MOVEDUSERS AND MDUSER" + unknownMap.size()
				+ " " + movedUsers.size());

		for (edse.edu.com.User mdUser : movedUsers)
		{

			for (twitter4j.User userKey : unknownMap.keySet())
			{
				if (mdUser.getUserName().equals(userKey.getScreenName()))
				{
					// get all text from each mdUser.
					for (Tweet tweetString : mdUser.getTweets())
					{

						tweetText
								.append(tweetString.getTweet_text().toString());

					}
					// NOW CALL CALL THE CLASSIFIER AND DATASET TO DETERMINE
					// WHETHER THE TEXT
					// WAS WRITTEN BY A MALE OR FEMALE.

					textGiven = tweetText.toString();

					try
					{
						if (textGiven != null || textGiven != "")
						{
							System.out
									.println("TWEET TEXT " + textGiven + "\n");

							// FilteredLearner.init();

							String userClassifiedAs = FilteredClassifier
									.classifyNewInstance(userKey.getName(),
											userKey.getScreenName(),
											userKey.getDescription(),
											tweetText.toString());

							// check if male of female returned and then place
							// into arraylist.

							if (userClassifiedAs.equals("MALE"))
							{
								otherMaleList.add(userKey);
							} else if (userClassifiedAs.equals("FEMALE"))
							{
								otherFemaleList.add(userKey);
							}

						}

					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						System.out
								.println("Possible exception with calling WekaNaiveBayes class");
						e.printStackTrace();
					}

					// Done with this user, break out of the loop and go to the
					// next user.
					textGiven = null;
					tweetText.setLength(0);
					break;

				}

			}
		}

		// adding newly classified individuals to already known male and female
		// lists.

		// System.out.println("OTHER FEMALE " + otherFemaleList.size());
		// System.out.println("OTHER MALE " + otherMaleList.size());

		// Combining the originally known males and female with the predicted
		// gender users returned
		// from the classifier. Since the list of users was in the form of
		// twitter4j.User objects and
		// those do not contain text I needed to get them back into the form of
		// edse.edu.com.User objects.
		// This is kind of cumbersome but it did the job.
		GenderClassification convert = new GenderClassification();

		Collection<edse.edu.com.User> fUsers = new ArrayList<edse.edu.com.User>();
		Collection<edse.edu.com.User> mUsers = new ArrayList<edse.edu.com.User>();

		ArrayList<edse.edu.com.User> oFemaleUsers = new ArrayList<>();
		ArrayList<edse.edu.com.User> oMaleUsers = new ArrayList<>();

		fUsers = convert.ConvertList(0.0, femaleList, movedUsers);

		mUsers = convert.ConvertList(1.0, maleList, movedUsers);

		oFemaleUsers = convert.ConvertList(0, otherFemaleList, movedUsers);
		oMaleUsers = convert.ConvertList(1, otherMaleList, movedUsers);

		// adding the two array lists together for males and females
		fUsers.addAll(oFemaleUsers);
		mUsers.addAll(oMaleUsers);

		// printing their sizes out
		System.out.println(fUsers.size());
		System.out.println(mUsers.size());
		
		GenderClassification.printOutClassifiedUsers(mUsers, fUsers);

		// Calling the tweet sentiment class to figure out the emotion behind
		// male and
		// female user text.
		//TweetSentiment.DetermineSentiment(mUsers, fUsers);
		
		//build the word frequencies and total frequencies.
		TweetSentiment.CommonWordFrequency(fUsers, false, "FEMALE");
		TweetSentiment.CommonWordFrequency(mUsers, false, "MALE");
		
		//print the results to the two files.
		TweetSentiment.CommonWordFrequency(mUsers, true, "MALE");
		TweetSentiment.CommonWordFrequency(fUsers, true, "FEMALE");
	}

	/**
	 * This method converts a list of twitter4j.User objects back to a list of
	 * edse.edu.com.User objects. This is needed in order to work with user text
	 * in the sentiment class.
	 * 
	 * @param gend
	 *            A value indicating if the list being passed is M or F.
	 * @param userList
	 *            The user list that needs converting
	 * @param mUsers
	 *            The original mUsers list to match screen names from and build
	 *            the new list.
	 * @return This returns the array list of edse.edu.com.User objects
	 *         converted from twitter4j.User.
	 */
	public ArrayList<edse.edu.com.User> ConvertList(double gend,
			List<twitter4j.User> userList, List<edse.edu.com.User> mUsers)
	{
		// to form a user we need to look up the user by screen name in the
		// movedUsers and then add that user and their
		// tweets into a new list
		ArrayList<edse.edu.com.User> convertedList = new ArrayList<>();

		for (edse.edu.com.User user : mUsers)
		{
			for (twitter4j.User twitter4ju : userList)
			{
				if (user.getUserName().equals(twitter4ju.getScreenName()))
				{
					user.setDescription(twitter4ju.getDescription());
					user.setRealName(twitter4ju.getName());
					convertedList.add(user);

					// we now have user name, screen name, description, and
					// their tweets in one list.
				}
			}
		}

		/*
		 * try { String gender = null; BufferedWriter outFile = new
		 * BufferedWriter(new FileWriter("C://goodusers.arff", true));
		 * outFile.write("@RELATION gender\n\n");
		 * outFile.write("@ATTRIBUTE scrname STRING\n");
		 * outFile.write("@ATTRIBUTE desc STRING\n");
		 * outFile.write("@ATTRIBUTE text STRING\n");
		 * outFile.write("@ATTRIBUTE class {MALE,FEMALE}\n\n");
		 * outFile.write("@DATA\n"); for(edse.edu.com.User user : convertedList)
		 * { List<Tweet> tweets = user.getTweets();
		 * 
		 * for(Tweet tweet : tweets) {
		 * 
		 * gender = (gend == 0.0)?"FEMALE":"MALE"; if((gend == 0.0 || gend ==
		 * 1.0) && user.getRealName() != null && user.getUserName() != null &&
		 * user.getDescription() != null && tweet.tweet_text != null && gender
		 * != null){ outFile.write("\"" + user.getRealName() + "\"" + "," + "\""
		 * + user.getUserName() + "\"" + "," + "\"" + user.getDescription() +
		 * "\"" + "," + "\"" + tweet.tweet_text.toString() + "\"" + "," + gender
		 * + "\n"); } } }
		 * 
		 * outFile.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		return convertedList;

	}
	
	public static void printOutClassifiedUsers(Collection<edse.edu.com.User> mUsers, Collection<edse.edu.com.User> fUsers)
	{
		try
		{
		BufferedWriter buffWM = new BufferedWriter(new FileWriter("//Users//justinedse//Documents//classfdMales.txt"));
		BufferedWriter buffWF = new BufferedWriter(new FileWriter("//Users//justinedse//Documents//classfdFemales.txt"));
		
		//for complete male profile, with UID.
		for(twitter4j.User twitterU : NameValidation.finalFilteredList)
		{
			for(edse.edu.com.User mUser : mUsers)
			{
				if(mUser.getUserName().equals(twitterU.getScreenName()))
				{
					buffWM.write(twitterU.getId() + "," + mUser.getUserName() + "," + mUser.getRealName() + "," + mUser.getDescription() + "," + "M\n");
				}
			}
		}
		
		
		
		//for complete female profile, with UID.
		for(twitter4j.User twitterU : NameValidation.finalFilteredList)
		{
		   for(edse.edu.com.User fUser : fUsers)
			{
			  if(fUser.getUserName().equals(twitterU.getScreenName()))
			  {
				 buffWF.write(twitterU.getId() + "," + fUser.getUserName() + "," + fUser.getRealName() + "," + fUser.getDescription() + "," + "F\n");
			  }
			}
		}
		
		
		
		
		
		
		//for(edse.edu.com.User mUser : mUsers)
		//{
			
			//buffWM.write(mUser.getUserName() + "," + mUser.getRealName() + "," + mUser.getDescription() + "," + "M\n");
		//}
		
		//for(edse.edu.com.User fUser : fUsers)
		//{
			//buffWF.write(fUser.getUserName() + "," + fUser.getRealName() + "," + fUser.getDescription() + "," + "F\n");
		//}
		
		buffWM.flush();
		buffWF.flush();
		buffWM.close();
		buffWF.close();
		
	}
		
	catch(IOException ioe)
	{
		ioe.printStackTrace();
	}
    
	}
}
