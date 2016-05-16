package Code;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents a tile set, with different tiles all added to one
 * image. This class is useful for reducing memory costs.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class TileSet extends Image
{
	private final Rectangle2D[][] cells;

	private double cellWidth;
	private double cellHeight;

	/**
	 * Initialize the tile set and divide it into its tiles.
	 * @param image The tile set image.
	 * @param numHCells The number of tiles horizontally.
	 * @param numVCells The number of tiles vertically
	 */
	public TileSet(String image, int numHCells, int numVCells)
	{
		super(image);
		cellWidth = getWidth() / numHCells;
		cellHeight = getHeight() / numVCells;

		// Create rectangles with coordinates for each sub-image.
		cells = new Rectangle2D[numHCells][numVCells];
		for(int i = 0; i < numHCells; i++)
		{
			for(int j = 0; j < numVCells; j++)
			{
				cells[i][j] = new Rectangle2D(i * cellWidth, j * cellHeight, 
												cellWidth, cellHeight);
			}
		}
	}

	/**
	 * Draw the given tile in the tile set.
	 * @param gc The graphics object to use.
	 * @param x The x-coordinate to draw it at.
	 * @param y The y-coordinate to draw it at.
	 * @param width The width of the frame.
	 * @param height The height of the frame.
	 * @param tileX Which tile horizontally to draw.
	 * @param tileY Which tile vertically to draw.
	 */
	public void draw(GraphicsContext gc, double x, double y, 
						double width, double height, int tileX, int tileY)
	{
		gc.drawImage(this, 
				cells[tileX][tileY].getMinX(), 
				cells[tileX][tileY].getMinY(), 
				cells[tileX][tileY].getWidth(),
				cells[tileX][tileY].getHeight(), 
				x, y, width, height);
	}

	/**
	 * Return the width of a tile.
	 * @return the width of a tile.
	 */
	public double getCellWidth()
	{
		return cellWidth;
	}

	/**
	 * Return the height of a tile.
	 * @return the height of a tile.
	 */
	public double getCellHeight()
	{
		return cellHeight;
	}
}
