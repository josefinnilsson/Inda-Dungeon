package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snail extends Enemy {
    private boolean right;
    private boolean left;
    private boolean up;
    private boolean down;

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public Snail(double x, double y) {
        super(x, y, "Res/indaSnail.png", 2);
        hspd = 0.4;
        vspd = 0.4;
        speed = 0.4;
        imageSpeed = 0.05;
        damage =  1;
        left = false;
        right = true;
        up = true;
        down = false;
    }

    @Override
    public void update()
    {
        nextPosition();
    }

    private void nextPosition() {
        if(left)
        {
            if (wallCollision(Game.level, x+hspd, y))
            {
                hspd = -hspd;
                right = true;
                left = false;
                flippedRight=true;
                setEnemy();

            }
        } else if (right)
        {
            if (wallCollision(Game.level, x+hspd, y))
            {
                hspd = -hspd;
                right = false;
                left = true;
                flippedRight=false;
                setEnemy();
            }

        }
        x += hspd;

        //TODO: Make snail able to move upwards and downwards as well

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
