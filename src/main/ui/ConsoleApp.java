package ui;

import java.io.IOException;

import model.Game;
import model.item.Item;
import model.maze.Maze;
import model.player.Inventory;
import model.player.Player;
import model.utility.Coordinate;
import model.utility.Direction;
import ui.console.Pixel;
import ui.console.Screen;
import ui.console.Terminal;
import ui.console.TextAttribute;

public class ConsoleApp {
    private Game game;
    private Terminal termial;
    private boolean quit;

    private final Screen screen;

    private final Player player;
    private final Inventory playerInventory;
    private final Coordinate playerPos;

    public final int screenWidth;
    public final int screenHeight;

    private final TextAttribute wallStyle = new TextAttribute(4, -1, 0);
    private final TextAttribute exitStyle = new TextAttribute(9, -1, 0);
    private final TextAttribute playerStyle = new TextAttribute(10, -1, 0);
    private final TextAttribute itemStyle = new TextAttribute(11, -1, 1);
    private final TextAttribute messageStyle = new TextAttribute(6, -1, 0);
    private final Pixel wallPixel = new Pixel('█', wallStyle);
    private final Pixel exitPixel = new Pixel('⬤', exitStyle);
    private final Pixel playerPixel = new Pixel('⬤', playerStyle);
    private final Pixel itemPixel = new Pixel('⬤', itemStyle);

    public static final int WIDTH = 38;
    public static final int HEIGHT = WIDTH;
    public static final int INFO_PANNEL_START_X = (WIDTH + 3) * 2;
    public static final int INFO_PANNEL_WIDTH = 30;
    public static final int INFO_START_LINE = Math.toIntExact(Math.round(HEIGHT * 0.15));

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

    private void render() throws IOException, InterruptedException {
        drawInfoPannel();
        drawMaze();
        drawItems();
        drawPlayer();
        screen.render();
    }

    private void drawInfoPannel() {
        int line = INFO_START_LINE;
        screen.write("This is a friendly maze solving game", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT,
                false, true);
        line++;
        line = drawInventory(line);

        line++;
        line = drawInfo(line);

        line++;
        line = drawMessage(line);
    }

    private int drawMessage(int line) {
        screen.write("Message: ", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        screen.write(game.getItemMessage(), INFO_PANNEL_START_X, line++, messageStyle, false, true);
        return line;
    }

    private int drawInfo(int line) {
        String info;
        screen.write("Info: ", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "Direction: " + player.getDirection();
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "Position: " + playerPos;
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        return line;
    }

    private int drawInventory(int line) {
        String info;
        screen.write("Inventory: ", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "Coins: " + playerInventory.getCoins();
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        info = "You have " + playerInventory.getInventorySize() + " items:";
        screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        for (int i = 0; i < playerInventory.getInventorySize(); i++) {
            if (INFO_START_LINE + 12 + i + 1 == HEIGHT) {
                screen.write("    ...", INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
                return line;
            }
            info = "    " + (i + 1) + " - " + playerInventory.getItem(i).getName();
            screen.write(info, INFO_PANNEL_START_X, line++, TextAttribute.DEFAULT, false, true);
        }
        return line;
    }

    private void handleKeyDown() throws IOException, InterruptedException {
        int key = termial.getKey();
        char c = (char) key;

        switch (c) {
            case 'q':
                quit = true;
                break;
            case 'w':
                tryMove(Direction.up);
                break;
            case 'a':
                tryMove(Direction.left);
                break;
            case 's':
                tryMove(Direction.down);
                break;
            case 'd':
                tryMove(Direction.right);
                break;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                tryApply(key - 48 - 1);
                break;
            default:
                break;
        }
        handleItem();
        handleExit();
    }

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

    private void drawItems() {
        Coordinate screenPos;
        for (int i = 0; i < game.getItemsSize(); i++) {
            screenPos = toScreen(game.getItemPosition(i));

            screen.writePixel(itemPixel, screenPos, false);
        }
    }

    private void drawPlayer() {
        Coordinate pos = toScreen(playerPos);
        screen.writePixel(playerPixel, pos, false);
    }

    private Coordinate toScreen(Coordinate pos) {
        Coordinate screenPos = new Coordinate(pos.getX(), pos.getY());
        screenPos.increaseXY(1, 1);
        return screenPos;
    }

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

    private void tryApply(int index) {
        Item item = playerInventory.getItem(index);
        if (item == null) {
            return;
        }
        item.apply(game);
        playerInventory.removeItem(index);
    }

    private void handleItem() {
        for (int i = 0; i < game.getItemsSize(); i++) {
            if (!game.getItemPosition(i).isSame(playerPos)) {
                continue;
            }
            Item item = game.getItem(i);
            if (item.autoApply(game)) {
                item.apply(game);
            } else {
                playerInventory.addItem(item);
            }
            game.popItem(i);
        }
    }

    private void handleExit() {
        if (game.isEnded()) {
            game.nextLevel();
        }
    }
}
