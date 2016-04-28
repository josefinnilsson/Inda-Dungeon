package game_tutorial;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents an animatable image.
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
		
		cells = new Rectangle2D[numCells];
		for(int i = 0; i < numCells; i++)
		{
			cells[i] = new Rectangle2D(
							i*cellWidth, 0, 
							cellWidth, cellHeight);
		}
		numCell = 0;
	}
	
	/**
	 * Animates into the given cell in the sprite sheet.
	 * @param cell The cell to animate to.
	 */
	public void animate(int cell)
	{
		numCell = cell;
	}
	
	/**
	 * Draw the current frame using a graphics object.
	 * @param gc The graphics object to use.
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
