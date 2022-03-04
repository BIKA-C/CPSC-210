package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestHelpers;
import model.item.Item;
import model.utility.Coordinate;
import ui.ConsoleApp;

public class JsonWriterTest extends TestHelpers {
    private JsonWriter writer;
    Game game;

    @BeforeEach
    public void setup() {
        writer = new JsonWriter();
        game = new Game(ConsoleApp.WIDTH, ConsoleApp.HEIGHT);
    }

    @Test
    public void writeNonExistTest() {
        try {
            writer.saveToFile("abc/abc.json", game);
            fail("Expecting FileNotFoundException");
        } catch (FileNotFoundException e) {
            //expected
        }
    }

    @Test
    public void saveTest() {
        try {
            writer.saveToFile(ConsoleApp.DATA_STORAGE + "saveTest.json", game);
        } catch (FileNotFoundException e) {
            fail("Not expecting FileNotFoundException");
        }

        JsonReader reader = new JsonReader();
        try {
            Game saved = reader.parseGame(ConsoleApp.DATA_STORAGE + "saveTest.json");
            assertGameWrite(game, saved);
        } catch (JSONException e) {
            fail("File failed to write");
        } catch (IOException e) {
            fail("IOException");
        }
    }

    // EFFECTS: check if two games are the same
    private void assertGameWrite(Game expected, Game actual) {
        assertEquals(expected.getGameMessage(), actual.getGameMessage());
        assertTrue(isSameMaze(expected.getMaze(), actual.getMaze()));

        Iterator<Coordinate> expectedIt = expected.getItemPositionIterator();
        Iterator<Coordinate> actualIt = actual.getItemPositionIterator();
        while (expectedIt.hasNext() || actualIt.hasNext()) {
            Coordinate expectedItemPos = expectedIt.next();
            Coordinate actualItemPos = actualIt.next();
            assertTrue(expectedItemPos.isSame(actualItemPos));
            assertItemSame(expected.getItem(expectedItemPos), actual.getItem(actualItemPos));
        }

        assertTrue(expected.getPlayer().getPosition().isSame(actual.getPlayer().getPosition()));
        assertEquals(expected.getPlayer().getDirection(), actual.getPlayer().getDirection());
        assertEquals(expected.getPlayer().getSolved(), actual.getPlayer().getSolved());
        assertEquals(expected.getPlayer().getInventory().getCoins(), actual.getPlayer().getInventory().getCoins());
        assertEquals(expected.getPlayer().getInventory().getInventorySize(),
                actual.getPlayer().getInventory().getInventorySize());

        for (int i = 0; i < expected.getPlayer().getInventory().getInventorySize(); i++) {
            assertItemSame(expected.getPlayer().getInventory().getItem(i),
                    actual.getPlayer().getInventory().getItem(i));
        }
    }

    private void assertItemSame(Item expected, Item actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getDisplayName(), actual.getDisplayName());
        assertEquals(expected.isAutoApply(), actual.isAutoApply());
    }
}
