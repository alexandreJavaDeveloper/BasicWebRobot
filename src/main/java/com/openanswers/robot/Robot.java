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

    private String headerField;

    public Robot(final String url) throws IOException
    {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        this.url = new URL(url);
        this.prepareConnectionGET();
        this.prepareConnectionPost();
    }

    private void prepareConnectionGET() throws IOException
    {
        this.connectionGET = (HttpURLConnection) this.url.openConnection();
        this.connectionGET.setRequestMethod("GET");
        this.connectionGET.setRequestProperty("User-Agent", Robot.USER_AGENT);
    }

    private void prepareConnectionPost() throws IOException
    {
        this.connectionPOST = (HttpsURLConnection) this.url.openConnection();
        this.connectionPOST.setRequestMethod("POST");
        this.connectionPOST.setRequestProperty("User-Agent", Robot.USER_AGENT);
        this.connectionPOST.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        this.connectionPOST.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        this.connectionPOST.setDoOutput(true);
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

        final String responseExtracted = this.extractResponse(response.toString());

        this.headerField = this.connectionGET.getHeaderField("Set-Cookie");

        System.out.println(response.toString());
        System.out.println("Operation to be executed [" + responseExtracted + "]");

        final String[] responseSplitted = responseExtracted.split(" ");

        final long finalValue = RobotRules.getFinalValue(responseSplitted);

        System.out.println("FinalValue: " + finalValue);

        //        in.close();

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
        final String urlParameters = String.format("answer=%d&submitbtn=Submit", new Long(finalValue));

        this.connectionPOST.setFixedLengthStreamingMode(urlParameters.getBytes().length);
        this.connectionPOST.setRequestProperty("Cookie", this.headerField);
        this.connectionPOST.setRequestProperty("cookies_accept", this.headerField);
        this.connectionPOST.setRequestProperty("Set-Cookie", this.headerField);

        final DataOutputStream dataOutputStreamPOST = new DataOutputStream(this.connectionPOST.getOutputStream());

        dataOutputStreamPOST.writeBytes(urlParameters);
        dataOutputStreamPOST.flush();
        dataOutputStreamPOST.close();

        final BufferedReader in = new BufferedReader(new InputStreamReader(this.connectionPOST.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }

        final String responseExtracted = this.extractResponse(response.toString());
        System.out.println(response.toString());
        System.out.println("Operation to be executed by POST [" + responseExtracted + "]");

        in.close();
    }
}