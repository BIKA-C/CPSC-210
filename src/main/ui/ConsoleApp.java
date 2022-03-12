package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.json.JSONException;

import model.Game;
import model.item.Item;
import model.maze.Maze;
import model.player.Inventory;
import model.player.Player;
import model.utility.Coordinate;
import model.utility.Direction;
import model.utility.menu.Menu;
import model.utility.menu.MenuOption;
import model.utility.menu.MessageBoxOption;
import model.utility.pixel.Pixel;
import model.utility.pixel.TextAttribute;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.console.Screen;
import ui.console.Terminal;
import ui.exceptions.BagIsFullException;
import ui.exceptions.FileOverLimitException;
import ui.exceptions.NotRecognizedKeyException;

// ConsoleApp represents the maze in the console
// it processes the game logics.
// it handles key input and graphics
public class ConsoleApp {
    private Game game;
    private Terminal termial;
    private boolean quit;

    private Player player;
    private Inventory playerInventory;
    private Coordinate playerPos;
    private String gameLoaded;

    private final Screen screen;
    private final int screenWidth;
    private final int screenHeight;

    private final Menu mainMenu;
    private final Menu fileMenu;

    private final JsonWriter writer;
    private final JsonReader reader;
    private final DateTimeFormatter dtf;

    private final TextAttribute wallStyle = new TextAttribute(4, TextAttribute.DEFAULT_VALUE, 0);
    private final TextAttribute exitStyle = new TextAttribute(9, TextAttribute.DEFAULT_VALUE, 0);
    private final TextAttribute playerStyle = new TextAttribute(10, TextAttribute.DEFAULT_VALUE, 0);
    private final TextAttribute itemStyle = new TextAttribute(11, TextAttribute.DEFAULT_VALUE, 0);
    private final TextAttribute messageStyle = new TextAttribute(6, TextAttribute.DEFAULT_VALUE, 0);
    private final Pixel wallPixel = new Pixel('█', wallStyle);
    private final Pixel exitPixel = new Pixel('⬤', exitStyle);
    private final Pixel playerPixel = new Pixel('⬤', playerStyle);
    private final Pixel itemPixel = new Pixel('⬤', itemStyle);

    public static final int WIDTH = 38;
    public static final int HEIGHT = WIDTH;
    public static final int INFO_PANNEL_START_X = (WIDTH + 3) * 2;
    public static final int INFO_PANNEL_WIDTH = 30;
    public static final int INFO_START_LINE = Math.toIntExact(Math.round(HEIGHT * 0.15));

    public static final int TERMINAL_GUI_NUM_RESTRICT = 9;
    public static final String DATA_STORAGE = "./data/";
    public static final String FILE_EXTENSION = ".json";

    // EFFECTS: constructs a consoleApp with all elements initialized
    public ConsoleApp() {
        quit = false;
        screenWidth = WIDTH + INFO_PANNEL_WIDTH;
        screenHeight = HEIGHT;

        termial = new Terminal(screenWidth, screenHeight);

        screen = termial.getScreen();

        writer = new JsonWriter();
        reader = new JsonReader();
        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        gameLoaded = null;

        mainMenu = new Menu("A Friendly Maze Game", new ArrayList<MenuOption>(
                Arrays.asList(MenuOption.NEWGAME, MenuOption.LOADGAME, MenuOption.QUIT)));

        fileMenu = new Menu("", new ArrayList<>());
    }

    // MODIFIES: this
    // EFFECTS: create a new game from newGame if newGame is not null
    // otherwise, game will be the newGame
    // link all the references
    private void init(Game newGame) {
        game = newGame == null ? new Game(WIDTH, HEIGHT) : newGame;
        player = game.getPlayer();
        playerInventory = player.getInventory();
        playerPos = player.getPosition();
    }

    // MODIFIES: this
    // EFFECTS: start the app
    public void start() {
        screen.setCursorInvisible();

        menu(mainMenu, 0, (screenWidth * 2 - 18) / 2 - 4, (screenHeight - 3 + 1) / 3 - 1);

        okMessageBox("Closing the Program", "Thanks for playing! Have a good day!");
        okMessageBox("Bye", "A Game made by William Chen");

        termial.close();
    }

