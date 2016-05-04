package Code;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Enemy extends GameObject {
    private int xAxis;
    private int yAxis;
    private int health;
    private boolean flippedRight;
    public int damage;


    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     * @param image     The object's sprite.
     * @param subImages
     */
    public Enemy(double x, double y, String image, int subImages) {
        super(x, y, image, subImages);
        xAxis = 0;
        yAxis = 0;
        flippedRight = true;
        health = 100;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void hit (int damage) {
        health -= damage;
        if (damage < 0) {
            health = 0;
        }
    }

    @Override
    public void render(GraphicsContext gc)
    {
        //Draw the image
        image.draw(gc, x, y, width, height);

        //Only animate if moving
        if(Math.abs(hspd) > 0 || Math.abs(vspd) > 0)
        {
            if(incrementImage >= 1)
            {
                imageIndex = (imageIndex + 1) % imageNumber;
                image.animate(imageIndex);
                incrementImage--;
            }
            else
            {
                incrementImage += imageSpeed;
            }
        }
        else
        {
            image.animate(imageIndex);
            imageIndex = 0;
        }
    }



}
