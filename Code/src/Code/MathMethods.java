package game_tutorial;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class contains a handful of useful methods for calculating various
 * things, such as position.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
/**
 * @author fredrikomstedt
 *
 */
public class MathMethods
{
	/**
	 * Calculates the cathetus in the x direction from a hypotenuse and a 
	 * direction.
	 * @param length The length of the hypotenuse.
	 * @param direction The angle (based on unit circle) in degrees.
	 * @return The cathetus in the x direction.
	 */
	public static double lengthDirX(double length, double direction)
	{
		return Math.cos(Math.toRadians(direction))*length;
	}
	
	/**
	 * Calculates the cathetus in the y direction from a hypotenuse and a 
	 * direction.
	 * @param length The length of the hypotenuse.
	 * @param direction The angle (based on unit circle) in degrees.
	 * @return The cathetus in the y direction.
	 */
	public static double lengthDirY(double length, double direction)
	{
		return Math.sin(Math.toRadians(direction))*length;
	}
	
	/**
	 * Clamps a variable so it's between two bounds.
	 * @param var The variable to clamp.
	 * @param lowBound The lower bound.
	 * @param upBound The upper bound.
	 * @return the clamped variable.
	 */
	public static double clamp(double var, double lowBound, double upBound)
	{
		if(var < lowBound)
		{
			var = lowBound;
		}
		else if(var > upBound)
		{
			var = upBound;
		}
		return var;
	}
	
	/**
	 * Clamps a variable so it's between four bounds.
	 * @param var The variable to clamp.
	 * @param firstLowBound The first lower bound.
	 * @param firstUpBound The first upper bound.
	 * @param secondLowBound The second lower bound.
	 * @param secondUpBound The second upper bound.
	 * @return the clamped variable.
	 */
	public static double clamp(double var, double firstLowBound, 
											double firstUpBound,
											double secondLowBound, 
											double secondUpBound)
	{
		if(var < firstLowBound)
		{
			var = firstLowBound;
		}
		else if(var > firstUpBound)
		{
			var = firstUpBound;
		}
		else if(var < secondLowBound)
		{
			var = secondLowBound;
		}
		else if(var > secondUpBound)
		{
			var = secondUpBound;
		}
		return var;
	}
	
	/**
	 * Gets the direction between two points in degrees.
	 * 
	 * @param x1 The x-coordinate of the first point.
	 * @param y1 The y-coordinate of the first point.
	 * @param x2 The x-coordinate of the second point.
	 * @param y2 The y-coordinate of the second point.
	 * @return the direction between the two points.
	 */
	public static double getDirectionBetweenPoints(double x1, double y1, 
										double x2, double y2)
	{
		double dx = x2 - x1;
		double dy = y2 - y1;
		double direction = Math.atan2(dy, dx);
		return Math.toDegrees(direction);
	}
	
	/**
	 * Rounds a value to a certain amount of decimals.
	 * @param value The value to round.
	 * @param places The amount of decimals to keep.
	 * @return the rounded value.
	 * 
	 * Found on StackOverflow from user Jonik
	 */
	public static double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
