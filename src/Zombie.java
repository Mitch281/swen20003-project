import bagel.*;

public class Zombie extends GameObject implements Meetable {
    public static final int SHOOTING_RANGE = 150;
    private static int numZombies = 0;
    private static int numZombiesDead = 0;
    private boolean zombieShot;

    /**
     * Initialises instance of Zombie with appropriate image
      * @param image
     */
    public Zombie(Image image) {
        super(image);
    }

    /**
     * Counts how many zombies have been loaded from environment
     */
    public static void addNumZombies() {
        numZombies++;
    }

    /**
     * Counts how many zombies have been killed
     */
    public static void addNumZombiesDead() {
        numZombiesDead++;
    }

    /**
     * @return number of zombies that have been loaded from environment
     */
    public static int getNumZombies() {
        return numZombies;
    }

    /**
     * Sets zombieShot to true or false (always set to true)
     * @param zombieShot
     */
    public void setZombieShot(boolean zombieShot) {
        this.zombieShot = zombieShot;
    }

    /**
     * @return if zombie has been shot
     */
    public boolean getZombieShot() {
        return zombieShot;
    }

    /**
     * Checks if player has met (and thus shoots a bullet to zombie) zombie
     * @param player
     * @return true or false
     */
    public boolean meets(Player player) {
        double distance = this.point.distanceTo(player.getPoint());
        if (distance < SHOOTING_RANGE) {
            return true;
        }
        return false;
    }

    /**
     * @return if all true if all zombies are dead. False otherwise
     */
    public static boolean isAllZombiesDead() {
        if (numZombiesDead == numZombies) {
            return true;
        }
        return false;
    }
}
