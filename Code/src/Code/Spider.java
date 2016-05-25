package Code;

import java.util.Random;

/**
 * This class represents a spider object. It controls the movement of the spider.
 * Created by Josefin on 2016-05-04.
 */
public class Spider extends Enemy
{
	private double direction;
	private boolean close;
	Random random;
	Alarm alarm;
	Alarm shootTimer;

	/**
	 * Initialize the spider object.
	 *
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 */
	public Spider(double x, double y)
	{
		super(x, y, "Res/indaSpider.png", 2);
		hspd = 0.4;
		vspd = 0.4;
		speed = 0.4;
		imageSpeed = 0.05;
		damage = 1;
		alarm = new Alarm(50);
		shootTimer = new Alarm(200);
		random = new Random();
		close = false;
	}

	public void update()
	{
		nextPosition();
	}

	/**
	 * This method decides the next position for the spider. It moves at random
	 * and checks if the player is close enough for attack.
	 */
	private void nextPosition()
	{
		double playerX = Game.player.getX();
		double playerY = Game.player.getY();
		double diffX = playerX - x;
		double diffY = playerY - y;
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
			attackPlayer();
		} 
		else if(alarm.done())
		{
			direction = Math.abs(random.nextInt() % 360) + 1;
			alarm.setTime(50);
		}
		hspd = MathMethods.lengthDirX(speed, direction);
		vspd = MathMethods.lengthDirY(speed, direction);
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
		move();
		alarm.tick();
		shootTimer.tick();
	}

	/**
	 * Fires a spiderweb towards the player if the shoot timer is done.
	 */
	private void attackPlayer()
	{
		if(shootTimer.done())
		{
			Projectile spiderWeb = new SpiderWeb(x, y);
			Game.objectWaitingRoom.add(spiderWeb);
			spiderWeb.shoot();
			shootTimer.setTime(200);
		}
	}

	private void setEnemy()
	{
		if(flippedRight)
		{
			setImage("Res/indaSpider.png", 2);
		} 
		else
		{
			setImage("Res/indaSpiderFlipped.png", 2);
		}
	}

}
