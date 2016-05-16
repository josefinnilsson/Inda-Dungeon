package Code;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents a damage object that will damage everything but it's
 * creator and then disappear. This will help in making sure objects don't take
 * too much damage because of too many frames before the player can react.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Damage extends GameObject
{
	private GameObject creator;
	private int damage;
	private boolean dealtDamage;

	/**
	 * Creates the damage object at a given location.
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 * @param creator The creator of the object.
	 * @param damage The damage this object will deal.
	 */
	public Damage(double x, double y, GameObject creator, int damage)
	{
		super(x, y, "Res/indadamage.png", 1);
		this.creator = creator;
		this.damage = damage;
		dealtDamage = false;
	}

	@Override
	public void render(GraphicsContext gc)
	{
		// Do nothing, this object should not be rendered.
	}

	@Override
	public Rectangle2D getBounds()
	{
		return new Rectangle2D(x, y, width, height);
	}

	@Override
	public void update()
	{
		// Only damage once
		if(!dealtDamage)
		{
			// Loop through every object that's a life form
			for(GameObject object : Game.objects)
			{
				if(!object.equals(creator) && object instanceof LifeForm)
				{
					if(collidesWith(object))
					{
						((LifeForm) object).hit(damage);
					}
				}
			}
			dealtDamage = true;
		}
	}

	/**
	 * Returns whether this object has dealt damage yet.
	 * @return true if it has dealt damage, false otherwise.
	 */
	public boolean hasDamaged()
	{
		return dealtDamage;
	}
}
