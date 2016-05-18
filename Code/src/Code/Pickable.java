package Code;

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
	}

	@Override
	public void update()
	{
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
