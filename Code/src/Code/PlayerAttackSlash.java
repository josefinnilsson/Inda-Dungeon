package Code;

/**
 * This class represents the slash made by the player when attacking. It is
 * in the game to help the user in knowing how far the player can hit.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class PlayerAttackSlash extends GameObject
{
	private boolean finished;
	
	public PlayerAttackSlash(double x, double y, int slashDir)
	{
		super(x, y, "Res/indaAttackSlashALU.png", 3);
		imageSpeed = .5;
		
		//Set image depending on direction of attack
		switch(slashDir)
		{
			case -3:
				setImage("Res/indaAttackSlashALU.png", 3);
				break;
			case -2:
				setImage("Res/indaAttackSlashAU.png", 3);
				break;
			case -1:
				setImage("Res/indaAttackSlashARU.png", 3);
				break;
			case 0:
				setImage("Res/indaAttackSlashAR.png", 3);
				break;
			case 1:
				setImage("Res/indaAttackSlashARD.png", 3);
				break;
			case 2:
				setImage("Res/indaAttackSlashAD.png", 3);
				break;
			case 3:
				setImage("Res/indaAttackSlashALD.png", 3);
				break;
			case 4:
				setImage("Res/indaAttackSlashAL.png", 3);
				break;
		}
		
		finished = false;
	}
	
	@Override
	public void update()
	{
		if(imageIndex == 2)
		{
			finished = true;
		}
	}

	/**
	 * Returns whether the slash's animation is done.
	 * @return true if it is, false otherwise.
	 */
	public boolean isFinished()
	{
		return finished;
	}
}
