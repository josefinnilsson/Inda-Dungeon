package Code;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * This class represents the player object of the game. It handles input and
 * controls the actions of the player.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Player extends GameObject
{
	//These states control in which way the player object updates, and are
	//switched between depending on input and in-game events.
	private enum State
	{
		move,
		dash,
		attack,
		shoot,
		hurt,
		dead
	}
	
	private int xAxis;
	private int yAxis;
	private State state;
	
	//Holds different values depending on input.
	private int rightKey;
	private int leftKey;
	private int upKey;
	private int downKey;
	private int leftMouse;
	private int rightMouse;
	
	private boolean flippedRight;
	
	/**
	 * Initialize the player object.
	 * @param x The player's x-coordinate.
	 * @param y The player's y-coordinate.
	 * @param image The player's sprite.
	 */
	public Player(double x, double y, String image, int subImages)
	{
		super(x, y, image, subImages);
		getInput();
		xAxis = 0;
		yAxis = 0;
		speed = 1;
		imageSpeed = .2;
		flippedRight = true;
		state = State.move;
	}
	
	/**
	 * Set the player's horizontal and vertical speed depending on the
	 * input. 
	 */
	public void setSpeed()
	{
		//Creates a "unit circle" with 8 different directions
		getInput();
		xAxis = rightKey - leftKey;
		yAxis = downKey - upKey;
		
		//Set the speed
		hspd = xAxis * speed;
		vspd = yAxis * speed;
	}
	
	/* (non-Javadoc)
	 * @see game_tutorial.GameObject#update()
	 * 
	 * Sets the speed and moves the player accordingly.
	 */
	@Override
	public void update()
	{
		//Choose what to do depending on which state the player is in.
		switch(state)
		{
			case move:
				setSpeed();
				move();
				
				//Switch sprite's direction
				if(hspd > 0 && !flippedRight)
				{
					setImage("Res/Indo.png", 8);
					flippedRight = true;
				}
				else if(hspd < 0 && flippedRight)
				{
					setImage("Res/IndoFlipped.png", 8);
					flippedRight = false;
				}
				break;
			case dash:
				break;
			case attack:
				break;
			case shoot:
				break;
			case dead:
				break;
			case hurt:
				break;
			default:
				break;
		}
	}
	
	@Override
	public void render(GraphicsContext gc)
	{
		//Draw the image
		image.draw(gc, x, y, width, height);
		
		//Only animate if moving
		if(Math.abs(hspd) > 0 || Math.abs(vspd) > 0)
		{
			if(incrementImage >= 1)
			{
				imageIndex = (imageIndex + 1) % imageNumber;
				image.animate(imageIndex);
				incrementImage--;
			}
			else
			{
				incrementImage += imageSpeed;
			}
		}
		else
		{
			image.animate(imageIndex);
			imageIndex = 0;
		}
	}
	
	/**
	 * Moves the player according to walls and speed.
	 */
	private void move()
	{
		prevX = x;
		prevY = y;
		//Check for horizontal collision
		if(wallCollision(Game.level, x+hspd, y))
		{
			while(!wallCollision(Game.level, x+Math.signum(hspd), y))
			{
				x += Math.signum(hspd);
			}
			hspd = 0;
		}
		
		//Move horizontally
		x += hspd;
		
		//Check for vertical collision
		if(wallCollision(Game.level, x, y+vspd))
		{
			while(!wallCollision(Game.level, x, y+Math.signum(vspd)))
			{
				y += Math.signum(vspd);
			}
			vspd = 0;
		}
		
		//Move vertically
		y += vspd;
	}
	
	/**
	 * Returns whether different mouse buttons or keys are currently being
	 * pressed.
	 */
	private void getInput()
	{
		//Keys
		rightKey = (Input.keyPressed(KeyCode.D) || 
				Input.keyPressed(KeyCode.RIGHT)) ? 1 : 0;
		leftKey = (Input.keyPressed(KeyCode.A) || 
						Input.keyPressed(KeyCode.LEFT)) ? 1 : 0;
		upKey = (Input.keyPressed(KeyCode.W) || 
						Input.keyPressed(KeyCode.UP)) ? 1 : 0;
		downKey = (Input.keyPressed(KeyCode.S) || 
						Input.keyPressed(KeyCode.DOWN)) ? 1 : 0;
		
		//Mouse buttons
		leftMouse = (Input.mousePressed(MouseButton.PRIMARY)) ? 1 : 0;
		rightMouse = (Input.mousePressed(MouseButton.SECONDARY)) ? 1 : 0;
	}
}
