package com.openanswers.robot;

import org.junit.Assert;
import org.junit.Test;

import com.openanswers.rules.RobotRules;

public class RobotRulesTest
{
	@Test
	public void testExecuteOperation()
	{
		long firstValue = 10;
		String operator = "+";
		long secondValue = 15;
		long executeOperation = RobotRules.executeOperation(firstValue, operator, secondValue);

		Assert.assertEquals(25, executeOperation);

		firstValue = 2;
		operator = "*";
		secondValue = 10;
		executeOperation = RobotRules.executeOperation(firstValue, operator, secondValue);

		Assert.assertEquals(20, executeOperation);
	}

	@Test
	public void testExistsMultiplierOperator() {
		final String[] responseSplitted = {"10", "+", "20", "-", "25", "+" , "30"};
		Assert.assertFalse(RobotRules.existsMultiplierOperator(responseSplitted));

		final String[] responseSplitted2 = {"10", "+", "20", "*", "25", "+" , "30"};
		Assert.assertTrue(RobotRules.existsMultiplierOperator(responseSplitted2));
	}

	@Test
	public void testGetFinalValue()
	{
		final String[] responseSplitted = {"10", "+", "20", "-", "25", "+" , "30"};
		long finalValue = RobotRules.getFinalValue(responseSplitted);
		Assert.assertEquals(35, finalValue);

		final String[] responseSplitted2 = {"10", "*", "20", "-", "25", "+" , "30"};
		finalValue = RobotRules.getFinalValue(responseSplitted2);
		Assert.assertEquals(205, finalValue);

		final String[] responseSplitted3 = {"10", "-", "20", "*", "25", "+" , "30"};
		finalValue = RobotRules.getFinalValue(responseSplitted3);
		Assert.assertEquals(-460, finalValue);

		final String[] responseSplitted4 = {"10", "-", "20", "+", "25", "*" , "30"};
		finalValue = RobotRules.getFinalValue(responseSplitted4);
		Assert.assertEquals(740, finalValue);
	}
}