package Code;

public class DamagePowerup extends Pickable
{
	public DamagePowerup(double x, double y)
	{
		super(x, y, "Res/indaStrength.png", 1);
	}

	@Override
	public void update()
	{
		int damage = 2*Game.player.getDamage();
		super.update();
		if(isPicked())
		{
			Game.player.damageBoost(damage);
		}
	}
}
