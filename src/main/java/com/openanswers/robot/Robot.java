package com.openanswers.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Robot
{
    private static final String USER_AGENT = "Mozilla/5.0";

    private final URL obj;

    public Robot(final String url) throws MalformedURLException
    {
        this.obj = new URL(url);
    }

    public void execute() throws Exception
    {
        this.sendGetAndExecute();
    }

    /**
     * Just to gain performance (execute all operations before closing the connection of HTTP request)
     * was chosen couple of executions in this method (send get and execute it then separating).
     *
     * @throws IOException
     */
    private void sendGetAndExecute() throws IOException
    {
        final HttpURLConnection con = (HttpURLConnection) this.obj.openConnection();

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

        System.out.println("responseExtracted: " + responseExtracted);

        in.close();
    }

    public String extractResponse(final String response)
    {
        return response.substring(response.indexOf("post\"  \"action=\"\">") + 29, response.indexOf("= <input")).trim();
    }

    /*  private void sendPost()
      {
          final URL obj = new URL(Robot.DEFAULT_URL);
          final HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

          //add reuqest header
          con.setRequestMethod("POST");
          con.setRequestProperty("User-Agent", Robot.USER_AGENT);
          con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

          final String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

          // Send post request
          con.setDoOutput(true);
          final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
          wr.writeBytes(urlParameters);
          wr.flush();
          wr.close();

          final int responseCode = con.getResponseCode();
          System.out.println("\nSending 'POST' request to URL : " + Robot.DEFAULT_URL);
          System.out.println("Post parameters : " + urlParameters);
          System.out.println("Response Code : " + responseCode);

          final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          final StringBuffer response = new StringBuffer();

          while ((inputLine = in.readLine()) != null)
          {
              response.append(inputLine);
          }
          in.close();

          //print result
          System.out.println(response.toString());
      }*/
}