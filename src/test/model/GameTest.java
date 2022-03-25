package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.item.Item;
import model.utility.Coordinate;

public class GameTest extends TestHelpers {

    @BeforeEach
    public void setup() {
        game = new Game(20);
    }

    @Test
    public void constructorTest() {
        assertEquals(18, game.getMaze().getWidth());
        assertEquals(18, game.getMaze().getHeight());
        assertEquals(round((18 * 18) * Game.REWARD_TO_MAZE_SIZE_RATIO), game.getReward());
        assertEquals(round(game.getMaze().getNumOfRoad() * Game.NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO), game.getNumOfItems());

        assertNotNull(game.getMaze());
        assertNotNull(game.getPlayer());
        assertEquals("New Maze !!!", game.getGameMessage());
    }

    @Test
    public void isEndedTest() {
        assertFalse(game.isEnded());

        game.getPlayer().setPosition(game.getMaze().getExit());
        assertTrue(game.isEnded());
    }

    @Test
    public void nextLevelNonSkipTest() {
        for (int i = 0; i < 20; i++) {
            assertNextLevelNonSkip(i + 1);
        }
    }

    @Test

    public void nextLevelNonSkipTestIncreaseSize() {
        int coins = 0;
        for (int i = 0; i < 20; i++) {
            coins = assertNextLevelNonSkipIncrease(i + 1, coins);
        }
    }

    @Test
    public void nextLevelSkipTest() {
        for (int i = 0; i < 20; i++) {
            assertNextLevelSkip();
        }
    }

    // @Test
    // public void getItemPositions() {
    //     ArrayList<Coordinate> copyList = copyItemPositions(game);

    //     Coordinate returned = game.getItemPosition(0);
    //     assertTrue(returned.isSame(copyList.get(0)));
    //     assertSame(copyList.get(0), returned);
    // }

    @Test
    public void getItemTest() {
        HashMap<Coordinate, Item> copyList = copyItemsMap(game);
        Coordinate randomKey = game.getItemEntrySet().iterator().next().getKey();

        Item returned = game.getItem(randomKey);
        assertEquals(copyList.get(randomKey).getDisplayName(), returned.getDisplayName());
        assertSame(copyList.get(randomKey), returned);
        assertTrue(game.isItem(randomKey));
    }

    @Test
    public void removeItemTest() {
        HashMap<Coordinate, Item> copyList = copyItemsMap(game);
        Coordinate randomKey = game.getItemEntrySet().iterator().next().getKey();

        game.removeItem(randomKey);
        assertEquals(copyList.size() - 1, game.getNumOfItems());
        assertFalse(game.isItem(randomKey));
    }

    private void assertNextLevelSkip() {
        makeCopyOfGame();
        game.nextLevel(true, false);
        assertPlayerInventoryAndCoinsNotChanged();
        assertPlayerSolvedNotChanged();

        assertEquals(18, game.getMaze().getWidth());
        assertEquals(18, game.getMaze().getHeight());
        assertEquals(round((18 * 18) * Game.REWARD_TO_MAZE_SIZE_RATIO), game.getReward());
        assertEquals(round(game.getMaze().getNumOfRoad() * Game.NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO), game.getNumOfItems());

        assertNotNull(game.getMaze());

        assertEquals("New Maze !!!", game.getGameMessage());
    }

    private void assertNextLevelNonSkip(int counter) {
        makeCopyOfGame();
        game.nextLevel(false, false);
        assertPlayerInventoryNotChanged();

        assertEquals(18, game.getMaze().getWidth());
        assertEquals(18, game.getMaze().getHeight());
        assertEquals(round((18 * 18) * Game.REWARD_TO_MAZE_SIZE_RATIO), game.getReward());
        assertEquals(round(game.getMaze().getNumOfRoad() * Game.NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO), game.getNumOfItems());

        assertNotNull(game.getMaze());

        int reward = round((18 * 18) * Game.REWARD_TO_MAZE_SIZE_RATIO);
        assertEquals("You have earned " + reward + " coins for solving the maze", game.getGameMessage());
        assertEquals(counter, game.getPlayer().getSolved());
        assertEquals(reward * counter, game.getPlayer().getInventory().getCoins());
    }

    private int assertNextLevelNonSkipIncrease(int counter, int coinsBefore) {
        makeCopyOfGame();
        game.nextLevel(false, true);
        assertPlayerInventoryNotChanged();

        assertEquals(18 + counter * 2, game.getMaze().getWidth());
        assertEquals(18 + counter * 2, game.getMaze().getHeight());
        assertEquals(round(Math.pow(18 + counter * 2, 2) * Game.REWARD_TO_MAZE_SIZE_RATIO), game.getReward());
        assertEquals(round(game.getMaze().getNumOfRoad() * Game.NUM_OF_ITEMS_TO_MAZE_SIZE_RATIO), game.getNumOfItems());

        assertNotNull(game.getMaze());

        int reward = round(Math.pow(18 + counter * 2, 2) * Game.REWARD_TO_MAZE_SIZE_RATIO);
        assertEquals("You have earned " + reward + " coins for solving the maze", game.getGameMessage());
        assertEquals(counter, game.getPlayer().getSolved());
        assertEquals(reward + coinsBefore, game.getPlayer().getInventory().getCoins());
        return game.getPlayer().getInventory().getCoins();
    }

    private int round(double num) {
        return Math.toIntExact(Math.round(num));
    }
}
