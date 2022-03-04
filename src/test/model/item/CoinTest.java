package model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestHelpers;

public class CoinTest extends TestHelpers {

    private Coin coin;

    @BeforeEach
    public void setup() {
        game = new Game(20, 20);
        coin = new Coin(20);
    }

    @Test
    public void applyTest() {
        makeCopyOfGame();
        coin.apply(game);

        assertGameItemsNotChanged();
        assertPlayerSolvedNotChanged();
        assertMazeNotChanged();
        assertPlayerPositionAndDirectionNotChanged();

        assertEquals("You got 20 coins", game.getGameMessage());
        assertEquals(game.getPlayer().getInventory().getCoins(), 20);

        makeCopyOfGame();
        coin.apply(game);

        assertGameItemsNotChanged();
        assertPlayerSolvedNotChanged();
        assertMazeNotChanged();
        assertPlayerPositionAndDirectionNotChanged();

        assertEquals("You got 20 coins", game.getGameMessage());
        assertEquals(game.getPlayer().getInventory().getCoins(), 40);

    }

    @Test
    public void isAutoApplyTest() {
        assertTrue(coin.isAutoApply());
    }

    @Test
    public void getNameTest() {
        assertNull(coin.getDisplayName());
    }

    @Test
    public void reportTest() {
        assertNull(coin.report());
    }

    @Test
    public void toJsonStringTest() {
        assertEquals(coin.toJson().toString(), coin.toJSONString());
    }
}
