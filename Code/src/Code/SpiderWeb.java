package Code;

/**
 * Created by Josefin on 2016-05-12.
 */
public class SpiderWeb extends GameObject {
    Player player;
    double direction;
    int damage;

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public SpiderWeb(double x, double y) {
        super(x, y, "Res/indaSpider.png", 1);
        hspd = 1.5;
        vspd = 1.5;
        speed = 1.5;
        imageSpeed = 0.05;
        damage = 3;
    }

    public void update()
    {
        shoot();
    }

    public void shoot()
    {
        double playerX = player.getX();
        double playerY = player.getY();
        double diffX = playerX-x;
        double diffY = playerY-y;
        direction = MathMethods.getDirectionBetweenPoints(0, 0, diffX, diffY);
        hspd = MathMethods.lengthDirX(speed, direction);
        vspd = MathMethods.lengthDirY(speed, direction);
    }



}
