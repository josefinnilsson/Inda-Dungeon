package Code;

import java.util.Random;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Spider extends Enemy {
    private double direction;
    private boolean close;
    private double playerX;
    private double playerY;
    private double diffX;
    private double diffY;
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
        shootTimer = new Alarm(200);
        random = new Random();
        close = false;
    }

    public void update() {
        nextPosition();
    }

    private void nextPosition()
    {
        playerX = Game.player.getX();
        playerY = Game.player.getY();
        diffX = playerX-x;
        diffY = playerY-y;
        if(Math.abs(diffX) < 100 && Math.abs(diffY) < 100 )
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
        alarm.tick();
        shootTimer.tick();
    }

    private void attackPlayer()
    {
        if(shootTimer.done())
        {
            SpiderWeb spiderWeb = new SpiderWeb(x, y);
            Game.objectWaitingRoom.add(spiderWeb);
            spiderWeb.shoot();
            spiderWeb.checkCollision();
            shootTimer.setTime(200);
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
