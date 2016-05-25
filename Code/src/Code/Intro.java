package Code;

import javafx.animation.FadeTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

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
	private Sprite textBubble;
	private Alarm talkAlarm;
	private Alarm enterGameAlarm;

	private FadeTransition ft;

	private boolean finished;
	private boolean talking;
	private boolean showTalk;
	private boolean enteringGame;
	private boolean genderSet;

	/**
	 * Initializes the Intro.
	 */
	public Intro()
	{
		finished = false;
		genderSet = false;

		// The canvas to draw the intro on
		canvas = new Canvas(Game.ROOM_WIDTH, Game.ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		// Create the player object that will be in the intro
		player = new Player(-64, Game.ROOM_HEIGHT / 4 - 16);

		// Images used
		background = new Sprite("Res/indaBackgroundIntro.png", 1);
		textBubble = new Sprite("Res/indaTextBubble.png", 1);

		// Set up for the talking
		talkAlarm = new Alarm();
		talking = false;
		showTalk = true;

		// Set up for when the game's about to begin
		enterGameAlarm = new Alarm();
		enteringGame = false;

		// Fade transition
		ft = new FadeTransition(Duration.millis(3000), canvas);
		ft.setFromValue(255);
		ft.setToValue(0);
	}

	/**
	 * This method will update the intro so that it looks nice.
	 */
	public void update()
	{
		if(!genderSet)
		{
			genderSet = true;
			player.setPlayer(Game.playerGender);
		}
		
		// Count down the alarms
		talkAlarm.tick();
		enterGameAlarm.tick();

		// Move the player
		if(player.getX() < (3.5 * Game.ROOM_WIDTH / 5) / 2)
		{
			player.setX(player.getX() + 1);
			player.setHSpd(1);
		}
		else
		{
			// Start talking
			player.setHSpd(0);
			if(!talking)
			{
				talkAlarm.setTime(420);
			}
			talking = true;
		}

		// Move into the cave when talking is done
		if(talking && talkAlarm.done())
		{
			showTalk = false;
			if(player.getX() < (3.5 * Game.ROOM_WIDTH / 5) / 2 + 32)
			{
				player.setX(player.getX() + 0.5);
				player.setHSpd(1);
			}
			else
			{
				// Time to fade to the game
				if(!enteringGame)
				{
					enteringGame = true;
					enterGameAlarm.setTime(180);
					ft.play();
				}
			}

			// Start the game
			if(enteringGame && enterGameAlarm.done())
			{
				finished = true;
			}
		}
	}

	/**
	 * This method draws onto the canvas.
	 */
	public void render()
	{
		update();

		// Draw the background
		background.draw(gc, 0, 0, background.getCellWidth(),
				background.getCellHeight());

		// Draw the player
		player.render(gc);

		// Draw the text bubble
		if(talking && showTalk)
		{
			textBubble.draw(gc, 0, 0, textBubble.getCellWidth(),
					textBubble.getCellHeight());
		}
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
