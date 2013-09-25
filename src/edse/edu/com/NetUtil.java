/*
 * Author: Justin Edse, This code was found on an open source Android application for educational purposes at the following link:
 * http://www.java2s.com/Open-Source/Android-Open-Source-App/Social-Network/tweentiment/edu/sjsu/tweentiment/util/NetUtil.java.htm
 * Title: NetUtil.java
 * Purpose: To put together an HTTP connection to send the user text to the Viral Heat API for emotion evaluation.
 * Date: September 2013
 */

package edse.edu.com;

import java.io.*;
import java.net.*;

/**
 * Utility class for network utility methods
 */
public class NetUtil
{

	/**
	 * Call URL and get Response as String
	 * 
	 * @param urlStr
	 * URL to call
	 * @return response as String
	 * @throws IOException
	 */
	public static String httpGet(String urlStr) throws IOException
	{
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setInstanceFollowRedirects(true);

		if (conn.getResponseCode() != 200)
		{
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null)
		{
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		return sb.toString();
	}
}