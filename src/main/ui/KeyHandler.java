package ui;

import java.awt.event.KeyListener;

import model.Game;
import model.item.Item;
import model.player.Inventory;
import model.utility.Coordinate;
import model.utility.Direction;
import ui.exceptions.BagIsFullException;
import ui.exceptions.CollisionException;
import ui.exceptions.PlayerMovementException;
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

        int old = game.getPlayer().getInventory().getInventorySize();
        int key = e.getKeyChar();

        if (isDirectionKey(e.getKeyChar())) {
            tryMove(keyToDirection(e.getKeyChar()));
        } else if (key <= 57 && key >= 49) {
            tryApply(key - 48 - 1);
        } else {
            return;
        }

        checkExit();
        handleItem();
        if (old != game.getPlayer().getInventory().getInventorySize()) {
            panel.getInfoPanel().updateItems();
        }
        panel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: get the item from the player's inventory bag by
    // the index and apply it. Then the item will be removed
    // from the player's inventory
    // if such index does not exist, the function will do nothing
    // i.e. user does not have this indexed item in the bag
    private void tryApply(int index) {
        Inventory playerInventory = game.getPlayer().getInventory();
        Item item = playerInventory.getItem(index);
        try {
            item.apply(game);
            playerInventory.removeItem(index);
            panel.getInfoPanel().updateItems();
        } catch (NullPointerException e) {
            return;
        }
    }

    // MODIFIES: this
    // EFFECTS: move the player along the direction, if movePlayer(direction) throws
    // PlayerMovementException, player will not be moved, but panel.getInfoPanel() will be updated
    private void tryMove(Direction direction) {
        try {
            movePlayer(direction);
        } catch (PlayerMovementException e1) {
            return;
        }
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
    // EFFECTS: try move the player 1 unit along the direction
    // if the movement will cause the player to be out of the
    // boundary of the game or the destination is a wall
    // the function will only update the player's direction to the
    // given direction and the movement will not occur and the function
    // throws PlayerMovementException but panel.getInfoPanel() will be updated
    private void movePlayer(Direction direction) throws PlayerMovementException {
        Coordinate save = new Coordinate(game.getPlayer().getPosition());
        save.go(direction, 1);
        if (!game.getMaze().isInRange(save) || game.getMaze().isWall(save)) {
            game.getPlayer().setDirection(direction);
            panel.getInfoPanel().repaint();
            throw new CollisionException();
        }

        game.getPlayer().move(direction);
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
        Coordinate playerPos = game.getPlayer().getPosition();
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
    // if playerInventory.getInventorySize() >= Inventory.NUM_RESTRICT
    // then throws BagIsFullException and the item will not be added
    private void addItemToPlayerInventory(Item item) throws BagIsFullException {
        Inventory playerInventory = game.getPlayer().getInventory();
        if (playerInventory.getInventorySize() >= Inventory.NUM_RESTRICT) {
            throw new BagIsFullException();
        }
        playerInventory.addItem(item);
    }
}
