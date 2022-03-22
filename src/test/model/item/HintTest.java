package model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestHelpers;

public class HintTest extends TestHelpers {

    private Hint hint;

    @BeforeEach
    public void setup() {
        game = new Game(20);
        hint = new Hint();
    }

    @Test
    public void applyTest() {
        makeCopyOfGame();

        hint.apply(game);
        assertGameItemsNotChanged();
        assertPlayerSolvedNotChanged();
        assertMazeNotChanged();
        assertPlayerNotChanged();
        assertEquals("Exit Position: " + game.getMaze().getExit(), game.getGameMessage());

        makeCopyOfGame();

        hint.apply(game);
        assertGameItemsNotChanged();
        assertPlayerSolvedNotChanged();
        assertMazeNotChanged();
        assertPlayerNotChanged();
        assertEquals("Exit Position: " + game.getMaze().getExit(), game.getGameMessage());
    }

    @Test
    public void isAutoApplyTest() {
        assertFalse(hint.isAutoApply());
    }

    @Test
    public void getNameTest() {
        assertEquals("Hint", hint.getDisplayName());
    }

    @Test
    public void reportTest() {
        assertEquals("You got a Hint", hint.report());
    }

    @Test
    public void toJsonStringTest() {
        assertEquals(hint.toJson().toString(), hint.toJSONString());
    }
}
