import bagel.*;

public class GameObject {
    protected Point point;
    protected Image image;

    /**
     * Default constructor for GameObject
     */
    public GameObject(){}

    /**
     * Initialises a GameObject with the appropriate image
     * @param image
     */
    public GameObject(Image image) {
        this.image = image;
    }

    /**
     * Initialises instance of GameObject with a initial point and appropriate image
     * @param point
     * @param image
     */
    public GameObject(Point point, Image image) {
        this.point = point;
        this.image = image;
    }

    /**
     * Sets initial point for GameObject
     * @param point
     */
    public final void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Gets the current point of GameObject
     * @return current point of GameObject
     */
    public final Point getPoint() {
        return point;
    }

    /**
     * Renders GameObject at the current point
     * @param point
     */
    public final void render(Point point) {
        image.draw(point.x, point.y);
    }
}
