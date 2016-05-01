package Code;

/**
 * This class represents an alarm that will count down to 0.
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Alarm
{
	private int time;
	
	/**
	 * Initializes the alarm without any starting value.
	 */
	public Alarm()
	{
		time = 0;
	}
	
	/**
	 * Initializes the alarm with a starting value.
	 * @param time The starting value.
	 */
	public Alarm(int time)
	{
		this.time = time;
	}
	
	/**
	 * Sets the alarm to the given time.
	 * @param time The given time.
	 */
	public void setTime(int time)
	{
		this.time = time;
	}
	
	/**
	 * Ticks the alarm clock once, simulating the countdown.
	 */
	public void tick()
	{
		if(time > 0)
		{
			time--;
		}
	}
	
	/**
	 * Return whether the alarm has finished ticking.
	 * @return true if alarm is done, false otherwise.
	 */
	public boolean done()
	{
		return time == 0;
	}
}
