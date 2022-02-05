package ui;

import java.io.IOException;

import model.Game;
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

    private final Screen screen;

    private Maze maze;
    private final Player player;
    private final Inventory playerInventory;
    private final Coordinate playerPos;

    private boolean quit;

    public final int screenWidth;
    public final int screenHeight;

    private final TextAttribute wallStyle;
    private final TextAttribute exitStyle;
    private final TextAttribute playerStyle;
    private final TextAttribute itemStyle;

    private final Pixel wallPixel;
    private final Pixel exitPixel;
    private final Pixel playerPixel;
    private final Pixel itemPixel;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public static final int STATUS_BAR_HEIGHT = 1;

    public ConsoleApp() throws InterruptedException, IOException {
        quit = false;
        screenWidth = WIDTH;
        screenHeight = HEIGHT + STATUS_BAR_HEIGHT;

        game = new Game(WIDTH, HEIGHT);
        termial = new Terminal(screenWidth, screenHeight);

        screen = termial.getScreen();

        maze = game.getMaze();
        player = game.getPlayer();
        playerInventory = player.getInventory();
        playerPos = player.getPosition();

        wallStyle = new TextAttribute(4, -1, 0);
        exitStyle = new TextAttribute(13, -1, 0);
        playerStyle = new TextAttribute(83, -1, 0);
        itemStyle = new TextAttribute(11, -1, 1);

        wallPixel = new Pixel('█', wallStyle);
        exitPixel = new Pixel('⬤', exitStyle);
        playerPixel = new Pixel('⬤', playerStyle);
        itemPixel = new Pixel('⬤', itemStyle);
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
        screen.write("Maze Game!! Coins: " + playerInventory.getCoins(), 0, 0, TextAttribute.DEFAULT, false);
        drawMaze();
        drawItems();
        drawPlayer();
        screen.render();
    }

    private void handleKeyDown() throws IOException, InterruptedException {
        int key = termial.getKey();
        char c = (char) key;

        // screen.write("key:" + Character.toString(c) + " -> " + key, 11, 0,
        // TextAttribute.DEFAULT);
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
            default:
                break;
        }
        handleItem();
        handleExit();
    }

    private void drawMaze() {
        drawMazeBorder();
        Coordinate screenPos;
        Coordinate pos;
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < game.getMaze().getWidth(); j++) {
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
        Coordinate upper = new Coordinate(0, STATUS_BAR_HEIGHT);
        Coordinate bottom = new Coordinate(0, screenHeight - 1);
        for (int i = 0; i < WIDTH; i++) {
            screen.writePixel(wallPixel, upper, true);
            screen.writePixel(wallPixel, bottom, true);
            upper.goRight();
            bottom.goRight();
        }

        Coordinate left = new Coordinate(0, STATUS_BAR_HEIGHT);
        Coordinate right = new Coordinate(WIDTH - 1, STATUS_BAR_HEIGHT);
        for (int i = 0; i < HEIGHT; i++) {
            screen.writePixel(wallPixel, left, true);
            screen.writePixel(wallPixel, right, true);
            left.goDown();
            right.goDown();
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
        screenPos.increaseXY(1, STATUS_BAR_HEIGHT + 1);
        return screenPos;
    }

    private void tryMove(Direction direction) {
        Coordinate pos = new Coordinate(playerPos.getX(), playerPos.getY());
        pos.go(direction);
        if (!maze.isInRange(pos) || maze.isWall(pos)) {
            return;
        }
        player.move(direction);
    }

    private void handleItem() {
        for (int i = 0; i < game.getItemsSize(); i++) {
            if (!game.getItemPosition(i).isSame(playerPos)) {
                continue;
            }
            game.popItem(i).apply(game);
        }
    }

    private void handleExit() {
        if (game.isEnded()) {
            game.nextLevel();
            maze = game.getMaze();
        }
    }
}
