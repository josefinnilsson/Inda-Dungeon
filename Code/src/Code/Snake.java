package Code;


import java.util.Random;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snake extends Enemy {
    Random random = new Random();
    private double direction;

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Snake(double x, double y) {
        super(x, y, "Res/indaSnake.png", 2);
        speed = 1;
        imageSpeed = 0.2;
        damage =  1;
    }

    public void update()
    {
        nextPosition();
        //TODO: make snake move random until player gets close
    }

    private void nextPosition()
    {
        double playerX = Game.player.getX();
        double playerY = Game.player.getY();
        double diffX = playerX-x;
        double diffY = playerY-y;

        if (diffX < 5 || diffY < 5) { //this condition needs to change
            direction = MathMethods.getDirectionBetweenPoints(0, 0, diffX, diffY);

        } else {
            direction = Math.abs(random.nextInt() % 360) + 1;
            //direction cannot change for every frame, FIX 
        }
        x += MathMethods.lengthDirX(speed, direction);
        y += MathMethods.lengthDirY(speed, direction);

        if (wallCollision(Game.level, x, y))
        {
            speed = 0;
            //TODO: make snake go along the wall
        }
        setEnemy();
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
