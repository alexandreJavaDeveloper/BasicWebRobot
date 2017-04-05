package com.openanswers.robot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
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

	private HttpsURLConnection connectionPOST;

	private HttpURLConnection connectionGET;

	private DataOutputStream dataOutputStreamPOST;

	public Robot(final String url) throws IOException
	{
		// This is used to maintain the session of the first and second request
		// without this, will not work, because would be like another and new request of another client.
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

		this.url = new URL(url);
		this.prepareConnectionGET();
		this.prepareConnectionPost();
	}

	/**
	 * Just to gain performance, I start some objects before to sending the GET request.
	 * @throws IOException
	 */
	private void prepareConnectionGET() throws IOException
	{
		this.connectionGET = (HttpURLConnection) this.url.openConnection();
		this.connectionGET.setRequestMethod("GET");
		this.connectionGET.setRequestProperty("User-Agent", Robot.USER_AGENT);
	}

	/**
	 * Just to gain performance, I start some objects before to sending the POST request.
	 * @throws IOException
	 */
	private void prepareConnectionPost() throws IOException
	{
		this.connectionPOST = (HttpsURLConnection) this.url.openConnection();
		this.connectionPOST.setRequestMethod("POST");
		this.connectionPOST.setRequestProperty("User-Agent", Robot.USER_AGENT);
		this.connectionPOST.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		this.connectionPOST.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		this.connectionPOST.setDoOutput(true);

		this.dataOutputStreamPOST = new DataOutputStream(this.connectionPOST.getOutputStream());
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
		final BufferedReader in = new BufferedReader(new InputStreamReader(this.connectionGET.getInputStream()));

		String inputLine;
		final StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}

		final String extractedResponseOperation = this.extractOperation(response.toString());

		System.out.println("Operation from GET: " + extractedResponseOperation);

		final String[] responseSplitted = extractedResponseOperation.split(" ");

		final long finalValue = RobotRules.getFinalValue(responseSplitted);

		return finalValue;
	}

	/**
	 * Extract the {@value response} to have a better string to split it in the future
	 * to make some operations using "+", "-" or "*".
	 *
	 * @param response
	 * @return extracted response
	 */
	public String extractOperation(final String response)
	{
		return response.substring(response.indexOf("<form method=\"post\" action=\"\">") + 33, response.indexOf("= <input")).trim();
	}

	private String extractResult(final String response)
	{
		return response.substring(response.indexOf("</form>	</div>") + 18, response.indexOf("</p>	</div></div>") - 5).trim();
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
		final String urlParameters = String.format("answer=%d&submitbtn=Submit", finalValue);

		this.dataOutputStreamPOST.writeBytes(urlParameters);

		final BufferedReader inPOST = new BufferedReader(new InputStreamReader(this.connectionPOST.getInputStream()));

		String inputLine;
		final StringBuffer response = new StringBuffer();

		while ((inputLine = inPOST.readLine()) != null)
		{
			response.append(inputLine);
		}

		final String extractResult = this.extractResult(response.toString());

		System.out.println("Extract result: " + extractResult);

		System.out.println("Response: " + response.toString());

		this.dataOutputStreamPOST.close();
		inPOST.close();
	}
}