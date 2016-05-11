package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snake extends Enemy {

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Snake(double x, double y) {
        super(x, y, "Res/indaSnake.png", 2);
        speed = 0.4;
        imageSpeed = 0.2;
        damage =  1;
    }

    public void update()
    {
        nextPosition();
    }

    public void nextPosition()
    {
        double playerX = Game.player.getX();
        double playerY = Game.player.getY();
        double diffX = playerX-x;
        double diffY = playerY-y;
        float direction = (float)Math.atan2(diffY,diffX);
        x += speed * Math.cos(direction);
        y += speed * Math.sin(direction);
        setEnemy();

        //TODO: Check wall collision
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
