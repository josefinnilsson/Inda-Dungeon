package Code;

import java.util.Random;

/**
 * This class is a representation of the boss in the game. It takes much more
 * strength to beat this creature.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Monster extends Enemy
{
	//These states control in which way the player object updates, and are
	//switched between depending on input and in-game events.
	private enum State
	{
		move,
		dash,
		shoot
	}
	
	private Random r;
	private Alarm directionAlarm;
	private Alarm dashAlarm;
	private Alarm shootAlarm;
	
	private double direction;
	private State state;
	
	private boolean dashing;

	// TODO: FIX MONSTER AI!!!

	/**
	 * Initialize the monster.
	 *
	 * @param x The monster's x-coordinate.
	 * @param y The monster's y-coordinate.
	 */
	public Monster(double x, double y)
	{
		super(x, y, "Res/indaMonster.png", 4);
		speed = 0.5;
		imageSpeed = 0.05;
		damage = 1;
		
		r = new Random();
		directionAlarm = new Alarm(120);
		dashAlarm = new Alarm(r.nextInt(1080));
		shootAlarm = new Alarm(r.nextInt(1440));
		
		health = 500;
		damage = 25;
		state = State.move;
		dashing = false;
	}

	public void update()
	{
		//Count down the alarms.
		directionAlarm.tick();
		dashAlarm.tick();
		shootAlarm.tick();
		
		switch(state)
		{
			case move:
				imageSpeed = 0.05;
				if(dashAlarm.done())
				{
					state = State.dash;
				}
				else if(shootAlarm.done())
				{
					state = State.shoot;
				}
				
				if(directionAlarm.done())
				{
					direction = Math.abs(r.nextInt() % 360) + 1;
					if(direction >= 90 && direction <= 270)
					{
						flippedRight = false;
					} else
					{
						flippedRight = true;
					}
					setEnemy();
					directionAlarm.setTime(120);

				}
				hspd = MathMethods.lengthDirX(speed, direction);
				vspd = MathMethods.lengthDirY(speed, direction);
				move();
				break;
			case dash:
				imageSpeed = 0;
				
				if(!dashing)
				{
					double playerX = Game.player.getX() + 
													Game.player.getWidth()/2;
			        double playerY = Game.player.getY() + 
	        										3*Game.player.getHeight()/4;
			        double diffX = playerX-(x+width/2);
			        double diffY = playerY-(y+3*height/4);
			        direction = MathMethods.getDirectionBetweenPoints(0, 0, 
			        											diffX, diffY);
			        hspd = MathMethods.lengthDirX(speed*8, direction);
			        vspd = MathMethods.lengthDirY(speed*8, direction);
			        if(direction >= 90 && direction <= 270)
					{
						flippedRight = false;
					} else
					{
						flippedRight = true;
					}
			        setEnemy();
			        dashing = true;
				}
				if(wallCollision(Game.level, x, y + vspd) || 
					wallCollision(Game.level, x + hspd, y))
				{
					state = State.move;
					dashAlarm.setTime(r.nextInt(2160));
					dashing = false;
				}
				move();
				break;
			case shoot:
				double playerX = Game.player.getX() + 
								Game.player.getWidth()/2;
				double playerY = Game.player.getY() + 
								3*Game.player.getHeight()/4;
				double diffX = playerX-(x+width/2);
				double diffY = playerY-(y+3*height/4);
				direction = MathMethods.getDirectionBetweenPoints(0, 0, 
											diffX, diffY);
				
				
				
				shootAlarm.setTime(r.nextInt(3240));
				state = State.move;
				break;
			default:
				break;
			
		}
	}

	private void setEnemy()
	{
		if(flippedRight)
		{
			setImage("Res/indaMonster.png", 4);
		} else
		{
			setImage("Res/indaMonsterFlipped.png", 4);
		}

	}
}
