import bagel.*;

import java.util.ArrayList;

public class Player extends GameObject implements Movable{
    public static final int STEP_SIZE = 10;
    public static final int ENERGY_INCREASE = 5;
    public static final int ENERGY_DECREASE = -3;
    public static final int ENERGY_THRESHOLD = 3;

    private int energyLevel;
    private double directionX;
    private double directionY;

    /**
     * Initialises instance of Player with appropriate image
     * @param image
     */
    public Player(Image image) {
        super(image);
    }

    /**
     * Sets energy level of player
     * @param energyLevel
     */
    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    /**
     * Our destination is either zombie, sandwich or treasure depending on situation. This method sets our direction
     * to the appropriate game object
     * @param destination: the point of the closest zombie, sandwich or treasure
     */
    public void setDirection(Point destination) {
        double distance = this.point.distanceTo(destination);
        directionX = (destination.x - this.point.x) / distance;
        directionY = (destination.y - this.point.y) / distance;
    }

    /**
     * Finds the closest zombie to the player. The player will then move towards this closest zombie (assuming the
     * player's energy level is greater then or equal to 3)
     * @param numZombies
     * @param zombies
     * @return closest zombie
     */
    public Zombie findClosestZombie(int numZombies, ArrayList<Zombie> zombies) {
        double distanceToZombie;
        // Maximum distance possible in ShadowTreasure. Every distance we compute will be lower.
        double minimumDistance = (Window.getHeight()) * (Window.getHeight()) + (Window.getWidth()) * (Window.getWidth());

        int closestZombieIndex = 0;

        for (int i = 0; i < numZombies; i++) {
            Zombie zombie = zombies.get(i);
            distanceToZombie = this.point.distanceTo(zombie.getPoint());
            if (i == 0 && !zombie.getZombieShot()) {
                minimumDistance = distanceToZombie;
                closestZombieIndex = i;
            } else if (!zombie.getZombieShot()) {
                if (distanceToZombie < minimumDistance) {
                    minimumDistance = distanceToZombie;
                    closestZombieIndex = i;
                }
            }
        }
        return zombies.get(closestZombieIndex);
    }

    /**
     * Finds the closest sandwich to the player. The player will then move towards this sandwich (assuming the
     * player's energy level is less than 3)
     * @param numSandwiches
     * @param sandwiches
     * @return closest sandwich
     */
    public Sandwich findClosestSandwich(int numSandwiches, ArrayList<Sandwich> sandwiches) {
        double distanceToSandwich;
        // Maximum distance possible in ShadowTreasure. Every distance we compute will be lower.
        double minimumDistance = (Window.getHeight()) * (Window.getHeight()) + (Window.getWidth()) * (Window.getWidth());

        int closestSandwichIndex = 0;

        for (int i = 0; i < numSandwiches; i++) {
            Sandwich sandwich = sandwiches.get(i);
            distanceToSandwich = this.point.distanceTo(sandwich.getPoint());
            if ((i == 0) && (!sandwich.getSandwichEaten())) {
                minimumDistance = distanceToSandwich;
                closestSandwichIndex = i;
            } else if (!sandwich.getSandwichEaten()) {
                if (distanceToSandwich < minimumDistance) {
                    minimumDistance = distanceToSandwich;
                    closestSandwichIndex = i;
                }
            }
        }
        return sandwiches.get(closestSandwichIndex);
    }

    /**
     * @return energy level of player
     */
    public int getEnergyLevel() {
        return energyLevel;
    }

    /**
     * Controls movement of player
     */
    public void move() {
        this.point.x += directionX * STEP_SIZE;
        this.point.y += directionY * STEP_SIZE;
    }
}
