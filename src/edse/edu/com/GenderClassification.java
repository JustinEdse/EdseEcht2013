package edse.edu.com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

	}

	public static void CheckGender(Map<twitter4j.User, Integer> userMap,
			List<edse.edu.com.User> movedUsers)
	{
		

		List<Map<twitter4j.User, Integer>> unknownUserList = new ArrayList<>();

		Map<twitter4j.User, Integer> unknown = new HashMap<>();

		// GO AHEAD AND SEPARATE OUT NOW AND ASSIGN A NEW MAP TO THE UPDATED
		// UNKKNOWN USERS MAP BEFORE BEGINNING CLASSIFICATION!
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
			} else if (mFVal == -1 || mFVal == -2)
			{

				unknown.put(userInMap, mFVal);
				unknownUserList.add(unknown);
			}

		}

		System.out.println("UNKNOWN USER LIST " + unknownUserList.size());
		System.out.println("maleLIST " + maleList.size());
		System.out.println("femaleLIST " + femaleList.size());
		
		//GenderClassification convert = new GenderClassification();
		
		//Call the method to build the training set and make an arff file.
		//convert.MakeTrainingSet(0.0, femaleList,
			//	movedUsers);
		//convert.MakeTrainingSet(1.0, maleList,
				//movedUsers);
		GenderClassification.CheckUnknowns(unknown, movedUsers);

	}

	public static void CheckUnknowns(Map<twitter4j.User, Integer> unknownMap,
			List<edse.edu.com.User> movedUsers)
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

						tweetText.append(tweetString.getTweet_text().toString());

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

							//FilteredLearner.init();
						
							String userClassifiedAs = FilteredClassifier
									.classifyNewInstance(userKey.getName(), userKey.getScreenName(), userKey.getDescription(),tweetText.toString());

							// check if male of female returned and then place
							// into arraylist.
							// BASED ON WHAT WAS RETURNED PLACE THE USER IN A
							// MALE OR FEMALE ARRAYLIST
							// UNKOWN USERS??????????????????????/
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

		// adding newly classified individuals to already known male and female lists.

		System.out.println("OTHER FEMALE " + otherFemaleList.size());
	    //System.out.println("REAL MALES " + maleList.size());
		//System.out.println("REAL FEMALES " + femaleList.size());
		System.out.println("OTHER MALE " + otherMaleList.size());
		//maleList.addAll(otherMaleList);

		//System.out.println("FINAL MALE " + maleList.size());
		//System.out.println("FINAL FEMALE " + femaleList.size());

		//GenderClassification convert = new GenderClassification();
		//convert.MakeTrainingSet(0.0, femaleList,
			//movedUsers);
		//convert.MakeTrainingSet(1.0, maleList,
			//movedUsers);
		
		
		
		

		
	}

	public void MakeTrainingSet(double gend,
			List<twitter4j.User> userList, List<edse.edu.com.User> mUsers)
	{
		// to form a user we need to look up the user by screen name in the
		// movedUsers and then add that user and their
		// tweets into a new list
		List<edse.edu.com.User> convertedList = new ArrayList<>();

		
		for(edse.edu.com.User user : mUsers)
		{
			for(twitter4j.User twitter4ju : userList)
			{
				if(user.getUserName().equals(twitter4ju.getScreenName()))
				{
					user.setDescription(twitter4ju.getDescription());
					user.setRealName(twitter4ju.getName());
					convertedList.add(user);
					
					//we now have user name, screen name, description, and their tweets in one list.
				}
			}
		}

	
		
		
		try
		{
			String gender = null;
			BufferedWriter outFile = new BufferedWriter(new FileWriter("C://goodusers.arff", true));
			outFile.write("@RELATION gender\n\n");
			outFile.write("@ATTRIBUTE scrname STRING\n");
			outFile.write("@ATTRIBUTE desc STRING\n");
			outFile.write("@ATTRIBUTE text STRING\n");
			outFile.write("@ATTRIBUTE class {MALE,FEMALE}\n\n");
			outFile.write("@DATA\n");
			for(edse.edu.com.User user : convertedList)
			{
			   List<Tweet> tweets = user.getTweets();
			   
			   for(Tweet tweet : tweets)
			   {
				   
				   gender = (gend == 0.0)?"FEMALE":"MALE";
				   if((gend == 0.0 || gend == 1.0) && user.getRealName() != null && user.getUserName() != null && user.getDescription() != null && tweet.tweet_text != null && gender != null){
				   outFile.write("\"" + user.getRealName() + "\"" + "," + "\"" + user.getUserName() + "\"" + "," + "\"" + user.getDescription() + "\"" + "," + "\"" + tweet.tweet_text.toString() + "\"" + "," + gender + "\n");
				   }
			   }
			}
			
			outFile.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// convertedList;

	}
}
