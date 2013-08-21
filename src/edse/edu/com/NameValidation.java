//Here we want to take each username from the list of type User and use the Twitter4J API to get the user's real name.
//Once the real name has been obtained we can cross check it with both male and female common names and store the result
//in a map or some other structure. Of course the name could be not found or may be in both files. If it is either of these two
//cases further explanation will be needed. Also all the text of each user's tweets must be checked for gender classification.
//We check all of their tweets so more text is available to analyze. Using both a real name and the text of a user's tweets,
//a classification can be made whether or not they're male or female

package edse.edu.com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class NameValidation
{


	// open the male and female text files to get ready for validation.
	// iterate through the list of users, check each m and f file.
	// check cases, call genderclassification for text class!!!!
   
	public static void check_name(List<User> movedUsers) throws IOException
	{
		// consumer key, consumer secret, etc.
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("Nfqi3CStffNi7TJMyZQhw");
		cb.setOAuthConsumerSecret("m6RFWLgQx9CvHzmJteEX5F21s3iOdmO4pUqjiO4K5D4");
		cb.setOAuthAccessToken("416099988-KC0pUGgQ9ATx85FkGywXQHrtdCUNlf9X3DCM91HW");
		cb.setOAuthAccessTokenSecret("CUjqMeykmPjMz1UEjQx2wXZRqrKvwuAUfn9Lhh9qMlc");

		//TWITTER OBJECT STARTS HERE. NEED TO FIGURE OUT RATE LIMIT EXCEEDED LISTENER.
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		
		String lineInMale, lineInFemale, fullName = null;
		boolean isMale = false, isFemale = false;
		BufferedReader readMale = null;
		BufferedReader readFemale = null;
		Map<Integer, User> MFMap = new HashMap<Integer, User>();

		try
		{
			readMale = new BufferedReader(new FileReader(
					"C://data//male//male.txt"));
			readFemale = new BufferedReader(new FileReader(
					"C://data//female//female.txt"));

			for (User user : movedUsers)
			{
				String userNameForLookUp = user.getUserName();
				//System.out.print("\n\n " + userNameForLookUp);
				// name from each user, now look up real name with Twitt4J
				if (userNameForLookUp != null)
				{
					twitter4j.User twitter4JUser = null;
					try
					{
						twitter4JUser = twitter.showUser(userNameForLookUp);
				
						
						fullName = twitter4JUser.getName();
						System.out.println("\t\t\t\t " + fullName);
						
					} 
					catch (TwitterException e)
					{
						// TODO Auto-generated catch block
						if (e.getStatusCode() == 404)
						{
							System.out
									.println("THIS USER DOES NOT EXIST. SKIP TO THE NEXT USER");
							continue;
						}else if(e.getStatusCode() == 88)
						{
							System.out.println("RATE LIMIT EXCEEDED ERROR");
							System.exit(1);
						}
						else if (fullName == null)
						{
							System.out.println("NAME IS NULL");
							continue;
						}
					}

				}

				String lowerCaseName = fullName.toLowerCase();
				String lowerCaseUN = userNameForLookUp.toLowerCase();
				
				// cases to check for
				// Need to always check both files regardless and use boolean
				// values to indicate
				// if male = true, female = true, or male = true and female =
				// true, meaning
				// the name was found in both files. Or if the name was not
				// found analyze text like
				// regular.
				// MALE = (MALE NAME + TEXT MALEPOSITIVE) OR (NO MALE NAME +
				// TEXT MALEPOSITIVE)
				// FEMALE = (FEMALE NAME + TEXT FEMALEPOSITIVE) OR (NO FEMALE
				// NAME + TEXT FEMPOS)
				// (MALE AND FEMALE NAME + TEXT MALE OR FEMALE POSITIVE)
				if (lowerCaseName != null)
				{
					while ((lineInMale = readMale.readLine()) != null)
					{
					
						if (lowerCaseName.contains(lineInMale.toLowerCase())
								|| lowerCaseUN.contains(lineInMale
										.toLowerCase()))
						{
							isMale = true;
							System.out.println("MALE MATCH");
							break;
						}
					}

					while ((lineInFemale = readFemale.readLine()) != null)
					{
						if (lowerCaseName.contains(lineInFemale.toLowerCase())
								|| lowerCaseUN.contains(lineInFemale
										.toLowerCase()))
						{
							isFemale = true;
							System.out.println("FEMALE MATCH");
							break;
						}
					}

				}

				// DO TEXT ANALYSIS FEATURES ALL THE TIME REGARDLESS IF MALE AND
				// FEMALE BOOL COMBINATIONS.
				// PASS THE USER OBJECT AS A PARAMETER TO THE
				// GENDERCLASSIFICATION CLASS AND THEN RETURN A
				// PROBABILITY OF MALE OR FEMALE AUTHOR. MUST ANALYZE ALL OF THE
				// USER'S TWEETS IN THE OTHER CLASS.
				// ASSIGN A 1 FOR MALE AND A 0 FOR FEMALE USER.
				// int returnedEstimate =
				// GenderClassification.EstimateGender(user);

				int returnedEstimate = 0;

				if (isMale == true && isFemale == false)
				{
					if (returnedEstimate == 1)
					{
						// male, assign to map for the given user.
						MFMap.put(returnedEstimate, user);
					}
				} else if (isFemale == true && isMale == false)
				{
					if (returnedEstimate == 0)
					{
						// female
						MFMap.put(returnedEstimate, user);
					}

				} else if (isMale == true && isFemale == true
						|| (isMale == false && isFemale == false))
				{
					// assign based on returnedEstimate. name encountered in
					// both files.
					MFMap.put(returnedEstimate, user);
				}

			}

		}

		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

        twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                System.out.println("onRateLimitStatus" + event);
               
            }

            public void onRateLimitReached(RateLimitStatusEvent event)
            {   //when the time reamining to call again hits zero I can request more information about a user.
            	try
				{
					Thread.sleep(2000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        });
		readMale.close();
		readFemale.close();

	}
	
	

	
}
