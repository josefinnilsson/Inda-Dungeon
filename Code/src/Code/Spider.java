package Code;

import java.util.Random;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Spider extends Enemy {
    private double direction;
    private boolean close;
    Random random;
    Alarm alarm;
    Alarm shootTimer;
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
        shootTimer = new Alarm(20);
        random = new Random();
        close = false;
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
        if(diffX < 3 || diffY < 3 )
        {
            close = true;
        } else
        {
            close = false;
        }
        if(close)
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
        //TODO: Fix bug so that the spider doesn't get stuck at collision 
        alarm.tick();
        shootTimer.tick();
    }

    private void attackPlayer()
    {
        if(alarm.done())
        {
            SpiderWeb spiderWeb = new SpiderWeb(x, y);
           // spiderWeb.shoot();
            shootTimer.setTime(30);
        }
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
