package model;

import java.util.ArrayList;
import java.util.Random;

import model.item.Breaker;
import model.item.Coin;
import model.item.Hint;
import model.item.Item;
import model.item.Items;
import model.item.Skip;
import model.maze.Maze;
import model.player.Player;
import model.utility.Coordinate;

// Game has a player and map.
public class Game {

    private Maze maze;
    private Player player;
    private ArrayList<Coordinate> itemPosition;
    private ArrayList<Item> items;

    private String itemMessage;

    private Random random;

    private static final int NUM_OF_ITEM = 12;

    // EFFECTS: Constructs a new game object with a new player at 0,0
    // and a random maze
    public Game(int width, int height) {
        maze = new Maze(width - 2, height - 2);
        player = new Player();
        random = new Random();

        init();
    }

    // EFFECTS: true if getPlayer.getPosition().isSame(getMaze().getExit())
    public boolean isEnded() {
        return player.getPosition().isSame(maze.getExit());
    }

    // MODIFIES: this
    // EFFECTS: creates a new maze as the next level
    public void nextLevel() {
        int width = maze.getWidth();
        int height = maze.getHeight();
        this.maze = new Maze(width, height);

        init();
    }

    public int getItemsSize() {
        return items.size();
    }

    public Coordinate getItemPosition(int index) {
        return itemPosition.get(index);
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public Item popItem(int index) {
        Item i = items.get(index);
        items.remove(index);
        itemPosition.remove(index);

        return i;
    }

    public Maze getMaze() {
        return maze;
    }

    public Player getPlayer() {
        return player;
    }

    public String getItemMessage() {
        return itemMessage;
    }

    public void setItemMessage(String itemMessage) {
        this.itemMessage = itemMessage;
    }

    private void generateItems() {
        int index;
        for (int i = 0; i < NUM_OF_ITEM; i++) {
            index = random.nextInt(Items.values().length);
            Items type = Items.values()[index];
            if (type == Items.coin) {
                items.add(new Coin(random.nextInt(20) + 1));
            } else if (type == Items.breaker) {
                int d = random.nextInt(5) + 1;
                items.add(new Breaker(d));
            } else if (type == Items.skip) {
                items.add(new Skip());
            } else if (type == Items.hint) {
                items.add(new Hint());
            } else {
                items.add(new Coin(random.nextInt(20) + 1));
            }
        }
    }

    private void init() {
        player.setPosition(maze.getStart());
        itemPosition = new ArrayList<>();
        itemMessage = "New Maze !!!";

        int index;
        do {
            index = random.nextInt(maze.getNumOfRoad());
            Coordinate road = maze.getaRoad(index);
            if (!itemPosition.contains(road)) {
                itemPosition.add(road);
            }
        } while (itemPosition.size() < NUM_OF_ITEM);

        items = new ArrayList<>();
        generateItems();
    }
}
