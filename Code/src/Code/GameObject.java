package Code;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents every object within a game, i.e. enemies, NPCs, etc.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class GameObject
{
	protected double x;
	protected double y;
	protected double prevX;
	protected double prevY;
	
	protected double hspd;
	protected double vspd;
	protected double width;
	protected double height;
	protected double speed;
	
	protected Sprite image;
	protected int subImageNumber;
	protected int subImageMax;
	protected double imageSpeed;
	protected double incrementImage;
	
	/**
	 * Initialize the object.
	 * @param x The object's x-coordinate.
	 * @param y The object's y-coordinate.
	 * @param image The object's sprite.
	 */
	public GameObject(double x, double y, String image, int subImages)
	{
		this.x = x;
		this.y = y;
		prevX = this.x;
		prevY = this.y;
		hspd = 0;
		vspd = 0;
		speed = 0;
		subImageNumber = 0;
		subImageMax = subImages;
		imageSpeed = 0;
		incrementImage = 0;
		
		this.image = new Sprite(image, subImages);
		width = this.image.getCellWidth();
		height = this.image.getCellHeight();
	}
	
	/**
	 * Updates the object. This method will be most likely be overridden
	 * in the subclasses of GameObject.
	 */
	public void update()
	{
		x += hspd;
		y += vspd;
	}
	
	/**
	 * Draws the object onto the screen.
	 * @param gc The object to draw with.
	 */
	public void render(GraphicsContext gc)
	{
		//Draw the image
		image.draw(gc, x, y, width, height);
		
		//Animate the image
		if(incrementImage >= 1)
		{
			subImageNumber = (subImageNumber + 1) % subImageMax;
			image.animate(subImageNumber);
			incrementImage--;
		}
		else
		{
			incrementImage += imageSpeed;
		}
	}
	
	/**
	 * Return the rectangular collision box of the object.
	 * @return the collision box of the object.
	 */
	public Rectangle2D getBounds()
	{
		return new Rectangle2D(x, y, width, height);
	}
	
	/**
	 * Checks if the object is intersecting with another object.
	 * 
	 * @param go The object to check if intersecting with.
	 * @return true if the objects are intersecting, false otherwise.
	 */
	public boolean collidesWith(GameObject go)
	{
		return go.getBounds().intersects(this.getBounds());
	}
	
	/**
	 * Checks if the object will collide with a wall at a given position.
	 * @param level The level to check in.
	 * @param x The x-coordinate of the position.
	 * @param y The y-coordinate of the position.
	 * @return true if there is a collision, false otherwise.
	 */
	public boolean wallCollision(int[][] level, double x, double y)
	{	
		//Remember object's position
		double objectXPos = this.x;
		double objectYPos = this.y;
		
		//Update position for bounding box calculations
		this.x = x;
		this.y = y;
		
		//Calculate bounding boxes and modify to fit level array
		int bBoxRight = (int)((this.x + this.width-1)/Game.CELL_WIDTH);
		int bBoxLeft = (int)(this.x/Game.CELL_WIDTH);
		int bBoxTop = (int)(this.y/Game.CELL_HEIGHT);
		int bBoxDown =(int)((this.y + this.height-1)/Game.CELL_HEIGHT);
		
		//Modify center positions to fit level array
		int centerCheckX = (int)(x/Game.CELL_WIDTH);
		int centerCheckY = (int)(y/Game.CELL_HEIGHT);
		
		//Check if borders of object collides with wall.
		boolean borderMeeting = 
				(level[bBoxRight][bBoxTop] != RandomLevelGenerator.FLOOR) ||
				(level[bBoxLeft][bBoxTop] != RandomLevelGenerator.FLOOR) ||
				(level[bBoxRight][bBoxDown] != RandomLevelGenerator.FLOOR) ||
				(level[bBoxLeft][bBoxDown] != RandomLevelGenerator.FLOOR);
		
		//Check if center of object collides with wall.
		boolean centerMeeting = level[centerCheckX][centerCheckY] != 
											RandomLevelGenerator.FLOOR;
		
		//Move back to starting position
		this.x = objectXPos;
		this.y = objectYPos;
		
		//Return whether the collision was true or not.
		return borderMeeting || centerMeeting;
	}
	
	/**
	 * Returns the object's x-coordinate.
	 * @return the object's x-coordinate.
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Returns the object's y-coordinate.
	 * @return the object's y-coordinate.
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Returns the object's hspd.
	 * @return the object's hspd.
	 */
	public double getHSpd()
	{
		return hspd;
	}
	
	/**
	 * Returns the object's vspd.
	 * @return the object's vspd.
	 */
	public double getVSpd()
	{
		return vspd;
	}
	
	/**
	 * Returns the object's previous x-coordinate.
	 * @return the object's previous x-coordinate.
	 */
	public double getPrevX()
	{
		return prevX;
	}
	
	/**
	 * Returns the object's previous y-coordinate.
	 * @return the object's previous y-coordinate.
	 */
	public double getPrevY()
	{
		return prevY;
	}
	
	/**
	 * Set the object's image to a new one.
	 * @param image The image to change to.
	 * @param subImages The number of sub-images for the sprite.
	 */
	public void setImage(String image, int subImages)
	{
		this.image = new Sprite(image, subImages);
		width = this.image.getCellWidth();
		height = this.image.getCellHeight();
	}
}
