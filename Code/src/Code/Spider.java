package Code;

import java.util.Random;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Spider extends Enemy {
    private double direction;
    Random random;
    Alarm alarm;
    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Spider(double x, double y) {
        super(x, y, "Res/indaSpider.png", 2);
        hspd = 0.4;
        vspd = 0.4;
        speed = 0.4;
        imageSpeed = 0.05;
        damage =  1;
        alarm = new Alarm(50);
        random = new Random();
    }

    public void update() {
        nextPosition();
    }

    private void nextPosition()
    {
        double playerX = Game.player.getX();
        double playerY = Game.player.getY();
        double diffX = playerX-x;
        double diffY = playerY-y;
        if (diffX < 4 || diffY < 4)
        {
            attackPlayer();
        }
        else if (alarm.done()) {
            direction = Math.abs(random.nextInt() % 360) + 1;
            if (direction >= 90 && direction <= 270) {
                flippedRight = false;
            } else {
                flippedRight = true;
            }
            setEnemy();
            alarm.setTime(50);
        }
        hspd = MathMethods.lengthDirX(speed, direction);
        vspd = MathMethods.lengthDirY(speed, direction);
        move();
        alarm.tick();
    }

    private void attackPlayer()
    {

    }

    private void setEnemy()
    {
        if(flippedRight)
        {
            setImage("Res/indaSpider.png", 2);
        }
        else
        {
            setImage("Res/indaSpiderFlipped.png", 2);
        }
    }
}
