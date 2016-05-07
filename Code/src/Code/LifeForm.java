package Code;

/**
 * This class represents a living object within the room.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class LifeForm extends GameObject
{
    protected int health;
    protected int damage;
	
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
        if (health < 0) 
        {
            health = 0;
        }
    }
}
