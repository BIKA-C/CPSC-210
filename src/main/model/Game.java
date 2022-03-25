package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import model.item.Breaker;
import model.item.Coin;
import model.item.Hint;
import model.item.Item;
import model.item.ItemType;
import model.item.Skip;
import model.maze.Maze;
import model.player.Player;
import model.utility.Coordinate;

// Game represents this maze game
// it has a maze and player and all the game items on the maze
// it keeps tracks of how many maze player has solved.
// it has a list of items that are available on the map
// the list will be randomly re-generated for each maze
public class Game {

    private Maze maze;
    private Player player;
    private int reward;
    private int mazeSize;
    private HashMap<Coordinate, Item> itemMap;

    private String gameMessage;

    private int numOfItemOnMap;

    private Random random;

    public static final double REWARD_TO_MAZE_SIZE_RATIO = 0.09375;
    public static final double NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO = 0.05;

    // REQUIRES: size >= 8 && size % 2 == 0
    // EFFECTS: Constructs a new game object with a new player at 0,0
    // and a random maze. Solved counter will be zero.
    // reward and num of items will be calculated proportion to the given width and
    // height
    // by the REWARD_MAZE_SIZE_RATIO
    // The actual maze's width and height will minus 2 from
    // the given width and height. These 2 are reserved for boarders.
    // Boarders are ignored when calculating the reward and num of items
    public Game(int size) {
        maze = new Maze(size - 2, size - 2);
        player = new Player();
        random = new Random();
        itemMap = new HashMap<>();
        reward = Math.toIntExact(Math.round(maze.getHeight() * maze.getWidth() * REWARD_TO_MAZE_SIZE_RATIO));
        mazeSize = size;
        init();
    }

    // public Game() {
    //     this(10);
    //     mazeSize = 10;
    // }

    // EFFECTS: true if getPlayer.getPosition().isSame(getMaze().getExit())
    // false otherwise
    public boolean isEnded() {
        return player.getPosition().isSame(maze.getExit());
    }

    // REQUIRES: skip and increaseSize can't be both true
    // MODIFIES: this
    // EFFECTS: creates a new maze with the same width and height as the next level
    // if skip is false the following will happen:
    // add 1 to the player solved counter
    // add getReward() to the player's inventory
    // getGameMessage() will give the message of the reward earned
    public void nextLevel(boolean skip, boolean increaseSize) {
        if (increaseSize) {
            mazeSize += 2;
        }
        this.maze = new Maze(mazeSize - 2, mazeSize - 2);
        reward = Math.toIntExact(Math.round(maze.getHeight() * maze.getWidth() * REWARD_TO_MAZE_SIZE_RATIO));

        init();
        if (skip) {
            return;
        }
        player.solvedAddOne();
        gameMessage = "You have earned " + reward + " coins for solving the maze";
        player.getInventory().addCoins(reward);

    }

    // MODIFIES: this
    // EFFECTS: moves the player by the key
    // if the player arrives the exit, goes to the next level
    // if the player is at an item position, stores the item into the
    // player inventory bag
    // public void movePlayer(char key) throws PlayerMovementException {
    //     Direction direction;
    //     if (key == 'w') {
    //         direction = Direction.UP;
    //     } else if (key == 'a') {
    //         direction = Direction.LEFT;
    //     } else if (key == 's') {
    //         direction = Direction.DOWN;
    //     } else if (key == 'd') {
    //         direction = Direction.RIGHT;
    //     } else {
    //         throw new NotRecognizedKeyException();
    //     }

    //     Coordinate save = new Coordinate(player.getPosition());
    //     save.go(direction, 1);
    //     if (!maze.isInRange(save) || maze.isWall(save)) {
    //         throw new CollisionException();
    //     }

    //     player.move(direction);
    //     checkExit();
    //     checkItem();
    // }

    // EFFECTS: true if the given pos is an Item position
    public boolean isItem(Coordinate pos) {
        return itemMap.containsKey(pos);
    }

