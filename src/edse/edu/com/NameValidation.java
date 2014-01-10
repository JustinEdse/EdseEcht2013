/*
 * Author: Justin Edse
 * Title: NameValidation.java
 * Purpose: Here we want to take each username from the list of type User and use the Twitter4J API to get the user's real name.
 * Once the real name has been obtained we can cross check it with both male and female common names and store the result
 * in a map or some other structure. Of course the name could be not found or may be in both files. If it is either of these two
 * cases further explanation will be needed. Also all the text of each user's tweets must be checked for gender classification.
 * We check all of their tweets so more text is available to analyze. Using both a real name and the text of a user's tweets,
 * a classification can be made whether or not they are male or female.
 * Date: August, September 2013
 */
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
	// check cases, call genderclassification for text class.
	static ConfigurationBuilder cb;
	static Twitter twitter;

	static List<User> gblMovedUsers = new ArrayList<User>();
	static String lineInMale = "";
	static String lineInFemale = "";
	static String fullName = "";

	static BufferedReader readMale;
	static BufferedReader readFemale;

	// Common keywords that may be used in a twitter profile description that
	// may potentially set
	// a woman and a man apart.
	static String[] maleDesc = { " Boyfriend ", " boyfriend ", " Husband ",
			" husband ", " Father ", " father ", " Son ", " son ", " Uncle ",
			" uncle ", " Grandpa ", " grandpa ", " Grandfather ",
			" grandfather ", " Brother ", " brother " };
	static String[] femaleDesc = { " Girlfriend ", " girlfriend ", "Mother ",
			" mother", " Wife ", " wife ", " Aunt ", " aunt ", " Mother ",
			" mother ", " Sister", " sister ", " Grandmother ",
			" grandmother ", " grandma ", " Grandma " };

	/**
	 * The first task method does is go through the movedUsers list and begins
	 * to call the method, CallTwitterAPI. The way this has to be accomplished
	 * is rather quirky but in order to get all of users real names found in
	 * their profile information and not exceed the Twitter API rate limt is to
	 * send/receive user information in increments of 100. Then once the real
	 * names are returned method effectively goes through the twitter users in
	 * the list movedUsers and opens two text files containing thousands of male
	 * and female common first names from 1960 - 2010 (Thank you to InfoChimps
	 * for this data).
	 * 
	 * The any matches of names in the text file and the real user are kept
	 * track of. During this process the user's profile is also looked at
	 * briefly.
	 * 
	 * @param movedUsers
	 * @throws IOException
	 */
	public static void check_name(List<User> movedUsers) throws IOException
	{
		// consumer key, consumer secret, access token, and access token secret.
		// These are required in order
		// to successfully use the Twitter4j API.
		cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("Nfqi3CStffNi7TJMyZQhw");
		cb.setOAuthConsumerSecret("m6RFWLgQx9CvHzmJteEX5F21s3iOdmO4pUqjiO4K5D4");
		cb.setOAuthAccessToken("416099988-KC0pUGgQ9ATx85FkGywXQHrtdCUNlf9X3DCM91HW");
		cb.setOAuthAccessTokenSecret("CUjqMeykmPjMz1UEjQx2wXZRqrKvwuAUfn9Lhh9qMlc");

		twitter = new TwitterFactory(cb.build()).getInstance();
		ArrayList<twitter4j.User> returnUserInfoList = new ArrayList<twitter4j.User>();

		Map<twitter4j.User, Integer> MFMap = new HashMap<twitter4j.User, Integer>();

		try
		{

			System.out.println("male" + readMale + " " + readFemale);

			// Here the needed number of API calls in increments of 100 need to
			// be figured out.
			gblMovedUsers = movedUsers;
			int callsToDo = movedUsers.size();
			int numToDo = (((callsToDo + 99) / 100) * 100) / 100;
			System.out.println("NUM TO DO CORRECT " + numToDo);
			int k = 0;
			int keepTrackSizeOfUsers = 0;

			while (k < numToDo)
			{
				System.out.println("in loop");
				ResponseList<twitter4j.User> initialReturn = CallTwitterAPI(keepTrackSizeOfUsers);

				if (initialReturn != null)
				{
					returnUserInfoList.addAll(initialReturn);
				}

				k++;
				System.out.println("this is k " + k);
				keepTrackSizeOfUsers += 100;

				if (keepTrackSizeOfUsers % 18000 == 0)
				{
					try
					{
						Thread.sleep(840 * 1000);

					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

			}

			List<twitter4j.User> finalFilteredList = NameValidation
					.filterCompanies(returnUserInfoList);
			// Now I have each users real name registered from twitter in the
			// returnUserInfoList. I can use this and the male/female file to
			// start to get a gauge on whether this person might be male or
			// female.

			int c = 0;
			int p = 0;
			ArrayList<String> bothFiles = new ArrayList<>();
			for (twitter4j.User tuser : finalFilteredList)
			{
				// Need to reopen or establish the filereader each time
				// a new user needs to be checked for male or female.
				// If this is not done readMale and readFemale are NULL.
				readMale = new BufferedReader(new FileReader(
						"//Users//justinedse//Desktop//maleCensus.txt"));
				readFemale = new BufferedReader(new FileReader(
						"//Users//justinedse//Desktop//femaleCensus.txt"));

				String realName = tuser.getName();
				String checkScreenName = tuser.getScreenName();
				String userDesc = tuser.getDescription();
				System.out.println(realName);
				
				int result = NameValidation.CheckGenderByFile(realName,
						checkScreenName, userDesc);

				
				if (result == 1 || result == 0 || result == -2)
				{
					MFMap.put(tuser, result);
				}

				// CALL GENDER CLASSIFICATION CLASS TO FURTHER CONFIRM WHETHER
				// OR NOT THE RESULT PARAMETER OF A 0 OR A 1 WAS CORRECT.

			}
			/*
			 * System.out.println("in both files \n" + p);
			 * System.out.println(bothFiles);
			 * System.out.println("in neither fields " + c); System.out
			 * .println(
			 * "INNAMEVALIDATION BEFOE CALL TO GENDERCLASSIFICATION!!! " +
			 * MFMap.size() + "\t" + gblMovedUsers.size() + "\t\n " + q);
			 */
			GenderClassification.CheckGender(MFMap, gblMovedUsers);
			// closing male and female buffers here.
			readMale.close();
			readFemale.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	/**
	 * This method compared the user's real name with any matches in either the
	 * male or female text files. It also checks to see if their screen name
	 * matches anything from the text file or if their profile description
	 * contains hint keywords as to whether they are male or female.
	 * 
	 * @param initialName
	 *            This parameter is used for cleaning up the string with regex
	 * @param screenName
	 *            The user's screen name
	 * @param userDesc
	 *            The user's profile description
	 * @return the result of whether they were male, female, in both files, or
	 *         not found in either of the files.
	 * @throws IOException
	 *             This is to catch any IO problems that may happen when reading
	 *             the two names text files.
	 */
	public static int CheckGenderByFile(String initialName, String screenName,
			String userDesc) throws IOException
	{

		// setup variables for assigning whether a match in a male or female
		// file took place.
		int result = 0;
		boolean mMatch = false;
		boolean fMatch = false;
		String realName = null;
		String realScreenName = null;
		String[] maleCensusData = null;
		String[] femaleCensusData = null;

		// Using regex here to split the user's screen name at a capital letter
		// and
		// get their first name
		String fixedName = initialName.trim().replaceAll("[^a-zA-Z\u00D6\u00F6\u00DC\u00FC\u00C4\u00E4\u00DF]", " ");
		String fixedSC = screenName.trim().replaceAll("[^a-zA-Z\u00D6\u00F6\u00DC\u00FC\u00C4\u00E4\u00DF]", " ");

		// more regex cleanup, checking if screen name contains a change from
		// lower case to upper case letters such as willBrown. If not then this
		// also
		// checks if the screen name contains a space separating a first and
		// last name.
		if (fixedSC.contains(" "))
		{

			String scArr[] = fixedSC.split(" ");
			realScreenName = scArr[0];
		}
		else
		{
			String realSCSplit = NameValidation.splitCamelCase(fixedSC);
			String endSC[] = realSCSplit.split(" ");
			realScreenName = endSC[0];
		}

		if (fixedName.contains(" "))
		{
			String nameArr[] = fixedName.split(" ");
			realName = nameArr[0];
		}
		else
		{
			String realSplit = NameValidation.splitCamelCase(fixedName);
			String endResult[] = realSplit.split(" ");
			realName = endResult[0];
		}

		try
		{
			// reading in the text from the files...
			while ((lineInMale = readMale.readLine()) != null)
			{
				// maleCensusData[0] = name
				// maleCensusData[1] = frequency of the name being used
				maleCensusData = lineInMale.split("\t");

				if (realName.toLowerCase().equals(maleCensusData[0])
						|| (realScreenName.toLowerCase()
								.equals(maleCensusData[0])))
				{

					mMatch = true;
					System.out.println("MALE MATCH DETECTED");

					break;
				}

			}

			while ((lineInFemale = readFemale.readLine()) != null)
			{

				femaleCensusData = lineInFemale.split("\t");

				if (realName.toLowerCase().equals(femaleCensusData[0])
						|| (realScreenName.toLowerCase()
								.equals(femaleCensusData[0])))
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
			}
			else if (mMatch == true && fMatch == false)
			{
				result = 1;
				System.out.println("MALE");
			}
			else if (mMatch == true && fMatch == true)
			{

				double femaleFreq = Double.parseDouble(femaleCensusData[1]);
				double maleFreq = Double.parseDouble(maleCensusData[1]);
				// CHECK FREQUENCY OF CENSUS DATA.

				if (maleFreq > femaleFreq)
				{
					result = 1;
				}
				else if (maleFreq < femaleFreq)
				{
					result = 0;
				}

			}
			else if (mMatch == false && fMatch == false)
			{

				// IN THIS SCENARIO WE NEED TO RELY ON THE CLASSIFIER OBJECT
				// FROM WEKA TO CLASSIFY
				// THE INSTANCE.
				//FIX NEGATIVE ONE RETURN FROM ABOVE!!!!!!!!!!!!
				/*
				if (stringContainsItemFromList(userDesc, maleDesc))
				{
					result = 1;
				}
				else if (stringContainsItemFromList(userDesc, femaleDesc))
				{
					result = 0;
				}
				else
				{
					result = -2;
					System.out.println("NO NAME MATCH IN EITHER FILE");
				}
				*/
				result = -2;
			}

		}
		catch (IOException ioe)
		{

			ioe.printStackTrace();
		}

		return result;
	}

	/**
	 * This method is the one that calls the Twitter API and increments of 100
	 * and gets back a bundle of information about each user. This information
	 * is representing by the twitter4j.User object. It contains a slew of info
	 * such as real name, profile picture url, number of followers, number
	 * followed, and a ton of other useful things.
	 * 
	 * @param keepTrackSize
	 *            This variable keeps track of how many API calls are left in
	 *            relation to the number of users in the list (ie. for 600 users
	 *            it would take 6 calls). This is a great way to avoid exceeding
	 *            the Twitter rate limit and getting into further complications
	 *            dealing with your twitter application settings.
	 * @return This returns a response list of twitter4j.User objects.
	 */
	public static ResponseList<twitter4j.User> CallTwitterAPI(int keepTrackSize)
	{

		// Needing to setup an array since the Twitter4j method lookUpUsers only
		// takes
		// an array type and not ArrayList.

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
			// Also need to watch out for any network errors which may occur
			// here.
			tempList = twitter.lookupUsers(tempArr);
			System.out.println("SIZE OF TEMP LIST GOT: " + tempList.size());
		}
		catch (TwitterException e)
		{
			// TODO Auto-generated catch block
			if (e.getStatusCode() == 404)
			{
				System.out.println("Client error");
			}
			else if (e.getStatusCode() == 503)
			{
				System.out
						.println("The Twitter Servers are up, but overloaded with requests. Try again later.");
			}
		}

		Arrays.fill(tempArr, "");
		catchStop = 0;
		return tempList;

	}

	/**
	 * A handy method to Split real names at camel casing if the user wrote
	 * something such as this; ChristopherPolini or christopherPolini.
	 * 
	 * @param This
	 *            parameter represents the users screen name or real name
	 * @return a formatted String
	 */
	public static String splitCamelCase(String s)
	{
		return s.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	/**
	 * Checks to see if the profile description contains any of the male or
	 * female keywords mention above at the top of the class.
	 * 
	 * @param inputString
	 *            the user's profile description string
	 * @param items
	 *            The array of keywords to check against.
	 * @return This returns a boolean value as to whether or not a keyword was
	 *         found.
	 */
	public static boolean stringContainsItemFromList(String inputString,
			String[] items)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (inputString.contains(items[i]))
			{
				return true;
			}
		}
		return false;
	}

	public static List<twitter4j.User> filterCompanies(
			List<twitter4j.User> users) throws IOException
	{
		// Go through the users list and check for usernames that belong to
		// companies.
		Iterator<twitter4j.User> it = users.iterator();
		BufferedReader reader = null;
		String readLine = "";
		while (it.hasNext())
		{

			reader = new BufferedReader(new FileReader(
					"//Users//justinedse//Desktop//companies.txt"));
			twitter4j.User u = it.next();

			while ((readLine = reader.readLine()) != null)
			{

				if (u.getScreenName().equalsIgnoreCase(readLine))
				{
					it.remove();
					break;
				}
			}

		}

		reader.close();
		return users;
	}

}
