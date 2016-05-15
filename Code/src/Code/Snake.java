package Code;


import java.util.Random;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snake extends Enemy {
    Random random;
    Alarm alarm;
    private double direction;
    private boolean close;


    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Snake(double x, double y) {
        super(x, y, "Res/indaSnake.png", 2);
        speed = 0.4;
        imageSpeed = 0.05;
        damage =  1;
        alarm = new Alarm(200);
        random = new Random();
        damage = 3;
        hspd = 0.4;
        vspd = 0.4;
        close = false;
    }

    public void update()
    {
        nextPosition();
    }

    private void nextPosition()
    {
        double playerX = Game.player.getX() + Game.player.getWidth()/2;
        double playerY = Game.player.getY() + 3*Game.player.getHeight()/4;
        double diffX = playerX-(x+width/2);
        double diffY = playerY-(y+3*height/4);
        if(Math.abs(diffX) < 64 && Math.abs(diffY) < 64)
        {
            close = true;
        } else
        {
            close = false;
        }
        if(close) {
            direction = MathMethods.getDirectionBetweenPoints(0, 0, diffX, diffY);
        } else if (alarm.done())
        {
            direction = Math.abs(random.nextInt() % 360) + 1;
            if (direction >= 90 && direction <= 270) {
                flippedRight = false;
            } else {
                flippedRight = true;
            }
            setEnemy();
            alarm.setTime(200);

        }
        hspd = MathMethods.lengthDirX(speed, direction);
        vspd = MathMethods.lengthDirY(speed, direction);
        move();
        alarm.tick();
    }


    private void setEnemy()
    {
        if(flippedRight)
        {
            setImage("Res/indaSnake.png", 2);
        }
        else
        {
            setImage("Res/indaSnakeFlipped.png", 2);
        }

    }
}
