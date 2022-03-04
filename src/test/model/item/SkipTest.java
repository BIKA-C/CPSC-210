package model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestHelpers;

public class SkipTest extends TestHelpers {

    private Skip skip;

    @BeforeEach
    public void setup() {
        game = new Game(20, 20);
        skip = new Skip();
    }

    @Test
    public void applyTest() {
        makeCopyOfGame();

        skip.apply(game);
        assertPlayerInventoryAndCoinsNotChanged();
        assertPlayerSolvedNotChanged();
        assertFalse(isSameMaze(game.getMaze(), saveMaze));

        assertEquals("Maze skipped!", game.getGameMessage());

        makeCopyOfGame();

        skip.apply(game);
        assertPlayerInventoryAndCoinsNotChanged();
        assertPlayerSolvedNotChanged();
        assertFalse(isSameMaze(game.getMaze(), saveMaze));

        assertEquals("Maze skipped!", game.getGameMessage());
    }

    @Test
    public void isAutoApplyTest() {
        assertFalse(skip.isAutoApply());
    }

    @Test
    public void getNameTest() {
        assertEquals("Skip this maze", skip.getDisplayName());
    }

    @Test
    public void reportTest() {
        assertEquals("You got a Skip", skip.report());
    }

    @Test
    public void toJsonStringTest() {
        assertEquals(skip.toJson().toString(), skip.toJSONString());
    }
}
