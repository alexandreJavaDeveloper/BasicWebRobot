package com.openanswers.robot;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class RobotTest
{
	private static final String OPEN_ANSWERS_URL = "https://www.openanswers.co.uk/careers/join-us";

	private final Robot robot;

	public RobotTest() throws IOException
	{
		this.robot = new Robot(RobotTest.OPEN_ANSWERS_URL);
	}

	@Test
	public void testRobot() throws Exception
	{
		final long finalValue = this.robot.getFinalValueUsingMethodGet();

		this.robot.sendPost(finalValue);
	}

	// If someone wants to test the private method in Robot.class
	@Test
	public void testExtract()
	{
		final String response = "<form method=\"post\" " + " \"action=\"\">           877 - 976 + 790 - 782 = <input size=6 type=\"text\" name=\"answer\" />\"";
		Assert.assertEquals("877 - 976 + 790 - 782", this.robot.extractResponse(response));
	}
}