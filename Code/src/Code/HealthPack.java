package Code;

public class HealthPack extends Pickable
{

	private double health;
	public HealthPack(double x, double y)
	{
		super(x, y, "Res/indaHealth.png", 1);
		health = 25;
	}

	@Override
	public void update()
	{
		super.update();
		if(isPicked())
		{
			Game.player.heal(health);
		}
	}
}
