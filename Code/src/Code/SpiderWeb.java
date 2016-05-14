package Code;

/**
 * Created by Josefin on 2016-05-12.
 */
public class SpiderWeb extends GameObject {
    double direction;
    int damage;
    boolean active;

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public SpiderWeb(double x, double y) {
        super(x, y, "Res/IndaSpiderWeb.png", 2);
        hspd = 1.5;
        vspd = 1.5;
        speed = 1.5;
        imageSpeed = 0.05;
        damage = 3;
        active = false;
    }

    public void update()
    {
        shoot();
    }

    public void shoot()
    {
        double playerX = Game.player.getX();
        double playerY = Game.player.getY();
        double diffX = playerX-x;
        double diffY = playerY-y;
        direction = MathMethods.getDirectionBetweenPoints(0, 0, diffX, diffY);
        active = true;
        if(x < 0 || x > Game.ROOM_WIDTH)
        {
            active = false;
        }
        if(y < 0 || y > Game.ROOM_HEIGHT)
        {
            active = false;
        }
        if(active)
        {
            hspd = MathMethods.lengthDirX(speed, direction);
            vspd = MathMethods.lengthDirY(speed, direction);
            x += hspd;
            y += vspd;
        }
    }



}
