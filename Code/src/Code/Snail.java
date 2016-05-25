package Code;

/**
 * This class represents a snail object. It controls the movement of the snail.
 * Created by Josefin on 2016-05-04.
 */
public class Snail extends Enemy
{
	private boolean right;
	private boolean left;

	/**
	 * Initialize the snail object.
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

	/**
	 * This method decides the next position for the snail. The snail can only
	 * move horizontally but changes direction if it collides with a wall.
	 */
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
