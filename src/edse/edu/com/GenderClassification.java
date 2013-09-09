package edse.edu.com;

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
		
		
		
		//	GO AHEAD AND SEPARATE OUT NOW AND ASSIGN A NEW MAP TO THE UPDATED
		// UNKKNOWN USERS MAP BEFORE BEGINNING CLASSIFICATION!
		for(Entry<User, Integer> user: userMap.entrySet())
		{
			
		   int mFVal = user.getValue();
		   
		   twitter4j.User userInMap = user.getKey();
		   
		   if(mFVal == 1)
		   {
			   
			   maleList.add(userInMap);
		   }
		   else if(mFVal == 0)
		   {
			   femaleList.add(userInMap);
		   }
		   else if(mFVal == -1 || mFVal == -2)
		   {
			
			 
			 unknown.put(userInMap, mFVal);
			 unknownUserList.add(unknown);
		   }
		   
			
		}
		
		System.out.println("UNKNOWN USER LIST " + unknownUserList.size());
		System.out.println("maleLIST " + maleList.size());
		System.out.println("femaleLIST " + femaleList.size());
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
					
						tweetText.append(tweetString.getTweet_text());

					}
					// NOW CALL CALL THE CLASSIFIER AND DATASET TO DETERMINE
					// WHETHER THE TEXT
					// WAS WRITTEN BY A MALE OR FEMALE.

					
					textGiven = tweetText.toString();
					
					try
					{
						if (textGiven != null || textGiven != "") 
						{
							System.out.println("TWEET TEXT "
									+ textGiven + "\n");

							//FilteredLearner.init();
							String userClassifiedAs = FilteredClassifier.classifyNewInstance(tweetText
									.toString());

							//check if male of female returned and then place into arraylist.
							//BASED ON WHAT WAS RETURNED PLACE THE USER IN A MALE OR FEMALE ARRAYLIST
							//UNKOWN USERS??????????????????????/
							if(userClassifiedAs.equals("MALE"))
							{
								otherMaleList.add(userKey);
							}
							else if(userClassifiedAs.equals("FEMALE"))
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
					
					//Done with this user, break out of the loop and go to the next user.
					textGiven = null;
					tweetText.setLength(0);
					break;

				}

			}
		}

		//separate male and female users up into different arraylists.
	
		System.out.println("OTHER FEMALE " + otherFemaleList.size());
		femaleList.addAll(otherFemaleList);
		System.out.println("OTHER MALE " + otherMaleList.size());
		maleList.addAll(otherMaleList);
		
		System.out.println("FINAL MALE " + maleList.size());
		System.out.println("FINAL FEMALE " + femaleList.size());
		
		
		
		
		
		
	}
	
	public List<edse.edu.com.User> ConvertListType(List<twitter4j.User> userList)
	{
		
		return null;
		
	}
}
