import bagel.*;

public class Bullet extends GameObject implements Movable{
    private static final int STEP_SIZE = 25;
    private static final int DEAD_RANGE = 25;
    private static int numBulletsShot = 0;

    private static boolean anyBulletShot;

    private boolean bulletShot;
    private boolean bulletHit = false;

    private double directionX;
    private double directionY;

    /**
     * Initialises instance of Bullet with the appropriate image
     * @param image
     */
    public Bullet(Image image) {
        super(image);
    }

    /**
     * Sets bulletShot to true or false
     * @param bulletShot
     */
    public void setBulletShot(boolean bulletShot) {
        this.bulletShot = bulletShot;
    }

    /**
     * Sets anyBulletShot to true or false
     * @param anyBulletShot
     */
    public static void setAnyBulletShot(boolean anyBulletShot) {
        Bullet.anyBulletShot = anyBulletShot;
    }

    /**
     * Checks if the bullet has hit the target (zombie)
     * @param zombie
     * @return true or false
     */
    public boolean isBulletHit(Zombie zombie) {
        double distance = this.point.distanceTo(zombie.getPoint());
        if (distance < DEAD_RANGE) {
            return true;
        }
        return false;
    }

    /**
     * @return if bullet has been shot
     */
    public boolean getBulletShot() {
        return bulletShot;
    }

    /**
     * @return total number of bullets that have been shot
     */
    public static int getNumBulletsShot() {
        return numBulletsShot;
    }

    /**
     * @return true if any bullet has been shot, false otherwise
     */
    public static boolean getAnyBulletShot() {
        return anyBulletShot;
    }

    /**
     * Sets the direction of the bullet to the closest zombie
     * @param zombie
     */
    public void setDirection(Zombie zombie) {
        double distance = this.point.distanceTo(zombie.getPoint());
        directionX = (zombie.getPoint().x - this.point.x) / distance;
        directionY = (zombie.getPoint().y - this.point.y) / distance;
    }

    /**
     * Controls bullet movement
     */
    public void move() {
        this.point.x += directionX * STEP_SIZE;
        this.point.y += directionY * STEP_SIZE;
    }

    /**
     * Keeps count of number of bullets shot
     */
    public static void addNumberBulletsShot() {
        numBulletsShot++;
    }
}
