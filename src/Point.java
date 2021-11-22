public class Point {
    public double x;
    public double y;

    /**
     * Initialises instance of Point with x value and y value
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param point
     * @return the distance between two points
     */
    public double distanceTo(Point point) {
        return Math.sqrt((this.x - point.x) * (this.x - point.x) + (this.y - point.y) * (this.y - point.y));
    }
}