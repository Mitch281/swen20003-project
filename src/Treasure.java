import bagel.*;

public class Treasure extends GameObject implements Meetable {
    private final static int MET_RANGE = 50;
    private boolean treasureReached = false;

    /**
     * Initialises instance of Treasure with appropriate image
     * @param image
     */
    public Treasure(Image image) {
        super(image);
    }

    /**
     * Checks if player has met treasure
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

    /**
     * Sets treasureReached to true or false (always set to true)
     * @param treasureReached
     */
    public void setTreasureReached(boolean treasureReached) {
        this.treasureReached = treasureReached;
    }

    /**
     * Gets value of treasureReached
     * @return true or false
     */
    public boolean getTreasureReached() {
        return treasureReached;
    }
}
