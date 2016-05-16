package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snail extends Enemy
{
	private boolean right;
	private boolean left;

	/**
	 * Initialize the object.
	 *
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 */
	public Snail(double x, double y)
	{
		super(x, y, "Res/indaSnail.png", 2);
		hspd = 0.25;
		vspd = 0.25;
		speed = 0.25;
		imageSpeed = 0.05;
		damage = 3;
		left = false;
		right = true;
	}

	@Override
	public void update()
	{
		nextPosition();
	}

	private void nextPosition()
	{
		if(left)
		{
			if(wallCollision(Game.level, x + hspd, y))
			{
				hspd = -hspd;
				right = true;
				left = false;
				flippedRight = true;
				setEnemy();

			}
		} 
		else if(right)
		{
			if(wallCollision(Game.level, x + hspd, y))
			{
				hspd = -hspd;
				right = false;
				left = true;
				flippedRight = false;
				setEnemy();
			}

		}
		x += hspd;

	}

	private void setEnemy()
	{
		if(flippedRight)
		{
			setImage("Res/indaSnail.png", 2);
		} 
		else
		{
			setImage("Res/indaSnailFlipped.png", 2);
		}

	}

}
