import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import bagel.*;
import bagel.util.Colour;

public class ShadowTreasure extends AbstractGame {
    private GameObject background = new GameObject(new Point(Window.getWidth() / 2, Window.getHeight() / 2),
            new Image("res/images/background.png"));
    private Player player = new Player(new Image("res/images/player.png"));
    private Treasure treasure = new Treasure(new Image("res/images/treasure.png"));
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private ArrayList<Sandwich> sandwiches = new ArrayList<Sandwich>();
    private Bullet bullet = new Bullet(new Image("res/images/shot.png"));

    private int frameCount = 0;
    private static final int TICK_CYCLE = 10;
    private boolean gameOver = false;

    private final Font font = new Font("res/font/DejaVuSans-Bold.ttf", 20);
    private final FileWriter writer = new FileWriter("res/IO/output.csv");
    private DrawOptions drawOption = new DrawOptions();


    // for rounding double number; use this to print the location of the player
    private static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Loads environment
     * @throws IOException
     */
    public ShadowTreasure() throws IOException {
        this.loadEnvironment("res/test/test_output/test5/environment.csv");
    }

    /**
     * Load from input file
     */
    private void loadEnvironment(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String text;
            while ((text = br.readLine()) != null) {
                String cells[] = text.split(",");
                // Gets rid of special characters.
                cells[0] = cells[0].replaceAll("[^a-zA-Z0-9]", "");

                if (cells[0].equals("Player")) {
                    player.setPoint(new Point(Double.parseDouble(cells[1]), Double.parseDouble(cells[2])));
                    player.setEnergyLevel(Integer.parseInt(cells[3]));
                } else if (cells[0].equals("Zombie")) {
                    zombies.add(new Zombie(new Image("res/images/zombie.png")));

                    // Adding the attributes (only point in this case) of
                    // the latest zombie added into the zombies array.
                    Zombie currentZombie = zombies.get(Zombie.getNumZombies());
                    currentZombie.setPoint(new Point(Double.parseDouble(cells[1]),
                            Double.parseDouble(cells[2])));

                    // Keeps track of number of zombies in the game.
                    Zombie.addNumZombies();
                } else if (cells[0].equals("Treasure")) {
                    treasure.setPoint(new Point(Double.parseDouble(cells[1]), Double.parseDouble(cells[2])));
                } else if (cells[0].equals("Sandwich")) {
                    sandwiches.add(new Sandwich(new Image("res/images/sandwich.png")));

                    //Adding the attributes (only point in this case) of
                    // the latest sandwich added into the sandwiches array.
                    Sandwich currentSandwich = sandwiches.get(Sandwich.getNumSandwiches());
                    currentSandwich.setPoint(new Point(Double.parseDouble(cells[1]), Double.parseDouble(cells[2])));

                    // Keeps track of number of sandwiches in the game.
                    Sandwich.addSandwichCount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a state update.
     */
    @Override
    public void update(Input input) {

        // Close window if game over and print required output.
        if (gameOver) {
            Window.close();
            if (treasure.getTreasureReached()) {
                System.out.println(player.getEnergyLevel() + ",success!");
            }
            else {
                System.out.println(player.getEnergyLevel());
            }
        }

        frameCount++;

        if (!gameOver) {
            background.render(background.getPoint());
            treasure.render(treasure.getPoint());
            player.render(player.getPoint());
            font.drawString("energy : " + player.getEnergyLevel(), 20, 760,
                    drawOption.setBlendColour(Colour.BLACK));

            for (Zombie zombie : zombies) {
                if (!zombie.getZombieShot()) {
                    zombie.render(zombie.getPoint());
                }
            }
            for (Sandwich sandwich : sandwiches) {
                if (!sandwich.getSandwichEaten()) {
                    sandwich.render(sandwich.getPoint());
                }
            }
            if (bullet.getBulletShot()) {
                bullet.render(bullet.getPoint());
            }
        }

        // Player moves toward treasure if all zombies are dead, regardless of energy level.
        if (Zombie.isAllZombiesDead()) {
            player.setDirection(treasure.getPoint());

            // Player meets sandwich along the way to treasure and eats it.
            for (Sandwich sandwich: sandwiches) {
                if (!sandwich.getSandwichEaten()) {
                    if (sandwich.meets(player)) {
                        sandwich.setSandwichEaten(true);
                    }
                }
            }

            // Player meets treasure
            if (treasure.meets(player)) {
                gameOver = true;
                treasure.setTreasureReached(true);
            }

        } else if (!Zombie.isAllZombiesDead()) {
            // Find the closest zombie and make the player move towards it.
            if (player.getEnergyLevel() >= Player.ENERGY_THRESHOLD || Bullet.getAnyBulletShot()) {
                Zombie closestZombie = player.findClosestZombie(Zombie.getNumZombies(), zombies);
                Point closestZombiePoint = closestZombie.getPoint();
                player.setDirection(closestZombiePoint);

                // Detects if we need to shoot a bullet.
                if (closestZombie.meets(player)) {
                    Bullet.setAnyBulletShot(true);

                    // If the bullet is not shot, we initialise all the bullet information.
                    if (!bullet.getBulletShot()) {
                        bullet.setPoint(new Point(player.getPoint().x, player.getPoint().y));
                        bullet.setBulletShot(true);
                        player.setEnergyLevel(player.getEnergyLevel() + Player.ENERGY_DECREASE);
                        bullet.setDirection(closestZombie);
                    }

                    // Collision detection between bullet and zombie.
                    if (bullet.isBulletHit(closestZombie)) {
                        bullet.setBulletShot(false);
                        closestZombie.setZombieShot(true);

                        // Write last line of bullet position.
                        try {
                            writer.append(df.format(bullet.getPoint().x) + "," + df.format(bullet.getPoint().y));
                            writer.append("\n");
                            writer.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Zombie.addNumZombiesDead();
                        if (Bullet.getNumBulletsShot() < (Zombie.getNumZombies() - 1)) {
                            Bullet.addNumberBulletsShot();
                        }
                        Bullet.setAnyBulletShot(false);
                    }
                }
            }

            // Find the closest sandwich and make the player move towards it.
            else if (!Sandwich.isAllSandwichesEaten()) {
                Sandwich closestSandwich = player.findClosestSandwich(Sandwich.getNumSandwiches(), sandwiches);
                Point closestSandwichPoint = closestSandwich.getPoint();
                player.setDirection(closestSandwichPoint);

                // Player eats sandwich.
                if (closestSandwich.meets(player)) {
                    player.setEnergyLevel(player.getEnergyLevel() + Player.ENERGY_INCREASE);
                    closestSandwich.setSandwichEaten(true);
                    Sandwich.addSandwichEatenCount();
                }
            }

            // There are no sandwiches left but still zombies alive, and our player's energy level is less than 3. Thus,
            // it is game over for the player.
            else {
                gameOver = true;
            }
        }

        // Player and bullet movement.
        if ((frameCount % TICK_CYCLE) == 0) {
            player.move();
            if (bullet.getBulletShot()) {

                // Write bullet position into csv file.
                try {
                    String line = df.format(bullet.getPoint().x) + "," + df.format(bullet.getPoint().y);
                    writer.append(line);
                    writer.append("\n");
                    writer.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                bullet.move();
            }
        }
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) throws IOException {
        ShadowTreasure game = new ShadowTreasure();
        game.run();
    }
}
