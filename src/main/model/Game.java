package model;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.binding.NumberExpressionBase;
import model.item.Breaker;
import model.item.Coin;
import model.item.Hint;
import model.item.Item;
import model.item.Items;
import model.item.Skip;
import model.maze.Maze;
import model.player.Player;
import model.utility.Coordinate;

// Game represents this maze game
// it has a maze and player and all the game items on the maze
// it keep tracks of how many maze player has solved.
// it has a list of items that are available on the map
// the list will be randomly re-generated for each maze
public class Game {

    private Maze maze;
    private Player player;
    private int solved;
    private int reward;
    private ArrayList<Coordinate> itemPosition;
    private ArrayList<Item> items;

    private String gameMessage;

    private int numOfItemOnMap;

    private Random random;

    public static final double REWARD_TO_MAZE_SIZE_RATIO = 0.09375;
    public static final double NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO = 0.05;

    // REQUIRES: (width % 2 == 0) and (height % 2 == 0) and (width and height) >= 8
    // EFFECTS: Constructs a new game object with a new player at 0,0
    // and a random maze. Solved counter will be zero.
    // reward and num of items will be calucated proportion to the given width and height
    // by the REWARD_MAZE_SIZE_RATIO
    // The actual maze's width and height will minus 2 from the
    // the given width and height. These 2 are reserved for boarders.
    // Boarders are ignored when calculating the reward and num of items
    public Game(int width, int height) {
        maze = new Maze(width - 2, height - 2);
        player = new Player();
        random = new Random();
        itemPosition = new ArrayList<>();
        items = new ArrayList<>();
        solved = 0;
        reward = Math.toIntExact(Math.round(maze.getHeight() * maze.getWidth() * REWARD_TO_MAZE_SIZE_RATIO));

        init();
    }

    // EFFECTS: true if getPlayer.getPosition().isSame(getMaze().getExit())
    // false otherwise
    public boolean isEnded() {
        return player.getPosition().isSame(maze.getExit());
    }

    // MODIFIES: this
    // EFFECTS: creates a new maze with the same width and height as the next level
    // if skip is false the following will happen:
    // add 1 to the solved counter
    // add getReward() to the player's inventory
    // getGameMessage() will give the message of the reward earned
    public void nextLevel(boolean skip) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        this.maze = new Maze(width, height);

        init();
        if (skip) {
            return;
        }
        solved++;
        gameMessage = "You have earned " + reward + " coins for solving the maze";
        player.getInventory().addCoins(reward);
    }

    // EFFECTS: return the number of items on the map/maze
    public int getNumOfItems() {
        return items.size();
    }

    // REQUIRES: index >= 0 && index < getItemSize()
    // EFFECTS: return the poisition of the item
    // by the given index
    public Coordinate getItemPosition(int index) {
        return itemPosition.get(index);
    }

    // REQUIRES: index >= 0 && index < getItemSize()
    // EFFECTS: return the item by the given index
    public Item getItem(int index) {
        return items.get(index);
    }

    public int getReward() {
        return reward;
    }

    // REQUIRES: index >= 0 && index < getNumOfItems()
    // MODIFIES: this
    // EFFECTS: remove the item from the list by the index
    public void removeItem(int index) {
        items.remove(index);
        itemPosition.remove(index);
    }

    public Maze getMaze() {
        return maze;
    }

    public Player getPlayer() {
        return player;
    }

    public String getGameMessage() {
        return gameMessage;
    }

    public void setGameMessage(String message) {
        this.gameMessage = message;
    }

    public int getSolved() {
        return solved;
    }

    // REQUIRES: items != null
    // MODIFIES: this
    // EFFECTS: randomly put different items to the items list
    private void generateItems() {
        int index;
        for (int i = 0; i < numOfItemOnMap; i++) {
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

    // REQUIRES: maze != null && player != null
    // MODIFIES: this
    // EFFECTS: init the maze. It dows the folling:
    // set the player's poistion to the maze's starting position
    // clear items' list (both items and itemPosition)
    // re-generate the items list (both items and itemPosition)
    // set gameMessage to be "New Maze !!!"
    private void init() {
        player.setPosition(maze.getStart());
        itemPosition.clear();
        gameMessage = "New Maze !!!";
        numOfItemOnMap = Math.toIntExact(Math.round(maze.getNumOfRoad() * NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO));

        int index;
        do {
            index = random.nextInt(maze.getNumOfRoad());
            Coordinate road = maze.getRoad(index);
            if (!itemPosition.contains(road)) {
                itemPosition.add(road);
            }
        } while (itemPosition.size() < numOfItemOnMap);

        items.clear();
        generateItems();
    }
}
