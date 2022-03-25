package ui.panel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Game;
import model.item.Item;
import model.maze.Maze;
import model.utility.Coordinate;
import ui.MazeGame;

// the panel that holds the maze
public class MazePanel extends JPanel {

    private Game game;

    private static final double ITEM_RATIO = 0.4;
    private static final double PLAYER_RATIO = 0.5;
    private static final double EXIT_RATIO = 0.6;

    private final int unit;
    private final int itemSize;
    private final int playerSize;
    private final int exitSize;

    // EFFECTS: constructs the mazePanel with the given game and size
    public MazePanel(Game game, Dimension size) {
        super();
        int width = size.height < size.width ? size.height : size.width;
        super.setPreferredSize(new Dimension(width, width));
        super.setBackground(MazeGame.BACKGROUND_COLOR);

        this.game = game;
        this.unit = width / game.getMazeSize();
        itemSize = (int) (unit * ITEM_RATIO);
        playerSize = (int) (unit * PLAYER_RATIO);
        exitSize = (int) (unit * EXIT_RATIO);

        //xxx
        if (MazeGame.DEBUG) {
            super.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    // MODIFIES: g
    // EFFECTS: paint the components, not managed by this class
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
        drawPlayer(g);
        drawItems(g);
    }

    // MODIFIES: g
    // EFFECTS: draw all the items on map to g
    private void drawItems(Graphics g) {
        g.setColor(MazeGame.ITEM_COLOR);
        Coordinate pos;
        for (Map.Entry<Coordinate, Item> entry : game.getItemEntrySet()) {
            pos = toScreen(entry.getKey());
            int offset = offset(ITEM_RATIO);
            g.fillOval(pos.getX() + offset, pos.getY() + offset, itemSize, itemSize);
        }
    }

    // MODIFIES: g
    // EFFECTS: draw the player to g
    private void drawPlayer(Graphics g) {
        g.setColor(MazeGame.PLAYER_COLOR);
        Coordinate pos = toScreen(game.getPlayer().getPosition());
        int offset = offset(PLAYER_RATIO);
        g.fillOval(pos.getX() + offset, pos.getY() + offset, playerSize, playerSize);
    }

    // MODIFIES: g
    // EFFECTS: draw the maze to g
    private void drawMaze(Graphics g) {
        Maze maze = game.getMaze();
        g.setColor(MazeGame.WALL_COLOR);
        drawMazeBorder(g);
        Coordinate pos;
        Coordinate screenPos;
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                pos = new Coordinate(j, i);
                screenPos = toScreen(pos);
                if (i == maze.getExit().getY() && j == maze.getExit().getX()) {
                    g.setColor(MazeGame.EXIT_COLOR);
                    int offset = offset(EXIT_RATIO);
                    g.fillRoundRect(screenPos.getX() + offset, screenPos.getY() + offset, exitSize, exitSize,
                            (int) (unit * 0.4), (int) (unit * 0.4));
                    g.setColor(MazeGame.WALL_COLOR);
                } else if (maze.isWall(pos)) {
                    g.fillRect(screenPos.getX(), screenPos.getY(), unit, unit);
                }
            }
        }
    }

    // MODIFIES: g
    // EFFECTS: draw the maze boarders to g
    private void drawMazeBorder(Graphics g) {
        Coordinate up = new Coordinate(0, 0);
        Coordinate bottom = new Coordinate(0, game.getMazeSize() - 1);
        for (int i = 0; i < game.getMazeSize(); i++) {
            g.fillRect(up.getX() * unit, up.getY() * unit, unit, unit);
            g.fillRect(bottom.getX() * unit, bottom.getY() * unit, unit, unit);
            up.goRight(1);
            bottom.goRight(1);
        }

        Coordinate left = new Coordinate(0, 0);
        Coordinate right = new Coordinate(game.getMazeSize() - 1, 0);
        for (int i = 0; i < game.getMazeSize(); i++) {
            g.fillRect(left.getX() * unit, left.getY() * unit, unit, unit);
            g.fillRect(right.getX() * unit, right.getY() * unit, unit, unit);
            left.goDown(1);
            right.goDown(1);
        }
    }

    // EFFECTS: converts the pos to a new pos that is relative to the screen(ready to render)
    private Coordinate toScreen(Coordinate pos) {
        Coordinate screenPos = new Coordinate(pos.getX(), pos.getY());
        screenPos.increaseXY(1, 1);
        screenPos.setXY(screenPos.getX() * unit, screenPos.getY() * unit);
        return screenPos;
    }

    // EFFECTS: computes the offset one element needs to take to be centered at the given unit square
    // if the element's widht/height/radius/etc is not equal to the unit. i.e. it has be scaled by
    // the ratio
    private int offset(double ratio) {
        return Math.toIntExact(Math.round(unit * (1 - ratio))) / 2;
    }
}
