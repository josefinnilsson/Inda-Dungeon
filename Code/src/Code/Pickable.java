package Code;

import java.util.Random;

/**
 * This class represents objects within the game that can be picked up by the
 * Player.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Pickable extends GameObject
{
	private boolean isPicked;
	private Random r;
	private Alarm moveAlarm;

	/**
	 * Initializes the pickable object.
	 * @param x The x-coordinate for the object.
	 * @param y The y-coordinate for the object.
	 * @param image The image of the object.
	 * @param subImages The amount of sub-images in its animation.
	 */
	public Pickable(double x, double y, String image, int subImages)
	{
		super(x, y, image, subImages);
		isPicked = false;
		r = new Random();
		
		hspd = r.nextInt(3) - 1;
		vspd = r.nextInt(3) - 1;
		moveAlarm = new Alarm(15);
	}

	@Override
	public void update()
	{
		moveAlarm.tick();
		
		//Move the object according to its speed.
		if(!moveAlarm.done())
		{
			move();
			
			//Apply friction
			hspd -= 0.01 * Math.signum(hspd);
			vspd -= 0.01 * Math.signum(hspd);
		}
		
		
		if(collidesWith(Game.player))
		{
			isPicked = true;
		}
	}

	/**
	 * Returns whether or not this object has been picked up by the player.
	 * @return true if it has been picked, false otherwise.
	 */
	public boolean isPicked()
	{
		return isPicked;
	}
}