    // EFFECTS: return the number of items on the map/maze
    public int getNumOfItems() {
        return itemMap.size();
    }

    // EFFECTS: return the the entry set of the items
    public Set<Map.Entry<Coordinate, Item>> getItemEntrySet() {
        return itemMap.entrySet();
    }

    // EFFECTS: return the item by the given pos
    // if the pos if not in the list, null will be returned
    public Item getItem(Coordinate pos) {
        return itemMap.get(pos);
    }

    // MODIFIES: this
    // EFFECTS: add the item to the map with the corresponding pos
    // if the pos already has an item, nothing will happen
    public void setItem(Coordinate pos, Item item) {
        itemMap.put(pos, item);
    }

    public int getReward() {
        return reward;
    }

    // MODIFIES: this
    // EFFECTS: remove the item from the list by the key/pos
    public void removeItem(Coordinate pos) {
        // items.remove(index);
        // itemPosition.remove(index);
        itemMap.remove(pos);
    }

    // MODIFIES: this
    // EFFECTS: remove all the items on the map
    public void removeAllItemsOnMap() {
        itemMap.clear();
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

    public int getMazeSize() {
        return mazeSize;
    }

    // REQUIRES: items != null
    // MODIFIES: this
    // EFFECTS: randomly put different items to the items list
    private void generateItems() {
        int index;
        while (itemMap.size() < numOfItemOnMap) {
            index = random.nextInt(maze.getNumOfRoad());
            Coordinate road = maze.getRoad(index);
            if (itemMap.containsKey(road) || road.isSame(getMaze().getExit())) {
                continue;
            }

            index = random.nextInt(ItemType.values().length);
            ItemType type = ItemType.values()[index];

            if (type == ItemType.COIN) {
                itemMap.put(road, new Coin(random.nextInt(20) + 1));
            } else if (type == ItemType.BREAKER) {
                int d = random.nextInt(5) + 1;
                itemMap.put(road, new Breaker(d));
            } else if (type == ItemType.SKIP) {
                itemMap.put(road, new Skip());
            } else {
                itemMap.put(road, new Hint());
            }
        }
    }

    // REQUIRES: maze != null && player != null
    // MODIFIES: this
    // EFFECTS: init the maze. It does the following:
    // set the player's position to the maze's starting position
    // clear items' list (both items and itemPosition)
    // re-generate the items list (both items and itemPosition)
    // set gameMessage to be "New Maze !!!"
    private void init() {
        player.setPosition(maze.getStart());
        itemMap.clear();
        gameMessage = "New Maze !!!";
        numOfItemOnMap = Math.toIntExact(Math.round(maze.getNumOfRoad() * NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO));

        generateItems();
    }

    // private void checkExit() {
    //     if (!player.getPosition().isSame(maze.getExit())) {
    //         return;
    //     }
    //     nextLevel(false, true);
    // }

    // private void checkItem() {
    //     if (!itemMap.containsKey(player.getPosition())) {
    //         return;
    //     }
    //     player.getInventory().addItem(itemMap.get(player.getPosition()));
    //     itemMap.remove(player.getPosition());
    // }

    // EFFECTS: convert the game to a JSON object
    public JSONObject toJson() {
        JSONObject game = new JSONObject(this);
        game.remove("reward");
        game.remove("numOfItems");
        game.remove("ended");
        game.remove("itemEntrySet");
        game.put("maze", maze.toJson());
        game.put("player", player.toJson());
        game.put("items", itemMapToJsonArray());
        return game;
    }

    // EFFECTS: converts the itemMap to a JSONArray
    private JSONArray itemMapToJsonArray() {
        Iterator<Coordinate> keys = itemMap.keySet().iterator();
        JSONArray items = new JSONArray();
        while (keys.hasNext()) {
            JSONObject item = new JSONObject();
            Coordinate key = keys.next();
            item.put("position", key.toJson());
            item.put("item", itemMap.get(key).toJson());

            items.put(item);
        }

        return items;
    }

}
