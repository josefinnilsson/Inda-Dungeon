package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class TrivialEnemy extends Enemy {
    private boolean right;
    private boolean left;

    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     */
    public TrivialEnemy(double x, double y) {
        super(x, y, "Res/indaSnail.png", 8);
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
            while(!wallCollision(Game.level, x+Math.signum(hspd), y))
            {
                x += Math.signum(hspd);
            }
            hspd = 0;
        }

        //Move horizontally
        x += hspd;

        //Check for vertical collision
        if(wallCollision(Game.level, x, y+vspd))
        {
            while(!wallCollision(Game.level, x, y+Math.signum(vspd)))
            {
                y += Math.signum(vspd);
            }
            vspd = 0;
        }

        //Move vertically
        y += vspd;
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
