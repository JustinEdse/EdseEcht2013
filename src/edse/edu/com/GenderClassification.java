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
	GenderClassification()
	{

	}

	public static double CheckGender(Map<twitter4j.User, Integer> userMap,
			List<edse.edu.com.User> movedUsers)
	{
		StringBuffer tweetText = new StringBuffer();
		ArrayList<String> listOfText = new ArrayList<String>();

		// This is kind of a wacky way of doing this but, since the list of
		// twitter4j didn't have text returned
		// as well, I needed to grab the each users tweets from the movedUsers
		// List that I already had.
		String textGiven = null;
		System.out.println("SIZE OF MOVEDUSERS AND MDUSER" + userMap.size()
				+ " " + movedUsers.size());
		for (edse.edu.com.User mdUser : movedUsers)
		{

			for (twitter4j.User userKey : userMap.keySet())
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

							FilteredLearner.init();
							FilteredClassifier.classifyNewInstance(tweetText
									.toString());
						}

					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						System.out
								.println("Possible exception with calling WekaNaiveBayes class");
						e.printStackTrace();
					}
					
					//Done with this user, break out of the loop and go to the next user.
					break;

				}

			}
		}

		//do work to compare navie bayes result and real name....
		return 0;
	}
}
