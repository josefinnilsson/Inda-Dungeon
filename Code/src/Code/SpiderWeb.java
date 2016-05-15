package Code;

/**
 * Created by Josefin on 2016-05-12.
 */
public class SpiderWeb extends GameObject {
    private int damage;
    private boolean remove;
    private double webDirection;

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
        speed = 0.5;
        imageSpeed = 0;
        imageIndex = 1;
        damage = 10;
        remove = false;
    }

    public void update()
    {
        x += hspd;
        y += vspd;
        checkCollision();
        imageIndex = 1;
    }

    public int getDamage() {
        return damage;
    }

    public void checkCollision()
    {
        if(wallCollision(Game.level, x+hspd, y))
        {
            remove = true;
        }
        if(wallCollision(Game.level, x, y+vspd))
        {
            remove = true;
        }
        if(x < 0 || x> Game.ROOM_WIDTH)
        {
            remove = true;
        }
        if(y < 0 || y> Game.ROOM_HEIGHT)
        {
            remove = true;
        }
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
