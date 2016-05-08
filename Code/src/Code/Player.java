package Code;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * This class represents the player object of the game. It handles input and
 * controls the actions of the player.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Player extends LifeForm
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
	private boolean leftMouse;
	private boolean rightMouse;
	
	private boolean flippedRight;
	private boolean dashing;
	private boolean dashable;

	private boolean malePlayer;
	
	private Alarm dashAlarm;
	
	private double stamina;
	
	/**
	 * Initialize the player object.
	 * @param x The player's x-coordinate.
	 * @param y The player's y-coordinate.
	 * @param image The player's sprite.
	 */
	public Player(double x, double y)
	{
		super(x, y, "Res/Indo.png", 8);
		getInput();
		xAxis = 0;
		yAxis = 0;
		speed = 1;
		imageSpeed = .2;
		
		flippedRight = true;
		dashing = false;
		dashable = true;
		state = State.move;
		dashAlarm = new Alarm();
		
		malePlayer = true;
		health = 100;
		stamina = 100;

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
		//Count down alarms
		dashAlarm.tick();
		
		//Choose what to do depending on which state the player is in.
		switch(state)
		{
			case move:
				//Check if time to dash
				if(rightMouse && dashable)
				{
					state = State.dash;
					dashAlarm.setTime(10);
				}
				
				//Control movement
				setSpeed();
				move();
				
				//Switch sprite's direction depending on speed
				setSpriteDirection();
				break;
			case dash:
				dash();
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
		leftMouse = Input.mousePressed(MouseButton.PRIMARY);
		rightMouse = Input.mousePressed(MouseButton.SECONDARY);
		
		//Check if it's possible to dash
		if(!rightMouse) dashable = true;
	}
	
	/**
	 * Sets the xAxis and yAxis relative to the mouse coordinates.
	 */
	private void setAxesToMouse()
	{
		double mX = Input.mouseX;
		double mY = Input.mouseY;
		double direction = MathMethods.getDirectionBetweenPoints(x, y, mX, mY);
		
		if(direction > -67.5 && direction <= 67.5)
		{
			xAxis = 1;
		}
		else if(direction < -112.5 || direction >= 112.5)
		{
			xAxis = -1;
		}
		else
		{
			xAxis = 0;
		}
		
		if(direction > -157.5 && direction <= -22.5)
		{
			yAxis = -1;
		}
		else if(direction > 22.5 && direction <= 157.5)
		{
			yAxis = 1;
		}
		else
		{
			yAxis = 0;
		}
	}
	
	/**
	 * Set the direction of the player depending on speed.
	 */
	private void setSpriteDirection()
	{
		if(hspd > 0 && !flippedRight)
		{
			if(malePlayer)
			{
				setImage("Res/Indo.png", 8);
			}
			else
			{
				setImage("Res/Inda.png", 8);
			}
			flippedRight = true;
		}
		else if(hspd < 0 && flippedRight)
		{
			if(malePlayer)
			{
				setImage("Res/IndoFlipped.png", 8);
			}
			else
			{
				setImage("Res/IndaFlipped.png", 8);
			}
			flippedRight = false;
		}
	}
	
	/**
	 * Makes the player dash in a certain direction.
	 */
	private void dash()
	{
		dashable = false;
		if(!dashing)
		{
			//Get direction to dash in
			setAxesToMouse();
			dashing = true;
			
			//Set speed
			hspd = xAxis * speed*4;
			vspd = yAxis * speed*4;
			
			//Switch sprite's direction depending on speed
			setSpriteDirection();
		}
		else
		{
			if(dashAlarm.done())
			{
				state = State.move;
				dashing = false;
			}
		}
		move();
	}
	
	/**
	 * Sets the player to either male or female, depending on the parameter.
	 * @param type What kind of player to play as, where true means male and
	 * false means female.
	 */
	public void setPlayer(boolean type)
	{
		malePlayer = type;
		if(malePlayer)
		{
			if(flippedRight)
			{
				setImage("Res/Indo.png", 8);
			}
			else
			{
				setImage("Res/IndoFlipped.png", 8);
			}
		}
		else
		{
			if(flippedRight)
			{
				setImage("Res/Inda.png", 8);
			}
			else
			{
				setImage("Res/IndaFlipped.png", 8);
			}
		}
	}

	/**
	 * Returns the player's stamina.
	 * @return the stamina of the player.
	 */
	public double getStamina()
	{
		return stamina;
	}
}
