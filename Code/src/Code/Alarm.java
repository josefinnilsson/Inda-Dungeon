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
	 * @return the time left on the alarm.
	 */
	public int tick()
	{
		if(time > 0)
		{
			time--;
		}
		return time;
	}
	
	/**
	 * Return whether the alarm has finished ticking.
	 * @return true if alarm is done, false otherwise.
	 */
	public boolean done()
	{
		return time <= 0;
	}
	
	/**
	 * Return the current time for the alarm.
	 * @return what index the alarm is currently at.
	 */
	public int currentTime()
	{
		return time;
	}
}
