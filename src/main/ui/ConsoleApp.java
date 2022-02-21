package ui;

import java.io.IOException;
import java.util.Iterator;

import model.Game;
import model.item.Item;
import model.maze.Maze;
import model.player.Inventory;
import model.player.Player;
import model.utility.Coordinate;
import model.utility.Direction;
import model.utility.pixel.Pixel;
import model.utility.pixel.TextAttribute;
import ui.console.Screen;
import ui.console.Terminal;

// ConsoleApp represents the maze in the console
// it processes the game logics.
// it handles key input and graphics
public class ConsoleApp {
    private Game game;
    private Terminal termial;
    private boolean quit;

    private final Screen screen;

    private final Player player;
    private final Inventory playerInventory;
    private final Coordinate playerPos;

    private final int screenWidth;
    private final int screenHeight;

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

    // EFFECTS: constructs a consoleApp with all elements initialized
    public ConsoleApp() throws InterruptedException, IOException {
        quit = false;
        screenWidth = WIDTH + INFO_PANNEL_WIDTH;
        screenHeight = HEIGHT;

        game = new Game(WIDTH, HEIGHT);
        termial = new Terminal(screenWidth, screenHeight);

        screen = termial.getScreen();

        player = game.getPlayer();
        playerInventory = player.getInventory();
        playerPos = player.getPosition();
    }

    // MODIFIES: this
    // EFFECTS: start the game. quit game until q or ctrl-c is pressed
    public void start() throws IOException, InterruptedException {
        screen.setCursorInvisible();
        render();
        while (!quit) {
            if (!termial.isKeyDown()) {
                continue;
            }
            handleKeyDown();
            render();
        }
        termial.close();
    }

    // MODIFIES: this
    // EFFECTS: render all the elements to the screen
    private void render() throws IOException, InterruptedException {
        drawInfoPannel();
        drawMaze();
        drawItems();
        drawPlayer();
        screen.render();
    }

    // MODIFIES: this
    // EFFECTS: draw (or actuall write) the info panel to the screen buffer
    private void drawInfoPannel() {
        int line = INFO_START_LINE;
        screen.write("This is a friendly maze solving game", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT,
                false, true);
        screen.write("You have solved " + game.getSolved(), INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false,
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
        info = "You have " + playerInventory.getInventorySize() + " items:";
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        for (int i = 0; i < playerInventory.getInventorySize(); i++) {
            if (INFO_START_LINE + 15 + i + 1 == HEIGHT) {
                screen.write("    ...", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
                return line;
            }
            info = "    " + (i + 1) + " - " + playerInventory.getItem(i).getName();
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
        Iterator<Coordinate> it = game.getItemPositionIterator();
        while (it.hasNext()) {
            screenPos = toScreen(it.next());

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
    private void handleKeyDown() throws IOException, InterruptedException {
        int key = termial.getKey();
        char c = (char) key;

        if (c == 'q') {
            quit = true;
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
        if (item == null) {
            return;
        }
        item.apply(game);
        playerInventory.removeItem(index);
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
    // plaeryInventory.getInventorySize < Inventory.TERMINAL_GUI_NUM_RESTRICT
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
        } else {
            if (playerInventory.getInventorySize() >= Inventory.TERMINAL_GUI_NUM_RESTRICT) {
                game.setGameMessage("Your bag is full...");
                return;
            }
            playerInventory.addItem(item);
            game.setGameMessage(item.report());
        }
        game.removeItem(playerPos);
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
}
