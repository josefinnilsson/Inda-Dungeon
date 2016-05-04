package Code;

/**
 * Created by Josefin on 2016-05-04.
 */
public class Enemy extends GameObject {
    private int xAxis;
    private int yAxis;
    private int health;
    private boolean flippedRight;


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
        speed = 1;
        imageSpeed = .2;
        flippedRight = true;
        health = 100;
    }

    @Override
    public void update() {

    }
}
