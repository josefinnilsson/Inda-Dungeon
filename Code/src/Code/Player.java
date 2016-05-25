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
	// These states control in which way the player object updates, and are
	// switched between depending on input and in-game events.
	private enum State
	{
		move, dash, attack
	}

	private int xAxis;
	private int yAxis;
	private State state;

	// Holds different values depending on input.
	private int rightKey;
	private int leftKey;
	private int upKey;
	private int downKey;
	private boolean leftMouse;
	private boolean rightMouse;

	private boolean flippedRight;
	private boolean dashing;
	private boolean dashable;
	private boolean attacking;
	private boolean attackable;
	private boolean attacked;

	public boolean malePlayer;

	private Alarm dashAlarm;
	private Alarm staminaRegenAlarm;
	private Alarm attackAlarm;
	private Alarm immortalTimer;
	private Alarm speedAlarm;
	private Alarm damageAlarm;

	private double stamina;
	private double maxStamina;

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

		damage = 25;
		attackable = true;
		attacking = false;
		attacked = false;
		attackAlarm = new Alarm();

		malePlayer = true;
		health = 100;
		stamina = 100;
		maxStamina = 100;
		staminaRegenAlarm = new Alarm();

		immortalTimer = new Alarm(30);
		speedAlarm = new Alarm();
		damageAlarm = new Alarm();

	}

	/**
	 * Set the player's horizontal and vertical speed depending on the input.
	 */
	public void setSpeed()
	{
		// Creates a "unit circle" with 8 different directions
		getInput();
		xAxis = rightKey - leftKey;
		yAxis = downKey - upKey;

		// Set the speed
		hspd = xAxis * speed;
		vspd = yAxis * speed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game_tutorial.GameObject#update()
	 * 
	 * Sets the speed and moves the player accordingly.
	 */
	@Override
	public void update()
	{
		// Count down alarms
		dashAlarm.tick();
		attackAlarm.tick();
		staminaRegenAlarm.tick();
		immortalTimer.tick();
		speedAlarm.tick();
		damageAlarm.tick();

		// Choose what to do depending on which state the player is in.
		switch(state)
		{
			case move:
				// Check if time to dash
				if(rightMouse && dashable && stamina >= 25)
				{
					stamina -= 25;
					state = State.dash;
					dashAlarm.setTime(10);
					if(staminaRegenAlarm.currentTime() < 60)
					{
						staminaRegenAlarm.setTime(60);
					}
				}
				// Check if time to attack
				else if(leftMouse && attackable && stamina >= 10)
				{
					stamina -= 10;
					state = State.attack;
					attackAlarm.setTime(20);
					if(staminaRegenAlarm.currentTime() < 60)
					{
						staminaRegenAlarm.setTime(60);
					}
				}

				// Control movement
				setSpeed();
				move();

				// Switch sprite's direction depending on speed
				setSpriteDirection();
				break;
			case dash:
				dash();
				break;
			case attack:
				attack();
				break;
			default:
				break;
		}

		if(speedAlarm.done())
		{
			speed = 1;
		}
		if(damageAlarm.done())
		{
			damage = 25;
		}

		checkEnemyCollision();
		regenerateStamina();
	}

	/**
	 * Returns whether different mouse buttons or keys are currently being
	 * pressed.
	 */
	private void getInput()
	{
		// Keys
		rightKey = (Input.keyPressed(KeyCode.D)
				|| Input.keyPressed(KeyCode.RIGHT)) ? 1 : 0;
		leftKey = (Input.keyPressed(KeyCode.A)
				|| Input.keyPressed(KeyCode.LEFT)) ? 1 : 0;
		upKey = (Input.keyPressed(KeyCode.W) || Input.keyPressed(KeyCode.UP))
				? 1 : 0;
		downKey = (Input.keyPressed(KeyCode.S)
				|| Input.keyPressed(KeyCode.DOWN)) ? 1 : 0;

		// Mouse buttons
		leftMouse = Input.mousePressed(MouseButton.PRIMARY);
		rightMouse = Input.mousePressed(MouseButton.SECONDARY);

		// Check if it's possible to dash
		if(!rightMouse)
			dashable = true;
		// Check if it's possible to attack
		if(!leftMouse)
			attackable = true;
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
			// Get direction to dash in
			setAxesToMouse();
			dashing = true;

			// Set speed
			hspd = xAxis * speed * 4;
			vspd = yAxis * speed * 4;

			// Switch sprite's direction depending on speed
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
	 * Makes the player attack in a certain direction.
	 */
	private void attack()
	{
		attackable = false;
		if(!attacking)
		{
			attacking = true;

			// Change sprite to attacking sprite
			if(malePlayer)
			{
				if(flippedRight)
				{
					setImage("Res/IndoAttack.png", 10);
				}
				else
				{
					setImage("Res/IndoAttackFlipped.png", 10);
				}
			}
			else
			{
				if(flippedRight)
				{
					setImage("Res/IndaAttack.png", 10);
				}
				else
				{
					setImage("Res/IndaAttackFlipped.png", 10);
				}
			}

			// Modify image speed so one attack animates throughout the time
			// of the attack.
			imageSpeed = ((double) 10) / ((double) attackAlarm.currentTime());
			imageIndex = 0;
		}
		else
		{
			// Time to move again
			if(attackAlarm.done())
			{
				state = State.move;
				attacking = false;
				attacked = false;
				imageSpeed = .2;
				imageIndex = 0;
				setPlayer(malePlayer);
			}
			// This is when the attack is committed
			else if(imageIndex == 6 && !attacked)
			{
				// Get direction to attack in
				setAxesToMouse();

				// Set attack coordinates
				double damageX = x + width / 2 + xAxis * 24;
				double damageY = y + height / 2 + yAxis * 24;
				Damage dmg = new Damage(damageX - 16, damageY - 16, this,
						damage);
				Game.objectWaitingRoom.add(dmg);
				attacked = true;

				// Create a slash for the attack
				int slashDir = (int) (MathMethods.getDirectionBetweenPoints(0,
						0, xAxis, yAxis) / 45);

				PlayerAttackSlash slash = new PlayerAttackSlash(x - 32, y - 32,
						slashDir);
				Game.objectWaitingRoom.add(slash);
			}
		}

		setSpeed();
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

	/**
	 * Returns the player's maximum stamina.
	 * @return the maximum stamina of the player.
	 */
	public double getMaxStamina()
	{
		return maxStamina;
	}

	/**
	 * Regenerates stamina over time.
	 */
	private void regenerateStamina()
	{
		if(staminaRegenAlarm.done())
		{
			if(stamina < maxStamina)
			{
				stamina += 1;
			}
			if(stamina > maxStamina)
			{
				stamina = maxStamina;
			}
		}
	}

	@Override
	public void animate()
	{
		// Only animate if moving or if attacking
		if(Math.abs(hspd) > 0 || Math.abs(vspd) > 0 || state == State.attack)
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
	 * Checks if the player collides with an enemy which attack by collision, if
	 * so, the player gets hit
	 */
	private void checkEnemyCollision()
	{
		for(GameObject go : Game.objects)
		{
			if(collidesWith(go) && go instanceof Enemy)
			{
				if(immortalTimer.done())
				{
					hit(((Enemy) go).getDamage());
					immortalTimer.setTime(30);
				}
			}
			if(collidesWith(go) && go instanceof SpiderWeb)
			{
				if(immortalTimer.done())
				{
					hit(((SpiderWeb) go).getDamage());
					immortalTimer.setTime(30);
					if(!((SpiderWeb) go).getCollided())
					{
						((SpiderWeb) go).unfold();
					}
				}
			}
			if(collidesWith(go) && go instanceof MonsterFire)
			{
				if(immortalTimer.done())
				{
					hit(((MonsterFire) go).getDamage());
					immortalTimer.setTime(30);
					((MonsterFire) go).setRemove();
				}
			}
		}
	}
	
	/**
	 * Gives the player a speed boost for a while.
	 * @param speed The new speed.
	 */
	public void speedBoost(double speed)
	{
		this.speed = speed;
		speedAlarm.setTime(180);
	}

	/**
	 * Gives the player a damage boost for a while.
	 * @param damage The new damage.
	 */
	public void damageBoost(int damage)
	{
		this.damage = damage;
		damageAlarm.setTime(360);
	}

	/**
	 * Makes the player invincible for 10 seconds.
	 */
	public void setTemporaryInvincible()
	{
		immortalTimer.setTime(600);
	}
}
