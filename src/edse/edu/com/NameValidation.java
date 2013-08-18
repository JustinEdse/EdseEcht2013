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
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class NameValidation
{

	// open the male and female text files to get ready for validation.
	// iterate through the list of users, check each m and f file.
	// check cases, call genderclassification for text class!!!!
	public NameValidation()
	{

	}

	public void check_name(ArrayList<User> listOfUsers) throws IOException,
			TwitterException
	{
		// consumer key, consumer secret, etc.
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("qCCd16UPfwj0GxtndGy36A");
		cb.setOAuthConsumerSecret("l24XaSmcyI1CjWVE2ct7Nbnu6lzk1BYrwcpYnaRyo");
		cb.setOAuthAccessToken("416099988-PVg4jwKkkMAZc8GZibrMh3iTgk6JxygMcTR9rxvb");
		cb.setOAuthAccessTokenSecret("HmHQTFVOrsE9od9UkLrcyJq7D2fZCz3SQgts8eKGdM");

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		String lineInMale, lineInFemale;
		boolean isMale = false, isFemale = false;
		Map<Integer, User> MFMap = new HashMap<Integer, User>();

		try
		{
			BufferedReader readMale = new BufferedReader(new FileReader(
					"//data/male/male.txt"));
			BufferedReader readFemale = new BufferedReader(new FileReader(
					"//data/female/female.txt"));

			for (User user : listOfUsers)
			{
				String userNameForLookUp = user.getUserName();
				// name from each user, now look up real name with Twitt4J

				twitter4j.User twitter4JUser = twitter
						.showUser(userNameForLookUp);
				String fullName = twitter4JUser.getName();
				String[] nameArray = fullName.split(" ");
				String firstName = nameArray[0];

				// cases to check for
				// Need to always check both files regardless and use boolean
				// values to indicate
				// if male = true, female = true, or male = true and female =
				// true, meaning
				// the name was found in both files. Or if the name was not
				// found analyze text like
				// regular.
				// MALE = (MALE NAME + TEXT MALEPOSITIVE) OR (NO MALE NAME + TEXT MALEPOSITIVE)
				// FEMALE = (FEMALE NAME + TEXT FEMALEPOSITIVE) OR (NO FEMALE NAME + TEXT FEMPOS)
				// (MALE AND FEMALE NAME + TEXT MALE OR FEMALE POSITIVE)
				if (firstName != null)
				{
					while ((lineInMale = readMale.readLine()) != null)
					{
						if (firstName.equalsIgnoreCase(lineInMale))
						{
							isMale = true;
						}
					}

					while ((lineInFemale = readFemale.readLine()) != null)
					{
						if (firstName.equalsIgnoreCase(lineInFemale))
						{
							isFemale = true;
						}
					}

				} 
				
				
				//DO TEXT ANALYSIS FEATURES ALL THE TIME REGARDLESS IF MALE AND FEMALE BOOL COMBINATIONS.
				//PASS THE USER OBJECT AS A PARAMETER TO THE GENDERCLASSIFICATION CLASS AND THEN RETURN A 
				//PROBABILITY OF MALE OR FEMALE AUTHOR. MUST ANALYZE ALL OF THE USER'S TWEETS IN THE OTHER CLASS.
				//ASSIGN A 1 FOR MALE AND A 0 FOR FEMALE USER.
				//int returnedEstimate = GenderClassification.EstimateGender(user);
				int returnedEstimate = 0;
				
				if(isMale == true && isFemale == false)
				{
					if(returnedEstimate == 1)
					{
						//male, assign to map for the given user.
						MFMap.put(returnedEstimate, user);
					}
				}
				else if(isFemale == true && isMale == false)
				{
					if(returnedEstimate == 0)
					{
						//female
						MFMap.put(returnedEstimate, user);
					}
					
				}
				else if(isMale == true && isFemale == true || (isMale == false && isFemale == false))
				{
					//assign based on returnedEstimate. name encountered in both files.
					MFMap.put(returnedEstimate, user);
				}

			}

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
