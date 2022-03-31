package ui;

import java.awt.event.KeyListener;

import model.Game;
import model.utility.Direction;
import ui.panel.GamePanel;

import java.awt.event.KeyEvent;

// handles key input and updates the game logic and graphics
public class KeyHandler implements KeyListener {

    private Game game;
    private GamePanel panel;

    // EFFECTS: constructs a new keyhandler
    public KeyHandler(Game game, GamePanel panel) {
        this.game = game;
        this.panel = panel;
    }

    /// EFFECTS: do nothing
    @Override
    public void keyTyped(KeyEvent e) {
    }

    // MODIFIES: this
    // EFFECTS: if the key pressed is one of [w, a, s, d], try to move the player
    // if the key is one [0-9], try to apply the item in the player's inventory
    @Override
    public void keyPressed(KeyEvent e) {

        int oldInventorySize = game.getPlayer().getInventory().getInventorySize();
        int key = e.getKeyChar();

        if (isDirectionKey(e.getKeyChar())) {
            game.move(keyToDirection(e.getKeyChar()));
            panel.getInfoPanel().repaint();
        } else if (key <= 57 && key >= 49) {
            game.applyItem(key - 48 - 1);
            panel.getInfoPanel().updateItems();
        } else {
            return;
        }

        checkExit();
        if (oldInventorySize != game.getPlayer().getInventory().getInventorySize()) {
            panel.getInfoPanel().updateItems();
        }
        panel.repaint();
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

    /// EFFECTS: do nothing
    @Override
    public void keyReleased(KeyEvent e) {
    }

    // MODIFIES: this
    public void setGame(Game game) {
        this.game = game;
    }

    // MODIFIES: this
    // EFFECTS: if game.isEnded()
    // then the game goes to the next maze
    // with a new maze initialized
    // and game.getReward() will be added to the player's'
    // inventory bad.
    // the reward message will be updated through
    // game.getGameMessage()
    private void checkExit() {
        if (game.isEnded()) {
            game.nextLevel(false, false);
        }
    }

}
