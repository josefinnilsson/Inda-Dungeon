package Code;

public class SpeedPowerup extends Pickable
{

	public SpeedPowerup(double x, double y)
	{
		super(x, y, "Res/indaSpeed.png", 1);
	}

	@Override
	public void update()
	{
		double speed = 2*Game.player.getSpeed();
		super.update();
		if(isPicked())
		{
			Game.player.speedBoost(speed);
		}
	}
}
