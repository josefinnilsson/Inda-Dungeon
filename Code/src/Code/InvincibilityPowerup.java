package Code;

public class InvincibilityPowerup extends Pickable
{
	public InvincibilityPowerup(double x, double y)
	{
		super(x, y, "Res/indaInvincibility.png", 1);
	}

	@Override
	public void update()
	{
		super.update();
		if(isPicked())
		{
			Game.player.setTemporaryInvincible();
		}
	}
}
