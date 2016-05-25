package Code;

/**
 * This class represents a projectile that an enemy (a spider or the boss)
 * can shoot at the player.
 * Created by Josefin on 2016-05-12.
 */
public class Projectile extends GameObject
{
	protected int damage;
	protected boolean remove;
	protected double direction;
	protected boolean collided;

	/**
	 * Initialize the projectile object.
	 *
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 */
	public Projectile(double x, double y, String image, int subImages)
	{
		super(x, y, image, subImages);
		hspd = 0.5;
		vspd = 0.5;
		speed = 1;
		damage = 10;
		remove = false;
		collided = false;
	}

	/**
	 * Update the projectiles posititon
	 */
	public void update()
	{
		x += hspd;
		y += vspd;
		checkCollision();
	}

	public int getDamage()
	{
		return damage;
	}

	/**
	 * Checks if the projectile collides with a wall.
	 */
	public void checkCollision()
	{
		if(wallCollision(Game.level, x + hspd, y))
		{
			remove = true;
		}
		if(wallCollision(Game.level, x, y + vspd))
		{
			remove = true;
		}
		if(x < 0 || x > Game.ROOM_WIDTH)
		{
			remove = true;
		}
		if(y < 0 || y > Game.ROOM_HEIGHT)
		{
			remove = true;
		}
	}

	/**
	 * Returns whether the projectile has collided with something or not.
	 * @return true if it has collided, false otherwise.
	 */
	public boolean getCollided()
	{
		return collided;
	}

	public boolean shouldRemove()
	{
		return remove;
	}

	public void setRemove()
	{
		remove = true;
	}

	/**
	 * Shoots the projectile towards the players current direction.
	 */
	public void shoot()
	{
		double pX = Game.player.getX();
		double pY = Game.player.getY();
		direction = MathMethods.getDirectionBetweenPoints(x, y, pX, pY);
		if(!remove)
		{
			hspd = MathMethods.lengthDirX(speed, direction);
			vspd = MathMethods.lengthDirY(speed, direction);
		}
	}

	public void shoot(double hspd, double vspd)
	{
		if(!remove)
		{
			direction = MathMethods.getDirectionBetweenPoints(0, 0, hspd, vspd);
			this.hspd = hspd;
			this.vspd = vspd;
		}
	}
}
