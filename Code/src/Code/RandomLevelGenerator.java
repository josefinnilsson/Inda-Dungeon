package Code;

import java.util.Random;

/**
 * This class generates a randomized 2D-level using an array that stores values
 * for walls, floors and void as integers, represented as constants within the
 * class.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class RandomLevelGenerator
{
	// Represents the various tiles in the level.
	public static final int FLOOR = 0;
	public static final int WALL = 1;
	public static final int VOID = 2;

	/**
	 * Generates a randomized 2D-level and stores it inside an array.
	 * @param sizeX The width of the level in pixels.
	 * @param sizeY The height of the level in pixels.
	 * @param cellWidth The width of each cell in the level (tile size).
	 * @param cellHeight The height of each cell in the level (tile size).
	 * @return the level generated.
	 */
	public static int[][] generateLevel(int sizeX, int sizeY, 
										int cellWidth, int cellHeight)
	{
		int columns = sizeX / cellWidth;
		int rows = sizeY / cellHeight;
		int[][] level = new int[columns][rows];
		Random r = new Random();

		// The generator will attempt to put an amount of floor tiles equal
		// to the size of the room into the level.
		int floorAmount = (columns * rows);
		int floorMakerX = r.nextInt(columns - 2) + 2;
		int floorMakerY = r.nextInt(rows - 2) + 2;

		// Start off by creating a void.
		for(int x = 0; x < columns; x++)
		{
			for(int y = 0; y < rows; y++)
			{
				level[x][y] = VOID;
			}
		}

		// Add flooring
		int dir = 0;
		for(int i = 0; i < floorAmount; i++)
		{
			level[floorMakerX][floorMakerY] = FLOOR;

			if(r.nextBoolean())
			{
				dir = r.nextInt(4);
			}

			// Make sure floor generator only moves in one direction
			int dx = (int) MathMethods.lengthDirX(1, dir * 90);
			int dy = (int) MathMethods.lengthDirY(1, dir * 90);
			floorMakerX += dx;
			floorMakerY += dy;

			// Make sure floor generator stays in the room.
			floorMakerX = (int) MathMethods.clamp(floorMakerX, 2, columns - 3);
			floorMakerY = (int) MathMethods.clamp(floorMakerY, 2, rows - 3);
		}

		// Add walls
		for(int x = 2; x < columns - 2; x++)
		{
			for(int y = 2; y < rows - 2; y++)
			{
				if(level[x][y] == FLOOR)
				{
					if(level[x + 1][y] != FLOOR)
						level[x + 1][y] = WALL;
					if(level[x - 1][y] != FLOOR)
						level[x - 1][y] = WALL;
					if(level[x][y + 1] != FLOOR)
						level[x][y + 1] = WALL;
					if(level[x][y - 1] != FLOOR)
						level[x][y - 1] = WALL;
				}
			}
		}

		// Remove single wall objects
		for(int x = 2; x < columns - 2; x++)
		{
			for(int y = 2; y < rows - 2; y++)
			{
				if(level[x][y] == WALL && 
					level[x + 1][y] == FLOOR && 
					level[x - 1][y] == FLOOR && 
					level[x][y - 1] == FLOOR && 
					level[x][y + 1] == FLOOR)
				{
					level[x][y] = FLOOR;
				}
			}
		}

		return level;
	}

	/**
	 * Generates a fixed 2D-level for a boss fight and stores it inside an
	 * array.
	 * @param sizeX The width of the level in pixels.
	 * @param sizeY The height of the level in pixels.
	 * @param cellWidth The width of each cell in the level (tile size).
	 * @param cellHeight The height of each cell in the level (tile size).
	 * @return the level generated.
	 */
	public static int[][] generateBossLevel(int sizeX, int sizeY, 
											int cellWidth, int cellHeight)
	{
		int columns = sizeX / cellWidth;
		int rows = sizeY / cellHeight;
		int[][] level = new int[columns][rows];

		// Start off by creating a void.
		for(int x = 0; x < columns; x++)
		{
			for(int y = 0; y < rows; y++)
			{
				level[x][y] = VOID;
			}
		}

		// Add flooring
		for(int x = columns / 3; x < 2 * columns / 3; x++)
		{
			for(int y = rows / 3; y < 2 * rows / 3; y++)
			{
				level[x][y] = FLOOR;
			}
			if(Math.abs(x - columns / 2 + 1) < 3)
			{
				for(int y = 2 * rows / 3; y < rows - 2; y++)
				{
					level[x][y] = FLOOR;
				}
			}
		}

		// Add walls
		for(int x = 2; x < columns - 2; x++)
		{
			for(int y = 2; y < rows - 2; y++)
			{
				if(level[x][y] == FLOOR)
				{
					if(level[x + 1][y] != FLOOR)
						level[x + 1][y] = WALL;
					if(level[x - 1][y] != FLOOR)
						level[x - 1][y] = WALL;
					if(level[x][y + 1] != FLOOR)
						level[x][y + 1] = WALL;
					if(level[x][y - 1] != FLOOR)
						level[x][y - 1] = WALL;
				}
			}
		}

		// Remove single wall objects
		for(int x = 2; x < columns - 2; x++)
		{
			for(int y = 2; y < rows - 2; y++)
			{
				if(level[x][y] == WALL && 
					level[x + 1][y] == FLOOR && 
					level[x - 1][y] == FLOOR && 
					level[x][y - 1] == FLOOR && 
					level[x][y + 1] == FLOOR)
				{
					level[x][y] = FLOOR;
				}
			}
		}

		return level;
	}
}
