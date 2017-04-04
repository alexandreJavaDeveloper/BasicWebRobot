package com.openanswers.robot;

import java.net.MalformedURLException;

import org.junit.Test;

public class RobotTest
{
    private static final String OPEN_ANSWERS_URL = "https://www.openanswers.co.uk/careers/join-us";

    private final Robot robot;

    public RobotTest() throws MalformedURLException
    {
        this.robot = new Robot(RobotTest.OPEN_ANSWERS_URL);
    }

    @Test
    public void testRobot() throws Exception
    {
        this.robot.execute();
    }

    // If someone wants to test the private method in Robot.class
    @Test
    public void testExtract()
    {
        final String response = "<form method=\"post\" " + " \"action=\"\">           877 - 976 * 790 - 782 = <input size=6 type=\"text\" name=\"answer\" />\"";

        final String extractResponse = this.robot.extractResponse(response);
        System.out.println(extractResponse);

    }
}