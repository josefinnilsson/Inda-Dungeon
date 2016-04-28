package Code;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class Player extends GameObject
{
	private int xAxis;
	private int yAxis;
	
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
		xAxis = 0;
		yAxis = 0;
		speed = 1;
		imageSpeed = .2;
		flippedRight = false;
	}
	
	/**
	 * Set the player's horizontal and vertical speed depending on the
	 * input. 
	 */
	public void setSpeed()
	{
		//Basically creates a unit circle with 8 different directions
		int rightKey = (Input.keyPressed(KeyCode.D) || 
						Input.keyPressed(KeyCode.RIGHT)) ? 1 : 0;
		int leftKey = (Input.keyPressed(KeyCode.A) || 
						Input.keyPressed(KeyCode.LEFT)) ? 1 : 0;
		int upKey = (Input.keyPressed(KeyCode.W) || 
						Input.keyPressed(KeyCode.UP)) ? 1 : 0;
		int downKey = (Input.keyPressed(KeyCode.S) || 
						Input.keyPressed(KeyCode.DOWN)) ? 1 : 0;
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
		setSpeed();
		move();
		
		//Switch sprite's direction
		if(hspd > 0 && !flippedRight)
		{
			setImage("Res/IndoFlipped.png", 8);
			flippedRight = true;
		}
		else if(hspd < 0 && flippedRight)
		{
			setImage("Res/Indo.png", 8);
			flippedRight = false;
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
				subImageNumber = (subImageNumber + 1) % subImageMax;
				image.animate(subImageNumber);
				incrementImage--;
			}
			else
			{
				incrementImage += imageSpeed;
			}
		}
		else
		{
			image.animate(subImageNumber);
			subImageNumber = 0;
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
}
