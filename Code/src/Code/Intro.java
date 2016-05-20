package Code;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents the Intro of the game. it will control what to render
 * to the screen
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Intro
{
	private Canvas canvas;
	private GraphicsContext gc;
	private Player player;
	private Sprite background;
	
	private boolean finished;

	/**
	 * Initializes the Intro.
	 */
	public Intro()
	{
		finished = false;
		
		// The canvas to draw the intro on
		canvas = new Canvas(Game.ROOM_WIDTH, Game.ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		// Create the player object that will be in the intro
		player = new Player(-64, Game.ROOM_HEIGHT/4-16);
		player.setPlayer(true); // Temporary, should be replaced with what
								// The user chooses

		background = new Sprite("Res/indaBackgroundIntro.png", 1);
	}

	/**
	 * This method will update the intro so that it looks nice.
	 */
	public void update()
	{
		if(player.getX() < (3.5*Game.ROOM_WIDTH/5)/2) 
		{
			player.setX(player.getX()+1);
			player.setHSpd(1);
		}
		else
		{
			player.setHSpd(0);
		}
	}

	/**
	 * This method draws onto the canvas.
	 */
	public void render()
	{
		update();
		
		background.draw(gc, 0, 0, background.getCellWidth(),
									background.getCellHeight());
		player.render(gc);
	}
	
	/**
	 * Returns the canvas of this intro.
	 * @return the canvas of this intro.
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	/**
	 * Return whether the intro is finished or not.
	 * @return true if it is, false otherwise.
	 */
	public boolean finished()
	{
		return finished;
	}
}
