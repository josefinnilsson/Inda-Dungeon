package Code;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents an image, but with included methods for animating
 * between frames within the image. The image must be created in such a way that
 * every frame of the animation is added in a strictly horizontal grid, or else
 * the animation will fail.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Sprite extends Image
{
	private final Rectangle2D[] cells;
	private int numCell;

	private double cellWidth;
	private double cellHeight;

	/**
	 * Initialize the sprite and divide it into its sub-images.
	 * @param image The sprite sheet.
	 * @param numCells The number of sub-images.
	 */
	public Sprite(String image, int numCells)
	{
		super(image);
		cellWidth = getWidth() / numCells;
		cellHeight = getHeight();

		// Create rectangles with coordinates for each sub-image.
		cells = new Rectangle2D[numCells];
		for(int i = 0; i < numCells; i++)
		{
			cells[i] = new Rectangle2D(i * cellWidth, 0, cellWidth, cellHeight);
		}
		numCell = 0;
	}

	/**
	 * Set which sub-image should be drawn.
	 * @param cell The sub-image to animate to.
	 */
	public void animate(int cell)
	{
		numCell = cell;
	}

	/**
	 * Draw the current frame of the sprite using a graphics object.
	 * @param gc The graphics object to use.
	 * @param x The x-coordinate to draw it at.
	 * @param y The y-coordinate to draw it at.
	 * @param width The width of the frame.
	 * @param height the height of the frame.
	 */
	public void draw(GraphicsContext gc, double x, double y, 
											double width, double height)
	{
		gc.drawImage(this, 
				cells[numCell].getMinX(), 
				cells[numCell].getMinY(), 
				cells[numCell].getWidth(),
				cells[numCell].getHeight(), 
				x, y, width, height);
	}

	/**
	 * Return the width of a sub-image.
	 * @return the width of a sub-image.
	 */
	public double getCellWidth()
	{
		return cellWidth;
	}

	/**
	 * Return the height of a sub-image.
	 * @return the height of a sub-image.
	 */
	public double getCellHeight()
	{
		return cellHeight;
	}
}
