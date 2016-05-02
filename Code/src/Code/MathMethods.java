package Code;

/**
 * This class contains a handful of useful mathematical methods for 
 * calculating various things, such as positions or directions.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class MathMethods
{
	/**
	 * Calculates the cathetus in the x-direction from a hypotenuse and a 
	 * direction.
	 * @param length The length of the hypotenuse.
	 * @param direction The angle (based on unit circle) in degrees.
	 * @return The cathetus in the x-direction.
	 */
	public static double lengthDirX(double length, double direction)
	{
		if(length >= 0)
			return Math.cos(Math.toRadians(direction))*length;
		else
			throw new ArithmeticException("Length must be larger than 0");
	}
	
	/**
	 * Calculates the cathetus in the y-direction from a hypotenuse and a 
	 * direction.
	 * @param length The length of the hypotenuse.
	 * @param direction The angle (based on unit circle) in degrees.
	 * @return The cathetus in the y-direction.
	 */
	public static double lengthDirY(double length, double direction)
	{
		if(length >= 0)
			return Math.sin(Math.toRadians(direction))*length;
		else
			throw new ArithmeticException("Length must be larger than 0");
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
	 * Gets the direction, in degrees, between two points.
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
}
