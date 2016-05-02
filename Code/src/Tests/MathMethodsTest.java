package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Code.MathMethods;

public class MathMethodsTest
{

	/**
	 * Tests that the lengthDirX method works on length 0.
	 */
	@Test
	public void testLengthDirXZero()
	{
		assertTrue(MathMethods.lengthDirX(0, 0) == 0);
		assertTrue(MathMethods.lengthDirX(0, 90) == 0);
		assertTrue(MathMethods.lengthDirX(0, 180) == 0);
		assertTrue(MathMethods.lengthDirX(0, 1337) == 0);
	}
	
	/**
	 * Tests that the lengthDirX method works on length that is negative.
	 */
	@Test(expected=ArithmeticException.class)
	public void testLengthDirXNegative()
	{
		MathMethods.lengthDirX(-1, 0);
		MathMethods.lengthDirX(-1, 90);
		MathMethods.lengthDirX(-1, 180);
		MathMethods.lengthDirX(-1, 1337);
	}
	
	/**
	 * Tests that the lengthDirX method works on length that is positive.
	 */
	@Test
	public void testLengthDirXPositive()
	{
		assertTrue(MathMethods.lengthDirX(1, 0) == 1);
		assertTrue(MathMethods.lengthDirX(1, 180) == -1);
		assertEquals(Math.sqrt(2)/2, MathMethods.lengthDirX(1, 45), 0.0001);
	}

	/**
	 * Tests that the lengthDirY method works on length 0.
	 */
	@Test
	public void testLengthDirYZero()
	{
		assertTrue(MathMethods.lengthDirY(0, 0) == 0);
		assertTrue(MathMethods.lengthDirY(0, 90) == 0);
		assertTrue(MathMethods.lengthDirY(0, 180) == 0);
		assertTrue(MathMethods.lengthDirY(0, 1337) == 0);
	}
	
	/**
	 * Tests that the lengthDirY method works on length that is negative.
	 */
	@Test(expected=ArithmeticException.class)
	public void testLengthDirYNegative()
	{
		MathMethods.lengthDirY(-1, 0);
		MathMethods.lengthDirY(-1, 90);
		MathMethods.lengthDirY(-1, 180);
		MathMethods.lengthDirY(-1, 1337);
	}
	
	/**
	 * Tests that the lengthDirY method works on length that is positive.
	 */
	@Test
	public void testLengthDirYPositive()
	{
		assertTrue(MathMethods.lengthDirY(1, 90) == 1);
		assertTrue(MathMethods.lengthDirY(1, 270) == -1);
		assertEquals(Math.sqrt(2)/2, MathMethods.lengthDirY(1, 45), 0.0001);
	}
	
	/**
	 * Tests that the getDirectionBetweenPoints method works when the points
	 * are in the same spot.
	 */
	@Test
	public void testGetDirectionBetweenPointsZero()
	{
		assertEquals(0, MathMethods.getDirectionBetweenPoints(0, 0, 0, 0), 
						0.0001);
		assertEquals(0, MathMethods.getDirectionBetweenPoints(-4, 4, -4, 4), 
				0.0001);
	}
	
	/**
	 * Tests that the getDirectionBetweenPoints method works when the points
	 * aren't in the same spot.
	 */
	@Test
	public void testGetDirectionBetweenPointsNonZero()
	{
		assertEquals(45, MathMethods.getDirectionBetweenPoints(0, 0, 1, 1), 
				0.0001);
		assertEquals(180, MathMethods.getDirectionBetweenPoints(40, 0, -400, 0), 
				0.0001);
	}
	
	/**
	 * Tests that the clamp method works on variables inside the bounds.
	 */
	@Test
	public void testClampInside()
	{
		assertEquals(50, MathMethods.clamp(50, 0, 100), 0.0001);
	}
	
	/**
	 * Tests that the clamp method works on variables outside the low bound.
	 */
	@Test
	public void testClampOutsideLow()
	{
		assertEquals(0, MathMethods.clamp(-10, 0, 100), 0.0001);
	}
	
	/**
	 * Tests that the clamp method works on variables outside the high bound.
	 */
	@Test
	public void testClampOutsideHigh()
	{
		assertEquals(100, MathMethods.clamp(110, 0, 100), 0.0001);
	}
}
