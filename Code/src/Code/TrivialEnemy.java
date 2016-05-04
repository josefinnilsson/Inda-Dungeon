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
     * @param image     The object's sprite.
     * @param subImages
     */
    public TrivialEnemy(double x, double y, String image, int subImages) {
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

    public void update() {

    }
}
