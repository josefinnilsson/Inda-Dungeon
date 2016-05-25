package Code;

/**
 * This class represents an Enemy
 * Created by Josefin on 2016-05-04.
 */
public class Enemy extends LifeForm
{
	protected boolean flippedRight;

	/**
	 * Initialize the Enemy object.
	 *
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 * @param image The object's sprite.
	 * @param subImages
	 */
	public Enemy(double x, double y, String image, int subImages)
	{
		super(x, y, image, subImages);
		damage = 0;
		flippedRight = true;
		health = 100;
		speed = 1;
		imageSpeed = .2;
	}

}
