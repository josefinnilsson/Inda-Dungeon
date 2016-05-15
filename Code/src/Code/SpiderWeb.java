package Code;

/**
 * Created by Josefin on 2016-05-12.
 */
public class SpiderWeb extends GameObject {
    private int damage;
    private boolean remove;
    private double webDirection;
    private Alarm killSwitch;
    private boolean collided;

    /**
     * Initialize the object.
     *
     * @param x The object's x-coordinate.
     * @param y The object's y-coordinate.
     */
    public SpiderWeb(double x, double y) {
        super(x, y, "Res/IndaSpiderWeb.png", 2);
        hspd = 0.5;
        vspd = 0.5;
        speed = 1;
        imageSpeed = 0;
        imageIndex = 1;
        image.animate(imageIndex);
        damage = 10;
        remove = false;
        collided = false;
        killSwitch = new Alarm();
    }

    public void update()
    {
    	killSwitch.tick();
        x += hspd;
        y += vspd;
        if(killSwitch.done() && collided)
        {
        	remove = true;
        }
        else if(!collided)
        {
        	checkCollision();
        }
    }

    public int getDamage() {
        return damage;
    }

    public void checkCollision()
    {
        if(wallCollision(Game.level, x+hspd, y))
        {
        	unfold();
        }
        if(wallCollision(Game.level, x, y+vspd))
        {
        	unfold();
        }
        if(x < 0 || x > Game.ROOM_WIDTH)
        {
        	remove = true;
        }
        if(y < 0 || y > Game.ROOM_HEIGHT)
        {
        	remove = true;
        }
    }

    /**
     * Unfolds the spider web, encapsulating its target.
     */
    public void unfold()
    {
    	killSwitch.setTime(60);
    	imageIndex = 0;
        image.animate(imageIndex);
        hspd = 0;
        vspd = 0;
        collided = true;
    }
    
    /**
     * Returns whether spider web has collided with something or not.
     * @return true if it has collided, false otherwise.
     */
    public boolean getCollided()
    {
    	return collided;
    }
    
    public boolean shouldRemove()
    {
        return remove;
    }

    public void setRemove()
    {
        remove = true;
    }

    public void shoot()
    {
        double pX = Game.player.getX();
        double pY = Game.player.getY();
        webDirection = MathMethods.getDirectionBetweenPoints(x, y, pX, pY);
        if(!remove)
        {
            hspd = MathMethods.lengthDirX(speed, webDirection);
            vspd = MathMethods.lengthDirY(speed, webDirection);
            x += hspd;
            y += vspd;
        }
    }
}
