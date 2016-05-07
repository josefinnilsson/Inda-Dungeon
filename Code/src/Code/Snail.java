package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Snail extends Enemy {
    private boolean right;
    private boolean left;

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

    private void nextPosition() {
        if(left) {
            x -= speed;
        } else if (right) {
            x += speed;
        }
    }

    @Override
    public void update()
    {
        nextPosition();
    }

    /**
     * Moves the player according to walls and speed.
     */
    private void move()
    {
        prevX = x;
        prevY = y;
        //Check for horizontal collision
        if(wallCollision(Game.level, x+hspd, y))
        {
            //TODO
        }

        //Check for vertical collision
        if(wallCollision(Game.level, x, y+vspd)) {
            //TODO
        }

    }

    public void setEnemy(boolean type)
    {

        if(flippedRight)
        {
            setImage("Res/indaSnail.png", 8);
        }
        else
        {
            setImage("Res/indaSnailFlipped.png", 8);
        }

    }
}
