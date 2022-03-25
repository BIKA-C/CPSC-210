package persistence;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Game;
import model.item.Breaker;
import model.item.Coin;
import model.item.Hint;
import model.item.Item;
import model.item.ItemType;
import model.item.Skip;
import model.maze.Maze;
import model.player.Inventory;
import model.player.Player;
import model.utility.Coordinate;
import model.utility.Direction;

// a reader that provides some file/directory reading operation
public class JsonReader {

    // REQUIRES: fileName != null
    // EFFECTS: return the content of the given file as a string
    // if anything goes wrong, IOException will be thrown
    public String read(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        return content;
    }

    // REQUIRES: dir != null && extension != null
    // EFFECTS: retrun a list of alphabetically sorted filenames ending in extension
    // if trimExtension is true, the extension will be trimed
    public String[] fileList(String dir, String extension, boolean trimExtension) {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(extension);
            }
        };
        File[] list = new File(dir).listFiles(filter);
        String[] names = new String[list.length];

        for (int i = 0; i < list.length; i++) {
            names[i] = trimExtension ? list[i].getName().replace(extension, "") : list[i].getName();
        }

        Arrays.sort(names);

        return names;
    }

    // REQUIRES: dir != null && extension != null && index > 0 && index < fileCount(dir, extension)
    // EFFECTS: retrun the full file name under a alphabetically sorted directory by index
    public String getAlphaSortedFileUnderDirByIndex(String dir, String extension, int index) {
        return fileList(dir, extension, false)[index];
    }

    // REQUIRES: dir != null && extension != null
    // EFFECTS: return the number of files under a directory that ends wiht the given extension
    public int fileCount(String dir, String extension) {
        return fileList(dir, extension, false).length;
    }

    // EFFECTS: parse the json into a Game object
    // if anything goes wrong, JSONException will be thrown
    // if file can not open for read, IOException will be thrown
    public Game parseGame(String file) throws JSONException, IOException {
        JSONObject gameJson = new JSONObject(read(file));
        int width = gameJson.getInt("mazeSize");
        Game game = new Game(width);

        game.setGameMessage(gameJson.getString("gameMessage"));
        parsePlayer(game.getPlayer(), gameJson.getJSONObject("player"));
        parseGameItems(game, gameJson.getJSONArray("items"));
        parseMaze(game.getMaze(), gameJson.getJSONObject("maze"));

        return game;
    }

    // MODIFIES: maze
    // EFFECTS: parse the json and write it into maze
    private void parseMaze(Maze maze, JSONObject json) {
        maze.setStart(parseCoordinate(json.getJSONObject("start")));
        maze.setExit(parseCoordinate(json.getJSONObject("exit")));
        Coordinate coord = new Coordinate(0, 0);

        JSONArray mazeJson = json.getJSONArray("maze");
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                coord.setXY(j, i);
                maze.setBlock(coord, mazeJson.getJSONArray(i).getBoolean(j));
            }
        }

    }

    // MODIFIES: game
    // EFFECTS: parse the JSONArray items into game items
    private void parseGameItems(Game game, JSONArray items) {
        game.removeAllItemsOnMap();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            Coordinate pos = parseCoordinate(itemJson.getJSONObject("position"));
            Item item = parseItem(itemJson.getJSONObject("item"));
            game.setItem(pos, item);
        }
    }

    // MODIFIES: player
    // EFFECTS: parse the json and write it into player
    private void parsePlayer(Player player, JSONObject json) {
        player.setDirection(parseDirection(json.getString("direction")));
        player.setSolved(json.getInt("solved"));
        player.setPosition(parseCoordinate(json.getJSONObject("position")));
        parseInventory(player.getInventory(), json.getJSONObject("inventory"));
    }

    // MODIFIES: inventory
    // EFFECTS: parse the json and write it into inventory
    private void parseInventory(Inventory inventory, JSONObject json) {
        inventory.addCoins(json.getInt("coins"));

        JSONArray items = json.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            inventory.addItem(parseItem(items.getJSONObject(i)));
        }
    }

    // EFFECTS: parse the json into an Item and return it
    private Item parseItem(JSONObject json) {
        ItemType type = parseItemType(json.getString("type"));

        if (type == ItemType.COIN) {
            int value = json.getInt("value");
            return new Coin(value);
        } else if (type == ItemType.BREAKER) {
            int d = json.getInt("range");
            return new Breaker(d);
        } else if (type == ItemType.SKIP) {
            return new Skip();
        } else {
            return new Hint();
        }

    }

    // REQUIRES: type is contained in ItemType.values()
    // EFFECTS: parse the type into ItemType
    private ItemType parseItemType(String type) {
        return ItemType.valueOf(type);
    }

    // REQUIRES: dir is contained in Direction.values()
    // EFFECTS: parse the dir into Direction
    private Direction parseDirection(String dir) {
        return Direction.valueOf(dir);
    }

    // EFFECTS: parse the json into Coordiante
    private Coordinate parseCoordinate(JSONObject json) {
        return new Coordinate(json.getInt("x"), json.getInt("y"));
    }

}
