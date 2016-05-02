package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Code.Alarm;

public class AlarmTest
{
	/**
	 * Tests that the constructor without parameters correctly assigns 
	 * the fields.
	 */
	@Test
	public void testNonParameterConstructor()
	{
		Alarm a = new Alarm();
		assertEquals(a.tick(), 0);
	}
	
	/**
	 * Tests that the tick method correctly modifies the time when the alarm
	 * is negative.
	 */
	@Test
	public void testTickNegative()
	{
		Alarm a = new Alarm(-5);
		assertEquals(-5, a.tick());
	}
	
	/**
	 * Tests that the tick method correctly modifies the time when the alarm
	 * is zero.
	 */
	@Test
	public void testTickZero()
	{
		Alarm a = new Alarm(0);
		assertEquals(0, a.tick());
	}
	
	/**
	 * Tests that the tick method correctly modifies the time when the alarm
	 * is positive.
	 */
	@Test
	public void testTickPositive()
	{
		Alarm a = new Alarm(6);
		assertEquals(5, a.tick());
	}
	
	/**
	 * Tests that the done method works on negative numbers.
	 */
	@Test
	public void testDoneNegative()
	{
		Alarm a = new Alarm(-5);
		assertTrue(a.done());	
	}
	
	/**
	 * Tests that the done method works on 0.
	 */
	@Test
	public void testDoneZero()
	{
		Alarm a = new Alarm();
		assertTrue(a.done());	
	}
	
	/**
	 * Tests that the done method works on positive numbers.
	 */
	@Test
	public void testDonePositive()
	{
		Alarm a = new Alarm(1);
		assertFalse(a.done());
		a.tick();
		assertTrue(a.done());
	}
}
