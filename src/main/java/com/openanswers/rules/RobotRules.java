package com.openanswers.rules;

/**
 * Contains all rules necessary to execute functions of Robot class.
 * Only the following operators are allowed: '+' '-' '*'.
 *
 * @author AlexandreSilva
 */
public class RobotRules
{
	/**
	 * Provide the final value after getting the exact operation to be executed.
	 * Check which is the best way to execute the operations with the values
	 * from the {@value responseSplitted}.
	 *
	 * @param responseSplitted
	 * @return finalValue
	 */
	public static long getFinalValue(final String[] responseSplitted)
	{
		final int firstValue = Integer.valueOf(responseSplitted[0]);
		final int secondValue = Integer.valueOf(responseSplitted[2]);
		final int thirdValue = Integer.valueOf(responseSplitted[4]);
		final int fourthValue = Integer.valueOf(responseSplitted[6]);

		final String firstOperator = String.valueOf(responseSplitted[1]);
		final String secondOperator = String.valueOf(responseSplitted[3]);
		final String thirdOperator = String.valueOf(responseSplitted[5]);

		long finalValue;

		// If there is no multiplier operator, will be more easy and faster.
		if (!RobotRules.existsMultiplierOperator(responseSplitted))
		{
			final long firstResult = RobotRules.executeOperation(firstValue, firstOperator, secondValue);
			final long secondResult = RobotRules.executeOperation(firstResult, secondOperator, thirdValue);
			finalValue = RobotRules.executeOperation(secondResult, thirdOperator, fourthValue);
		} else
		{
			// checked that always will have only one operator with multiplier

			if (firstOperator.equals("*"))
			{
				final long firstResult = RobotRules.executeOperation(firstValue, firstOperator, secondValue);
				final long secondResult = RobotRules.executeOperation(firstResult, secondOperator, thirdValue);
				finalValue = RobotRules.executeOperation(secondResult, thirdOperator, fourthValue);
			}
			else if (secondOperator.equals("*"))
			{
				final long firstResult = RobotRules.executeOperation(secondValue, secondOperator, thirdValue);
				final long secondResult = RobotRules.executeOperation(firstValue, firstOperator, firstResult);
				finalValue = RobotRules.executeOperation(secondResult, thirdOperator, fourthValue);
			}
			else
			{ // third operator is multiplier
				final long firstResult = RobotRules.executeOperation(firstValue, firstOperator, secondValue);
				final long secondResult = RobotRules.executeOperation(thirdValue, thirdOperator, fourthValue);
				finalValue = RobotRules.executeOperation(firstResult, secondOperator, secondResult);
			}
		}

		return finalValue;
	}

	/**
	 * Execute the operation using the mathematical operator depending of the
	 * {@value operator} that is as string format. In this case is necessary to check out
	 * what is the exact operator to execute the {@value firstValue} and {@value secondValue}.
	 *
	 * @param firstValue
	 * @param operator
	 * @param secondValue
	 * @return operation executed
	 */
	public static long executeOperation(final long firstValue, final String operator, final long secondValue)
	{
		switch (operator)
		{
			case "*":
				return firstValue * secondValue;
			case "+":
				return firstValue + secondValue;
			case "-":
				return firstValue - secondValue;
			default:
				throw new UnsupportedOperationException();
		}
	}

	/**
	 * Check if in the {@value responseSplitted} variable contains multiplier
	 * operator. If so, return TRUE; if not, return FALSE.
	 *
	 * @param responseSplitted
	 * @return boolean
	 */
	public static boolean existsMultiplierOperator(final String[] responseSplitted)
	{
		return RobotRules.existsMultiplierOperator(responseSplitted[1]) //
				|| RobotRules.existsMultiplierOperator(responseSplitted[3]) //
				|| RobotRules.existsMultiplierOperator(responseSplitted[5]);
	}

	private static boolean existsMultiplierOperator(final String operator)
	{
		switch (operator)
		{
			case "*":
				return true;
			case "+":
				return false;
			case "-":
				return false;
			default:
				throw new UnsupportedOperationException();
		}
	}
}