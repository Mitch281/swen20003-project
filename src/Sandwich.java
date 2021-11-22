import bagel.*;

public class Sandwich extends GameObject implements Meetable {
    private static final int MET_RANGE = 50;
    private static int numSandwiches;
    private static int numSandwichesEaten;
    private boolean sandwichEaten;

    /**
     * Initialises instance of Sandwich with the appropriate image
     * @param image
     */
    public Sandwich(Image image) {
        super(image);
    }

    /**
     * Counts number of sandwiches loaded from environment
     */
    public static void addSandwichCount() {
        numSandwiches++;
    }

    /**
     * Counts number of sandwiches eaten
     */
    public static void addSandwichEatenCount() {
        numSandwichesEaten++;
    }

    /**
     * Sets sandwichEaten to true or false (always set to true)
     * @param sandwichEaten
     */
    public void setSandwichEaten(boolean sandwichEaten) {
        this.sandwichEaten = sandwichEaten;
    }

    /**
     * Checks if all sandwiches are eaten
     * @return true or false
     */
    public static boolean isAllSandwichesEaten() {
        if (numSandwiches == numSandwichesEaten) {
            return true;
        }
        return false;
    }

    /**
     * This method returns the original number of sandwiches (doesn't decrease if sandwich is eaten)
     * @return if sandwich is eaten
     */
    public static int getNumSandwiches() {
        return numSandwiches;
    }

    /**
     * @return if sandwich is eaten
     */
    public boolean getSandwichEaten() {
        return sandwichEaten;
    }

    /**
     * Checks if a player has met (eaten) a sandwich
     * @param player
     * @return true or false
     */
    public boolean meets(Player player) {
        double distance = this.point.distanceTo(player.getPoint());
        if (distance <= (MET_RANGE + 0.10)) {
            return true;
        }
        return false;
    }
}
