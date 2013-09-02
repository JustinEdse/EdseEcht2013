//Here we want to take each username from the list of type User and use the Twitter4J API to get the user's real name.
//Once the real name has been obtained we can cross check it with both male and female common names and store the result
//in a map or some other structure. Of course the name could be not found or may be in both files. If it is either of these two
//cases further explanation will be needed. Also all the text of each user's tweets must be checked for gender classification.
//We check all of their tweets so more text is available to analyze. Using both a real name and the text of a user's tweets,
//a classification can be made whether or not they're male or female...
package edse.edu.com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.text.Normalizer;

import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class NameValidation
{

	// open the male and female text files to get ready for validation.
	// iterate through the list of users, check each m and f file.
	// check cases, call genderclassification for text class!!!!
	static ConfigurationBuilder cb;
	static Twitter twitter;
	// ResponseList<twitter4j.User> returnUserInfoList;

	static int q = 0;
	static List<User> gblMovedUsers = new ArrayList<User>();
	static String lineInMale = "";
	static String lineInFemale = "";
	static String fullName = "";

	static BufferedReader readMale;
	static BufferedReader readFemale;

	public static void check_name(List<User> movedUsers) throws IOException
	{
		// consumer key, consumer secret, etc.
		cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("Nfqi3CStffNi7TJMyZQhw");
		cb.setOAuthConsumerSecret("m6RFWLgQx9CvHzmJteEX5F21s3iOdmO4pUqjiO4K5D4");
		cb.setOAuthAccessToken("416099988-KC0pUGgQ9ATx85FkGywXQHrtdCUNlf9X3DCM91HW");
		cb.setOAuthAccessTokenSecret("CUjqMeykmPjMz1UEjQx2wXZRqrKvwuAUfn9Lhh9qMlc");

		// TWITTER OBJECT STARTS HERE. NEED TO FIGURE OUT RATE LIMIT EXCEEDED
		// LISTENER.
		twitter = new TwitterFactory(cb.build()).getInstance();
		ArrayList<twitter4j.User> returnUserInfoList = new ArrayList<twitter4j.User>();

		// lineInFemale = null;
		// fullName = null;

		Map<Integer, twitter4j.User> MFMap = new HashMap<Integer, twitter4j.User>();

		try
		{

			System.out.println("male" + readMale + " " + readFemale);
			// CHECKING WHETHER OR NOT EACH USERNAME STILL HAS A VALID TWITTER
			// ACCOUNT

			Iterator<User> it = movedUsers.iterator();

			while (it.hasNext())
			{

				try
				{
					User u = it.next();
					twitter4j.User receivedUser = twitter.showUser(u
							.getUserName());

				} catch (TwitterException te)
				{
					// all requests past 150 per hour return a 400 not a 404.
					if (te.getStatusCode() == 404)
					{
						it.remove();
					}

				}
			}
			System.out.println(movedUsers.size());
			gblMovedUsers = movedUsers;
			int callsToDo = movedUsers.size();
			int numToDo = (((callsToDo + 99) / 100) * 100) / 100;

			System.out.println("CALLS TO DO ARE " + numToDo);
			int k = 0;
			int keepTrackSizeOfUsers = 0;
			// movedUsers.size();

			while (k < numToDo)
			{
				System.out.println("in loop");
				ResponseList<twitter4j.User> initialReturn = CallTwitterAPI(keepTrackSizeOfUsers);
				returnUserInfoList.addAll(initialReturn);

				k++;
				System.out.println("this is k " + k);
				keepTrackSizeOfUsers += 100;

			}

			// Now I have each users real name registered from twitter in the
			// returnUserInfoList. I can use this and the male/female file to
			// start to get a gauge on whether this person might be male or
			// female.

			System.out.println("\n\n HEY  user list size is" + returnUserInfoList.size());

			for (twitter4j.User tuser : returnUserInfoList)
			{
				// Need to reopen or establish the filereader each time
				// a new user needs to be checked for male or female.
				// If this is not done readMale and readFemale are NULL.
				readMale = new BufferedReader(new FileReader(
						"C://data//male//male.txt"));
				readFemale = new BufferedReader(new FileReader(
						"C://data//female//female.txt"));

				String realName = tuser.getName();
				System.out.println(realName);
				int result = NameValidation.CheckGenderByFile(realName);
				
				if(result == -1 || result == 1 || result == 0 || result == -2){
				MFMap.put(result, tuser);}
				
				

				// CALL GENDER CLASSIFICATION CLASS TO FURTHER CONFIRM WHETHER
				// OR NOT
				// THE RESULT PARAMETER OF A 0 OR A 1 WAS CORRECT.
				System.out.println("AT PROBABILITY\n" + MFMap.size());
				
				

			}
			
			System.out.println("INNAMEVALIDATION BEFOE CALL TO GENDERCLASSIFICATION!!! " + MFMap.size() + "\t" + gblMovedUsers.size() + "\t\n " + q);
			double probablity = GenderClassification.CheckGender(MFMap,
						gblMovedUsers);
			// closing male and female buffers here.
			readMale.close();
			readFemale.close();
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public static int CheckGenderByFile(String initialName) throws IOException
	{

		
		int result = 0;
		boolean mMatch = false;
		boolean fMatch = false;
		String realName = null;

		// array with characters to remove from real name provided.
		//String[] charsToRemove = new String[3];
		//charsToRemove[0] = "?";
		//charsToRemove[1] = "@";
		//charsToRemove[2] = ".";
		//String fixedName = initialName;
		//for (int i = 0; i < charsToRemove.length; i++)
		//{
			//fixedName = initialName.replaceAll(charsToRemove[i], " ");

		//}
		 String fixedName = initialName.replaceAll("[?]", " ");
		// String.

		if (fixedName.contains(" "))
		{
			String nameArr[] = fixedName.split(" ");
			realName = nameArr[0];
		} else
		{
			String realSplit = NameValidation.splitCamelCase(fixedName);
			String endResult[] = realSplit.split(" ");
			realName = endResult[0];
		}

		try
		{
			while ((lineInMale = readMale.readLine()) != null)
			{
				if (realName.equalsIgnoreCase(lineInMale))
				{
					mMatch = true;
					System.out.println("MALE MATCH DETECTED");
					break;
				}

			}

			while ((lineInFemale = readFemale.readLine()) != null)
			{
				if (realName.equalsIgnoreCase(lineInFemale))
				{
					fMatch = true;
					System.out.println("FEMALE MATCH DETECTED");
					break;
				}
			}

			// different scenarios of matches in the file of male & female.

			if (fMatch == true && mMatch == false)
			{
				result = 0;
				System.out.println("FEMALE");
			} else if (mMatch == true && fMatch == false)
			{
				result = 1;
				System.out.println("MALE");
			} else if (mMatch == true && fMatch == true)
			{
				// don't know....by name.
				// CALL GENDER VERIFICATION IN OTHER CLASS TO STRENGTHEN
				// ARGUMENT OF WHETHER A PERSON IS MALE OR FEMALE
				// BY LOOKING AT SEVERAL OF THEIR TWEET TEXTS.
				result = -1;
				System.out.println("NAME MATCHED IN BOTH FILES");

			} else if (mMatch == false && fMatch == false)
			{
				result = -2;
				System.out.println("NO NAME MATCH IN EITHER FILE");
			}

		} catch (IOException ioe)
		{

			ioe.printStackTrace();
		}
		
		q++;

		return result;
	}

	public static ResponseList<twitter4j.User> CallTwitterAPI(int keepTrackSize)
	{

		System.out.println(keepTrackSize);
		String[] tempArr = new String[100];
		ResponseList<twitter4j.User> tempList = null;
		int startPosition = 0;
		int catchStop = 0;

		for (startPosition = keepTrackSize; startPosition < gblMovedUsers
				.size(); startPosition++)
		{
			System.out.println(startPosition);
			System.out.println(keepTrackSize);

			tempArr[catchStop] = gblMovedUsers.get(startPosition).getUserName();

			catchStop++;

			if (catchStop == 100)
			{
				break;
			}

		}

		try
		{
			// returning value into tempList.
			System.out.println(tempArr.length);

			tempList = twitter.lookupUsers(tempArr);
			System.out.println("size to call api with " + tempList.size());
		} catch (TwitterException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Arrays.fill(tempArr, "");
		catchStop = 0;
		return tempList;

	}

	
	// A handy method to Split real names at camel casing if the user
	// wrote something such as this; ChristopherPolini or christopherPolini.
	static String splitCamelCase(String s)
	{
		return s.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
}
