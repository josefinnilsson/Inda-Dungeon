package Code;

public class SpeedPowerup extends Pickable
{

	private double speed;
	public SpeedPowerup(double x, double y)
	{
		super(x, y, "Res/indaSpeed.png", 1);
		speed = 2*Game.player.getSpeed();
	}

	@Override
	public void update()
	{
		super.update();
		if(isPicked())
		{
			Game.player.speedBoost(speed);
		}
	}
}
