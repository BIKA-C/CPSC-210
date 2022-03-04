package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.item.Item;
import model.maze.Maze;
import model.player.Inventory;
import model.player.Player;
import model.utility.Coordinate;
import ui.ConsoleApp;

public class JsonReaderTest {

    private JsonReader reader;

    @BeforeEach
    public void setup() {
        reader = new JsonReader();
    }

    @Test
    public void readNonExistTest() {
        try {
            reader.parseGame("abc.json");
            fail("Expecting IOException");
        } catch (JSONException e) {
            fail("Expecting IOException");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    public void readCorruptedTest() {
        try {
            reader.parseGame(ConsoleApp.DATA_STORAGE + "corrupted.json");
            fail("Expecting JSONException");
        } catch (JSONException e) {
            //expected
        } catch (IOException e) {
            fail("Expecting JSONException");
        }
    }

    @Test
    public void readRegular() {
        Game game;
        String file = ConsoleApp.DATA_STORAGE + "test1.json";
        JSONObject json = new JSONObject(readFile(file));
        try {
            game = reader.parseGame(file);
            assertEquals(json.getString("gameMessage"), game.getGameMessage());
            assertEquals(json.getInt("width"), game.getMaze().getWidth() + 2);
            assertEquals(json.getInt("height"), game.getMaze().getHeight() + 2);
            assertMazeRead(json.getJSONObject("maze"), game.getMaze());
            assertPlayerRead(json.getJSONObject("player"), game.getPlayer());
            assertGameItemRead(json.getJSONArray("items"), game);
        } catch (JSONException e) {
            fail("Not expectng JSONException");
        } catch (IOException e) {
            fail("Not expecting IOException");
        }
    }

    @Test
    public void fileListJsonNoTrimTest() {
        String[] list = reader.fileList(ConsoleApp.DATA_STORAGE, ".json", false);
        assertEquals(reader.fileCount(ConsoleApp.DATA_STORAGE, ".json"), list.length);

        for (int i = 0; i < list.length; i++) {
            assertTrue(list[i].endsWith(".json"));
        }
    }

    @Test
    public void fileListJsonTrimTest() {
        String[] list = reader.fileList(ConsoleApp.DATA_STORAGE, ".json", true);
        assertEquals(reader.fileCount(ConsoleApp.DATA_STORAGE, ".json"), list.length);

        for (int i = 0; i < list.length; i++) {
            assertFalse(list[i].endsWith(".json"));
        }
    }

    // EFFECTS: return the file content as a string
    // fail the test if failed reading
    private String readFile(String file) {
        try {
            return reader.read(file);
        } catch (IOException e) {
            fail("Cannot read the file");
            return null;
        }
    }

    // EFFECTS: assert maze has been properly read
    private void assertMazeRead(JSONObject jsonMaze, Maze maze) {
        assertCoordinate(jsonMaze.getJSONObject("start"), maze.getStart());
        assertCoordinate(jsonMaze.getJSONObject("exit"), maze.getExit());

        assertMazeArray(jsonMaze.getJSONArray("maze"), maze);
    }

    // EFFECTS: assert maze array has been properly read
    private void assertMazeArray(JSONArray jsonMaze, Maze maze) {
        Coordinate pos = new Coordinate(0, 0);
        for (int i = 0; i < jsonMaze.length(); i++) {
            for (int j = 0; j < jsonMaze.getJSONArray(i).length(); j++) {
                pos.setXY(j, i);
                assertEquals(jsonMaze.getJSONArray(i).getBoolean(j), !maze.isWall(pos));
            }
        }
    }

    // EFFECTS: assert a coordiante is the given pos
    private void assertCoordinate(JSONObject jsonPos, Coordinate pos) {
        assertEquals(jsonPos.getInt("x"), pos.getX());
        assertEquals(jsonPos.getInt("y"), pos.getY());
    }

    // EFFECTS: assert player has been properly read
    private void assertPlayerRead(JSONObject jsonPlayer, Player player) {
        assertEquals(jsonPlayer.getInt("solved"), player.getSolved());
        assertEquals(jsonPlayer.getString("direction"), player.getDirection().toString());
        assertCoordinate(jsonPlayer.getJSONObject("position"), player.getPosition());
        assertInventoryRead(jsonPlayer.getJSONObject("inventory"), player.getInventory());
    }

    // EFFECTS: assert inventory has been properly read
    private void assertInventoryRead(JSONObject jsonInventory, Inventory inventory) {
        assertEquals(jsonInventory.getInt("coins"), inventory.getCoins());
        JSONArray items = jsonInventory.getJSONArray("items");
        assertEquals(items.length(), inventory.getInventorySize());
        for (int i = 0; i < jsonInventory.length(); i++) {
            asserItem(items.getJSONObject(i), inventory.getItem(i));
        }
    }

    // EFFECTS: assert a json item is the given item
    private void asserItem(JSONObject jsonItem, Item item) {
        assertEquals(jsonItem.getBoolean("autoApply"), item.isAutoApply());
        assertEquals(jsonItem.getString("type"), item.getType().toString());
    }

    // EFFECTS: assert game items have been properly read
    private void assertGameItemRead(JSONArray jsonItems, Game game) {
        assertEquals(jsonItems.length(), game.getNumOfItems());
        Iterator<Coordinate> iterator = game.getItemPositionIterator();
        int i = 0;
        while (iterator.hasNext()) {
            Coordinate key = iterator.next();
            JSONObject jsonItemPack = jsonItems.getJSONObject(i);
            assertCoordinate(jsonItemPack.getJSONObject("position"), key);
            asserItem(jsonItemPack.getJSONObject("item"), game.getItem(key));
            i++;
        }
    }
}