    // MODIFIES: this
    // EFFECTS: start the game
    private void startGame() {
        quit = false;
        render(true);
        while (!quit) {
            if (!termial.isKeyDown()) {
                continue;
            }
            handleKeyDown();
            if (!quit) {
                render(true);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: render all the elements to the screen if renderNow is true
    // otherwise, write to the buffer
    private void render(boolean renderNow) {
        drawInfoPannel();
        drawMaze();
        drawItems();
        drawPlayer();
        if (renderNow) {
            screen.render();
        }
    }

    // MODIFIES: this
    // EFFECTS: draw (or actuall write) the info panel to the screen buffer
    private void drawInfoPannel() {
        int line = INFO_START_LINE;
        screen.write("This is a friendly maze solving game", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT,
                false, true);
        screen.write("You have solved " + player.getSolved(), INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false,
                true);

        line++;
        line = drawInventory(line);

        line++;
        line = drawInfo(line);

        line++;
        line = drawMessage(line);

        line++;
        screen.write("WASD to move, Q to quit, 1-9 to use items", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT,
                false, true);
    }

    // REQUIRES: line >= 0 && line < screenHeight
    // MODIFIES: this, line
    // EFFECTS: draw (or actuall write) the message section of the
    // info panel to the screen buffer
    private int drawMessage(int line) {
        screen.write("Message: ", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        screen.write(game.getGameMessage(), INFO_PANNEL_START_X, line++, messageStyle, false, true);
        return line;
    }

    // REQUIRES: line >= 0 && line < screenHeight
    // MODIFIES: this, line
    // EFFECTS: draw (or actuall write) the game info section of the
    // info panel to the screen buffer
    private int drawInfo(int line) {
        String info;
        screen.write("Info: ", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "Direction: " + player.getDirection();
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "Position: " + playerPos;
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        return line;
    }

    // REQUIRES: line >= 0 && line < screenHeight
    // MODIFIES: this, line
    // EFFECTS: draw (or actuall write) the inventory section of the
    // info panel to the screen buffer
    private int drawInventory(int line) {
        String info;
        screen.write("Inventory: ", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "Coins: " + playerInventory.getCoins();
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "You have " + playerInventory.getInventorySize() + "/9 items:";
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        for (int i = 0; i < playerInventory.getInventorySize(); i++) {
            if (INFO_START_LINE + 15 + i + 1 == HEIGHT) {
                screen.write("    ...", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
                return line;
            }
            info = "    " + (i + 1) + " - " + playerInventory.getItem(i).getDisplayName();
            screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        }
        return line;
    }

    // MODIFIES: this
    // EFFECTS: draw (or actuall write) the maze to the screen buffer
    private void drawMaze() {
        Maze maze = game.getMaze();
        drawMazeBorder();
        Coordinate screenPos;
        Coordinate pos;
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                pos = new Coordinate(j, i);
                screenPos = toScreen(pos);
                if (i == maze.getExit().getY() && j == maze.getExit().getX()) {
                    screen.writePixel(exitPixel, screenPos, false);
                } else if (!maze.isWall(pos)) {
                    screen.writePixel(Pixel.EMPTY_PIXEL, screenPos, true);
                } else {
                    screen.writePixel(wallPixel, screenPos, true);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draw (or actuall write) the maze boarders to the screen buffer
    private void drawMazeBorder() {
        Coordinate upper = new Coordinate(0, 0);
        Coordinate bottom = new Coordinate(0, WIDTH - 1);
        for (int i = 0; i < WIDTH; i++) {
            screen.writePixel(wallPixel, upper, true);
            screen.writePixel(wallPixel, bottom, true);
            upper.goRight(1);
            bottom.goRight(1);
        }

        Coordinate left = new Coordinate(0, 0);
        Coordinate right = new Coordinate(WIDTH - 1, 0);
        for (int i = 0; i < HEIGHT; i++) {
            screen.writePixel(wallPixel, left, true);
            screen.writePixel(wallPixel, right, true);
            left.goDown(1);
            right.goDown(1);
        }
    }

    // MODIFIES: this
    // EFFECTS: draw (or actuall write) all the available items from the maze to the screen buffer
    private void drawItems() {
        Coordinate screenPos;
        for (Map.Entry<Coordinate, Item> entry : game.getItemEntrySet()) {
            screenPos = toScreen(entry.getKey());
            screen.writePixel(itemPixel, screenPos, false);
        }

    }

    // MODIFIES: this
    // EFFECTS: draw (or actuall write) the player to the screen buffer
    private void drawPlayer() {
        Coordinate pos = toScreen(playerPos);
        screen.writePixel(playerPixel, pos, false);
    }

    // MODIFIES: this
    // EFFECTS: response to all the user's key input, and update game logics
    private void handleKeyDown() {
        int key = termial.getKey();
        char c = (char) key;

        if (c == 'q') {
            askForSaveBeforeQuit();
            return;
        } else if (isDirectionKey(c)) {
            Direction dir = keyToDirection(c);
            tryMove(dir);
        } else if (key <= 57 && key >= 49) {
            tryApply(key - 48 - 1);
        } else {
            return;
        }

        handleItem();
        handleExit();
    }

    // MODIFIES: this
    // EFFECTS: pop a message box to ask if the use want to save the game
    // close the message box only if a valid input is received
    private void askForSaveBeforeQuit() {
        render(false);
        screen.writeMessageBox("Quit", "Do you want to save the game?", MessageBoxOption.YES_NO_CANCEL, WIDTH / 2,
                HEIGHT / 3 + 3);
        screen.render();

        while (true) {
            if (!termial.isKeyDown()) {
                continue;
            }
            char key = (char) termial.getKey();
            boolean finish = handleYesNoCancel(key);
            if (finish) {
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: pop a ok message box at the center of the game if a game is running
    // otherwise it will be located at the center of the screen. wait until enter is presses
    private void okMessageBox(String title, String message) {
        int x = (screenWidth * 2 - message.length() - 10) / 2;
        if (game != null && !quit) {
            render(false);
            x = (WIDTH * 2 - message.length() - 10) / 2;
            x = x < 0 ? (screenWidth * 2 - message.length() - 10) / 2 : x;
        }
        screen.writeMessageBox(title, message, MessageBoxOption.OK, x,
                HEIGHT / 3 + 3);
        screen.render();
        while (true) {
            if (!termial.isKeyDown()) {
                continue;
            }
            int key = (char) termial.getKey();
            if (key == 13) {
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: handle the ask for save key input
    // if key is y, it will try to save the file, and quit the game
    // and return true to indicate a valid key is received
    // if saving is failed, okMessageBox will be poped, and the game will
    // continue
    // if key is c, return true to indicate a valid key is received
    // if key is n, return true to indicate a valid key is received, and
    // quit the game
    private boolean handleYesNoCancel(char key) {
        if (key == 'c') {
            return true;
        } else if (key == 'y') {
            quit = true;
            try {
                saveFile();
                // todo updateFileMenuOptions();
                gameLoaded = null;
            } catch (FileNotFoundException e) {
                quit = false;
                okMessageBox(e.toString().split(":")[0], e.getMessage());
                okMessageBox("Warning", "File not saved");
            } catch (FileOverLimitException e) {
                quit = false;
                okMessageBox("File Not Saved", "You can only save upto " + (TERMINAL_GUI_NUM_RESTRICT - 1) + " games");
            }
            return true;
        } else if (key == 'n') {
            quit = true;
            gameLoaded = null;
            return true;
        }
        return false;
    }

    // EFFECTS: return a string of the local time
    // in the form of yyyy-mm-dd hh:mm:ss
    private String getTimeStamp() {
        return dtf.format(LocalDateTime.now());
    }

    // REQUIRES: isDirectionKey(c)
    // EFFECTS: convert c to a diretion:
    // w to up
    // a to left
    // s to down
    // d to right
    private Direction keyToDirection(char c) {
        switch (c) {
            case 'w':
                return Direction.UP;
            case 'a':
                return Direction.LEFT;
            case 's':
                return Direction.DOWN;
            case 'd':
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    // EFFECTS: true is c is one of the folling:
    // w, a, s, d
    // false otherwise
    private boolean isDirectionKey(char c) {
        switch (c) {
            case 'w':
            case 'a':
            case 's':
            case 'd':
                return true;
            default:
                return false;
        }
    }

    // EFFECTS: convert the pos to the screen pos
    // used to map the mazea and player positins to the
    // actuall screen position
    private Coordinate toScreen(Coordinate pos) {
        Coordinate screenPos = new Coordinate(pos.getX(), pos.getY());
        screenPos.increaseXY(1, 1);
        return screenPos;
    }

    // MODIFIES: this
    // EFFECTS: try move the player 1 unit along the direction
    // if the movement will cause the player to be out of the
    // boundary of the game or the destination is a wall
    // the function will only update the player's direction to the
    // given direction and the movement will not occur
    private void tryMove(Direction direction) {
        Maze maze = game.getMaze();
        Coordinate pos = new Coordinate(playerPos.getX(), playerPos.getY());
        pos.go(direction, 1);
        if (!maze.isInRange(pos) || maze.isWall(pos)) {
            player.setDirection(direction);
            return;
        }
        player.move(direction);
    }

    // MODIFIES: this
    // EFFECTS: get the item from the player's inventory bag by
    // the index and apply it. Then the item will be removed
    // from the player's inventory
    // if such index does not exist, the function will do nothing
    // i.e. user does not have this indexed item in the bag
    private void tryApply(int index) {
        Item item = playerInventory.getItem(index);
        try {
            item.apply(game);
            playerInventory.removeItem(index);
        } catch (NullPointerException e) {
            return;
        }
    }

    // MODIFIES: this
    // EFFECTS:
    // if there is an item at the player's position,
    // the flowing will happen:
    // 1.
    // if item.isAutoApply() then the item's effect will immedately be applied.
    //
    // 2.
    // if !item.isAutoApply() the item will be added to the inventory bag iff
    // plaeryInventory.getInventorySize < TERMINAL_GUI_NUM_RESTRICT
    // and then the item's report will be updated through game.getGameMessage().
    // if the item is not added to the player's inventory, "Your bad is full..."
    // will be reported through game.getGameMessage()
    //
    // 3.
    // this item will be removed from map/maze
    //
    // if there is no item at the player's position, the function
    // do nothing
    private void handleItem() {
        if (!game.isItem(playerPos)) {
            return;
        }
        Item item = game.getItem(playerPos);
        if (item.isAutoApply()) {
            item.apply(game);
            game.removeItem(playerPos);
            return;
        }

        try {
            addItemToPlayerInventory(item);
            game.setGameMessage(item.report());
            game.removeItem(playerPos);
        } catch (BagIsFullException e) {
            game.setGameMessage("Your bag is full...");
        }
    }

    // MODIFIES: this
    // EFFECTS: try to add the item to the player inventory
    // if playerInventory.getInventorySize() >= TERMINAL_GUI_NUM_RESTRICT
    // then throws BagIsFullException and the item will not be added
    private void addItemToPlayerInventory(Item item) throws BagIsFullException {
        if (playerInventory.getInventorySize() >= TERMINAL_GUI_NUM_RESTRICT) {
            throw new BagIsFullException();
        }
        playerInventory.addItem(item);
    }

    // MODIFIES: this
    // EFFECTS: if game.isEnded()
    // then the game goes to the next maze
    // with a new maze initialized
    // and game.getReward() will be added to the player's'
    // inventory bad.
    // the reward message will be updated through
    // game.getGameMessage()
    private void handleExit() {
        if (game.isEnded()) {
            game.nextLevel(false);
        }
    }

    // REQUIRES: x > 0 && the longest string in menu.getOptions() + 10 + x < screen.getWidth()
    // y > 0 && menu.getOptions().size() + y + 5 < screen().getHeight()
    // MODIFIES: this
    // EFFECTS: a menu loop, display the given the menu and wait until an option is selected
    private void menu(Menu menu, int defualtSelect, int x, int y) {
        int current = defualtSelect;

        String[] stringOptions = menuOptionArrayToStrings(menu.getOptions());
        screen.writeMenu(x, y, menu.getTitle(), stringOptions, current);
        screen.render();
        while (!menu.isQuit()) {
            if (!termial.isKeyDown()) {
                continue;
            }

            try {
                current = processeKeyCommand(current, menu);
            } catch (NotRecognizedKeyException e) {
                continue;
            }

            if (menu == fileMenu) {
                stringOptions = menuOptionArrayToStrings(menu.getOptions());
            }
            screen.writeMenu(x, y, menu.getTitle(), stringOptions, current);
            screen.render();
        }
    }

    // EFFECTS: process the key pressed.
    // If enter is pressed, call the corresponding function in response
    // if a valid number between 1 - menu.getOptions().size(), call the corresponding function in response
    // If w/s is pressed return the new seleced index (1-based).
    // If the new selected is out of index bound, it loops back
    private int processeKeyCommand(int current, Menu menu) throws NotRecognizedKeyException {
        int optionsSize = menu.getOptions().size();
        int save = handleMenuKeyDown(current, 1 + 48, optionsSize + 48);

        if (save == -1) {
            functionDispather(menu, current);
            return current;
        } else if (save >= 1 + 48 && save <= optionsSize + 48) {
            functionDispather(menu, save - 48 - 1);
            return save - 48 - 1;
        }

        return save;
    }

    // EFFECTS: converts ArrayList<MenuOption> to String[]
    private String[] menuOptionArrayToStrings(ArrayList<MenuOption> options) {
        String[] strings = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            strings[i] = options.get(i).getOption();
        }
        return strings;
    }

    // MODIFIES: this
    // EFFECTS: update the fileMenu options
    private void updateFileMenuOptions() {
        String[] files = reader.fileList(DATA_STORAGE, FILE_EXTENSION, true);
        ArrayList<MenuOption> options = fileMenu.getOptions();

        if (files.length == options.size() - 1) {
            return;
        }

        int difference = files.length - (options.size() - 1);

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOption() == files[i]) {
                continue;
            }
            if (i == options.size() - 1) {
                options.set(i, new MenuOption(files[i]));
            }
            options.get(i).setOption(files[i]);
        }

        if (difference < 0) {
            options.retainAll(options.subList(0, files.length - 1));
        } else {
            for (int i = options.size(); i < files.length; i++) {
                options.add(new MenuOption(files[i]));
            }
        }

        options.add(MenuOption.QUIT);
    }

    // REQUIRES: selected > 0 && seleced < menu.getOptions().size()
    // MODIFIES: this
    // EFFECTS: call seleced menu function
    private void functionDispather(Menu menu, int selected) {
        if (menu.getOptions().get(selected) == MenuOption.QUIT) {
            menu.setQuit(true);
        } else if (menu.getOptions().get(selected) == MenuOption.NEWGAME) {
            init(null);
            startGame();
        } else if (menu.getOptions().get(selected) == MenuOption.LOADGAME) {
            chooseSaveMenu();
        } else {
            loadGame(selected);
        }
    }

    // REQUIRES: selected > 0 && seleced < fileMenu.getOptions().size()
    // MODIFIES: this
    // EFFECTS: load the selected game
    private void loadGame(int selected) {
        String file = reader.getAlphaSortedFileUnderDirByIndex(DATA_STORAGE, FILE_EXTENSION, selected);
        try {
            init(reader.parseGame(DATA_STORAGE + file));
            gameLoaded = file;
            startGame();
        } catch (JSONException e) {
            okMessageBox(e.toString().split(":")[0], e.getMessage());
            okMessageBox("Failed to Load", file + " is corrupted");
        } catch (IOException e) {
            okMessageBox(e.toString().split(":")[0], e.getMessage());
            okMessageBox("Failed to Load", "IOException");
        }
    }

    // MODIFIES: this
    // EFFECTS: display the choose a game menu until an option is selected
    private void chooseSaveMenu() {
        updateFileMenuOptions();
        String title = "You have " + (fileMenu.getOptions().size() - 1) + "/" + (TERMINAL_GUI_NUM_RESTRICT - 1)
                + " files";
        fileMenu.setTitle(title);
        fileMenu.setQuit(false);
        menu(fileMenu, 0, (screenWidth * 2 - 21) / 2 - 3, (screenHeight - 3 + 1) / 3 - 1);
    }

    // REQUIRES: current + 48 > min && current + 48 < max
    // EFFECTS: if w is pressed return current - 1 or max - 48 -1 if current -1 < 0
    // if s is pressed return current + 1 or 0 if current + 1 > max - 48 -1
    // if any number key between min and max (inclusive) is pressed, return that number + 48
    // if enter is pressed return -1
    // if any other key is pressed, throws NotRecognizedKeyException
    private int handleMenuKeyDown(int current, int min, int max) throws NotRecognizedKeyException {
        char key = (char) termial.getKey();
        int save = current;
        if (key >= min && key <= max) {
            return key;
        } else if (key == 'w') {
            current = current - 1 >= 0 ? current - 1 : max - 48 - 1;
        } else if (key == 's') {
            current = current + 1 <= max - 48 - 1 ? current + 1 : 0;
        } else if ((int) key == 13) {
            return -1;
        }

        if (current == save) {
            throw new NotRecognizedKeyException();
        }

        return current;
    }

    // EFFECTS: save the file to the DATA_STORAGE
    // if the destination is not found, throws FileNotFoundException
    // if the number of the files already saved under DATA_STORAGE folder is
    // greater than TERMINAL_GUI_NUM_RESTRICT - 2, throws FileOverLimitException
    private void saveFile() throws FileNotFoundException, FileOverLimitException {
        if (gameLoaded != null) {
            writer.saveToFile(DATA_STORAGE + gameLoaded, game);
            return;
        }
        if (reader.fileCount(DATA_STORAGE, FILE_EXTENSION) > TERMINAL_GUI_NUM_RESTRICT - 2) {
            throw new FileOverLimitException();
        }
        writer.saveToFile(DATA_STORAGE + getTimeStamp() + ".json", game);
    }

    // private void fatelClose(String reason, Exception e) {
    //     termial.close();
    //     System.out.println("Fatel: " + reason);
    //     System.out.println("Program closed due to: " + e.getMessage());
    //     System.out.println("\nStack Trace:");
    //     e.printStackTrace();
    //     System.exit(1);
    // }
}