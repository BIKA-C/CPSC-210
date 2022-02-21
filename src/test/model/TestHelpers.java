package model;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.item.Item;
import model.maze.Maze;
import model.utility.Coordinate;
import model.utility.Direction;

public class TestHelpers {
    protected Game game;

    protected boolean[][] saveMaze;
    protected Coordinate saveStart;
    protected Coordinate saveExit;

    protected int saveNumOfItem;
    protected HashMap<Coordinate, Item> saveItemsMap;

    protected String saveGameMessage;
    protected int saveSolved;

    protected ArrayList<Item> savePlayerInventory;
    protected int savePlayerCoins;

    protected Coordinate savePlayerPos;
    protected Direction savePlayerDir;

    // REQUIRES: game != null
    public void makeCopyOfGame() {
        Coordinate coord = game.getPlayer().getPosition();
        // I have to basically make a copy of the game
        Maze maze = game.getMaze();
        saveMaze = copyMaze(game.getMaze());
        saveStart = new Coordinate(maze.getStart().getX(), maze.getStart().getY());
        saveExit = new Coordinate(maze.getExit().getX(), maze.getExit().getY());

        saveNumOfItem = game.getNumOfItems();
        saveItemsMap = copyItemsMap(game);
        // saveItems = copyItemList(game);
        // saveItemPositions = copyItemPositions(game);

        saveGameMessage = game.getGameMessage();
        saveSolved = game.getPlayer().getSolved();

        savePlayerPos = new Coordinate(coord.getX(), coord.getY());
        savePlayerDir = game.getPlayer().getDirection();

        savePlayerInventory = copyPlayerInventory();
        savePlayerCoins = game.getPlayer().getInventory().getCoins();
    }

    // REQUIRES: a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight()
    // EFFECTS: return true if maze a and b are the same, false if not
    public boolean isSameMaze(Maze a, boolean[][] b) {
        Coordinate coord;
        for (int i = 0; i < a.getHeight(); i++) {
            for (int j = 0; j < a.getWidth(); j++) {
                coord = new Coordinate(j, i);
                if (b[i][j] != !a.isWall(coord)) {
                    return false;
                }
            }
        }
        return true;
    }

    // REQUIRES: a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight()
    // EFFECTS: assert if two mazes are the same, and at the except point will be
    // ignored
    public void isSameMazeExcept(Maze a, boolean[][] b, Coordinate except) {
        Coordinate coord;
        for (int i = 0; i < a.getHeight(); i++) {
            for (int j = 0; j < a.getWidth(); j++) {
                coord = new Coordinate(j, i);
                if (i == except.getY() && j == except.getX()) {
                    continue;
                }
                assertEquals(b[i][j], !a.isWall(coord));
            }
        }
    }

    // EFFECTS: copy the maze and return it
    public boolean[][] copyMaze(Maze maze) {
        boolean[][] copy = new boolean[maze.getHeight()][maze.getWidth()];

        Coordinate coord;
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                coord = new Coordinate(j, i);
                if (maze.isWall(coord)) {
                    copy[i][j] = false;
                } else {
                    copy[i][j] = true;
                }
            }
        }

        return copy;
    }

    // REQUIRES: g != null
    // EFFECTS: make a deep copy of the ItemList
    public HashMap<Coordinate, Item> copyItemsMap(Game game) {
        HashMap<Coordinate, Item> copy = new HashMap<>();
        Iterator<Coordinate> keys = game.getItemPositionIterator();
        while (keys.hasNext()) {
            Coordinate coord = keys.next();
            Coordinate save = new Coordinate(coord.getX(), coord.getY());
            copy.put(save, game.getItem(save));
        }
        return copy;
    }

    // REQUIRES: g != null
    // EFFECTS: make a deep copy of the player inventory list
    public ArrayList<Item> copyPlayerInventory() {
        ArrayList<Item> copy = new ArrayList<>();
        for (int i = 0; i < game.getPlayer().getInventory().getInventorySize(); i++) {
            copy.add(game.getPlayer().getInventory().getItem(i));
        }
        return copy;
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert all game items not changed (items and their positins)
    public void assertGameItemsNotChanged() {
        assertEquals(saveNumOfItem, game.getNumOfItems());
        Iterator<Coordinate> keys = game.getItemPositionIterator();
        Iterator<Coordinate> savedKeys = saveItemsMap.keySet().iterator();
        while (keys.hasNext()) {
            Coordinate coord = keys.next();
            Coordinate saved = savedKeys.next();
            assertEquals(saveItemsMap.get(coord).getDisplayName(), game.getItem(coord).getDisplayName());
            assertTrue(saveItemsMap.containsKey(coord));
            assertTrue(game.isItem(saved));
        }
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert game's solved counter is not changes
    public void assertPlayerSolvedNotChanged() {
        assertEquals(saveSolved, game.getPlayer().getSolved());
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert all player inventory items not changed
    public void assertPlayerInventoryAndCoinsNotChanged() {
        for (int i = 0; i < game.getPlayer().getInventory().getInventorySize(); i++) {
            assertEquals(savePlayerInventory.get(i).getDisplayName(),
                    game.getPlayer().getInventory().getItem(i).getDisplayName());
            assertSame(savePlayerInventory.get(i), game.getPlayer().getInventory().getItem(i));
        }
        assertEquals(savePlayerCoins, game.getPlayer().getInventory().getCoins());
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert all player inventory items not changed
    public void assertPlayerInventoryNotChanged() {
        for (int i = 0; i < game.getPlayer().getInventory().getInventorySize(); i++) {
            assertEquals(savePlayerInventory.get(i).getDisplayName(),
                    game.getPlayer().getInventory().getItem(i).getDisplayName());
            assertSame(savePlayerInventory.get(i), game.getPlayer().getInventory().getItem(i));
        }
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert player position and direction is not changed
    public void assertPlayerPositionAndDirectionNotChanged() {
        assertTrue(game.getPlayer().getPosition().isSame(savePlayerPos));
        assertEquals(savePlayerDir, game.getPlayer().getDirection());
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert player is not changed, inventory, positin, and direction
    public void assertPlayerNotChanged() {
        assertTrue(game.getPlayer().getPosition().isSame(savePlayerPos));
        assertEquals(savePlayerDir, game.getPlayer().getDirection());

        assertPlayerInventoryAndCoinsNotChanged();
    }

    // REQUIRES: makeCopyOfGame() is called before this function is called
    // EFFECTS: assert the maze is not changed (all blocks, start pos, exit pos)
    public void assertMazeNotChanged() {
        assertTrue(isSameMaze(game.getMaze(), saveMaze));
        assertTrue(game.getMaze().getExit().isSame(saveExit));
        assertTrue(game.getMaze().getStart().isSame(saveStart));
    }
}
