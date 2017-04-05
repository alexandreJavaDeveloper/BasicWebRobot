package com.openanswers.robot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.openanswers.rules.RobotRules;

/**
 * Responsible to access via HTTP protocol the site "openanswers.co.uk" for getting
 * and returning information between this site and this application.
 *
 * There is simulations using jUnit tests to execute all methods of this class
 * and to following the best way to execute it.
 *
 * @author AlexandreSilva
 */
public class Robot
{
	private static final String USER_AGENT = "Mozilla/5.0";

	private final URL url;

	public Robot(final String url) throws MalformedURLException
	{
		this.url = new URL(url);
	}

	/**
	 * Execute the HTTP GET request to the specific URL where retrieve
	 * As String format the values and operators that is necessary to execute.
	 *
	 * There is some staffs as extract the string to execute the operation and
	 * have a result.
	 *
	 * @return final value of the all operation
	 * @throws IOException
	 */
	public long getFinalValueUsingMethodGet() throws IOException
	{
		final HttpURLConnection con = (HttpURLConnection) this.url.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", Robot.USER_AGENT);

		final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		final StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}

		final String responseExtracted = this.extractResponse(response.toString());

		final String[] responseSplitted = responseExtracted.split(" ");

		final long finalValue = RobotRules.getFinalValue(responseSplitted);

		System.out.println("FinalValue: " + finalValue);

		in.close();

		return finalValue;
	}

	/**
	 * Extract the {@value response} to have a better string to split it in the future
	 * to make some operations using "+", "-" or "*".
	 *
	 * @param response
	 * @return extracted response
	 */
	public String extractResponse(final String response)
	{
		return response.substring(response.indexOf("<form method=\"post\" action=\"\">") + 33, response.indexOf("= <input")).trim();
	}

	/**
	 * Execute the HTTP POST sending the data as {@value finalValue} to the URL specified.
	 * Is write the {@value finalValue} in the body of request and processed as a form
	 * (it's like a submit button).
	 *
	 * @param finalValue
	 * @throws IOException
	 */
	public void sendPost(final long finalValue) throws IOException
	{
		final HttpsURLConnection con = (HttpsURLConnection) this.url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", Robot.USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setDoOutput(true);

		final String urlParameters = "answer=" + finalValue;

		final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		final StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}

		in.close();
		System.out.println("Post response: " + response.toString());
	}
}