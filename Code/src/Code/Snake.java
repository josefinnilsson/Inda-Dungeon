package Code;


import java.util.Random;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snake extends Enemy {
    Random random;
    Alarm alarm;
    private double direction;


    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Snake(double x, double y) {
        super(x, y, "Res/indaSnake.png", 2);
        speed = 0.5;
        imageSpeed = 0.05;
        damage =  1;
        alarm = new Alarm(40); //change this condition?
        random = new Random();
        damage = 3;
        hspd = 0.5;
        vspd = 0.5;
    }

    public void update()
    {
        nextPosition();
    }

    private void nextPosition()
    {
        double playerX = Game.player.getX();
        double playerY = Game.player.getY();
        double diffX = playerX-x;
        double diffY = playerY-y;
        if(diffX < 1 || diffY < 1) { //change this condition?
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
            alarm.setTime(40); //change this condition?

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
