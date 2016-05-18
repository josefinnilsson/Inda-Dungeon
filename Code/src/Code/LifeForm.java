package Code;

import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents a living object within the room.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class LifeForm extends GameObject
{
	protected double health;
	protected double maxHealth;
	protected int damage;
	
	private Alarm hitAlarm;

	/**
	 * Initializes the life form.
	 * @param x The x-coordinate of the life form.
	 * @param y The y-coordinate of the life form.
	 * @param image The life form's sprite.
	 * @param subImages The amount of sub-images in the life form.
	 */
	public LifeForm(double x, double y, String image, int subImages)
	{
		super(x, y, image, subImages);
		health = 100;
		maxHealth = 100;
		hitAlarm = new Alarm();
	}

	/**
	 * Returns the damage this object deals.
	 * @return the life form's damage.
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * Returns the health of this object.
	 * @return the life form's health.
	 */
	public double getHealth()
	{
		return health;
	}

	/**
	 * Returns the maximum health of the object.
	 * @return the life form's maximum health.
	 */
	public double getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * Returns whether the object is dead or not.
	 * @return true if the object is dead, false otherwise.
	 */
	public boolean isDead()
	{
		return health <= 0;
	}

	/**
	 * Hits the life form, reducing its health by the given damage.
	 * @param damage The amount of damage to take.
	 */
	public void hit(int damage)
	{
		health -= damage;
		if(health < 0)
		{
			health = 0;
		}
		hitAlarm.setTime(30);
	}
	
	/**
	 * Heals the life form, increasing its health by the given hp.
	 * @param hp The amount of health to heal.
	 */
	public void heal(double hp)
	{
		health += hp;
		if(health > maxHealth)
		{
			health = maxHealth;
		}
	}

	@Override
	public void render(GraphicsContext gc)
	{	
		// Draw the image
		if(hitAlarm.tick() % 2 == 0)
		{
			image.draw(gc, x, y, width, height);
		}

		animate();
	}

	/**
	 * Animates the life form.
	 */
	public void animate()
	{
		// Only animate if moving
		if(Math.abs(hspd) > 0 || Math.abs(vspd) > 0)
		{
			if(incrementImage >= 1)
			{
				imageIndex = (imageIndex + 1) % imageNumber;
				image.animate(imageIndex);
				incrementImage--;
			} 
			else
			{
				incrementImage += imageSpeed;
			}
		} 
		else
		{
			image.animate(imageIndex);
			imageIndex = 0;
		}
	}

	/**
	 * Moves the player or enemy according to walls and speed.
	 */
	public void move()
	{
		prevX = x;
		prevY = y;
		// Check for horizontal collision
		if(wallCollision(Game.level, x + hspd, y))
		{
			while(!wallCollision(Game.level, x + Math.signum(hspd), y))
			{
				x += Math.signum(hspd);
			}
			hspd = 0;
		}

		// Move horizontally
		x += hspd;

		// Check for vertical collision
		if(wallCollision(Game.level, x, y + vspd))
		{
			while(!wallCollision(Game.level, x, y + Math.signum(vspd)))
			{
				y += Math.signum(vspd);
			}
			vspd = 0;
		}

		// Move vertically
		y += vspd;
	}
}
