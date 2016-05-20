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
	
	/**
	 * Initializes the Intro. 
	 */
	public Intro()
	{
		//The canvas to draw the intro on
		canvas = new Canvas(Game.ROOM_WIDTH, Game.ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();
		
		//Should be the same size as the rest of the game
		gc.scale(Game.SCALE_X, Game.SCALE_Y);
		
		//Create the player object that will be in the intro
		player = new Player(-32, Game.ROOM_HEIGHT/2);
		player.setPlayer(true); //Temporary, should be replaced with what
								//The user chooses
		
		background = new Sprite("Res/indaBackgroundIntro.png", 1);
	}
	
	/**
	 * This method will update the intro so that it looks nice.
	 */
	public void update()
	{
		
	}
	
	/**
	 * This method draws onto the canvas.
	 */
	public void render()
	{
		
		player.render(gc);
	}
}
