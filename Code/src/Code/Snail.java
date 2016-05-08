package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snail extends Enemy {
    private boolean right;
    private boolean left;
    private Snail snail;

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Snail(double x, double y) {
        super(x, y, "Res/indaSnail.png", 2);
        speed = 0.4;
        imageSpeed = 0.2;
        damage =  1;
        left = false;
        right = true;
    }

    @Override
    public void update()
    {
        nextPosition();
    }

    private void nextPosition() {
        if(left) {
            if (wallCollision(Game.level, x, y))
            {
                speed = -speed;
                right = true;
                left = false;
                flippedRight=true;
                setEnemy();

            }
        } else if (right) {
            if (wallCollision(Game.level, x, y))
            {
                speed = -speed;
                right = false;
                left = true;
                flippedRight=false;
                setEnemy();
            }
        }
        x += speed;
    }

    private void setEnemy()
    {

        if(flippedRight)
        {
            setImage("Res/indaSnail.png", 2);
        }
        else
        {
            setImage("Res/indaSnailFlipped.png", 2);
        }

    }

}
