package Code;

import Code.GameObject;

/**
 * Created by Josefin on 2016-05-12.
 */
public class SpiderWeb extends GameObject {
    /**
     * Initialize the object.
     *
     * @param x         The object's x-coordinate.
     * @param y         The object's y-coordinate.
     * @param image     The object's sprite.
     * @param subImages
     */
    public SpiderWeb(double x, double y, String image, int subImages) {
        super(x, y, image, subImages);
    }

    public void update()
    {
        nextPosition();
    }

    private void nextPosition()
    {
        setWeb();
        //TODO
    }

    private void setWeb()
    {
        setImage("Res/IndaSpiderWeb.png", 1);
    }


}
