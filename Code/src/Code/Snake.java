package Code;

import java.util.Random;

/**
 * This class represents an snake object. It controls the movement of the snake.
 * Created by Josefin on 2016-05-04.
 */
public class Snake extends Enemy
{
	Random random;
	Alarm alarm;
	private double direction;
	private boolean close;

	/**
	 * Initialize the snake object.
	 *
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 */
	public Snake(double x, double y)
	{
		super(x, y, "Res/indaSnake.png", 2);
		speed = 0.4;
		imageSpeed = 0.05;
		damage = 1;
		alarm = new Alarm(200);
		random = new Random();
		damage = 3;
		hspd = 0.4;
		vspd = 0.4;
		close = false;
	}

	public void update()
	{
		nextPosition();
	}

	/**
	 * This method decides the next position for the snake. If the snake
	 * is close enough to the player, it follows the player and if it isn't, it
	 * moves randomly.
	 */
	private void nextPosition()
	{
		double playerX = Game.player.getX() + Game.player.getWidth() / 2;
		double playerY = Game.player.getY() + 3 * Game.player.getHeight() / 4;
		double diffX = playerX - (x + width / 2);
		double diffY = playerY - (y + 3 * height / 4);
		if(Math.abs(diffX) < 64 && Math.abs(diffY) < 64)
		{
			close = true;
		} 
		else
		{
			close = false;
		}
		if(close)
		{
			direction = MathMethods.getDirectionBetweenPoints(0, 0, 
															diffX, diffY);
		} 
		else if(alarm.done())
		{
			direction = Math.abs(random.nextInt() % 360) + 1;
			alarm.setTime(200);

		}
		hspd = MathMethods.lengthDirX(speed, direction);
		vspd = MathMethods.lengthDirY(speed, direction);
		if(Math.abs(diffX) > 1 || Math.abs(diffY) > 1)
		{
			if(hspd > 0 && !flippedRight)
			{
				flippedRight = true;
				setEnemy();
			} 
			else if(hspd < 0 && flippedRight)
			{
				flippedRight = false;
				setEnemy();
			}
		}
		move();
		alarm.tick();
	}

	/**
	 * Sets the snake to either left or right depending on direction. 
	 */
	private void setEnemy()
	{
		if(flippedRight)
		{
			setImage("Res/indaSnake.png", 2);
		} 
		else
		{
			setImage("Res/indaSnakeFlipped.png", 2);
		}

	}
}
