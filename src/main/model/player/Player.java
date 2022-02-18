package model.player;

import model.utility.Coordinate;
import model.utility.Direction;

// Player is the player who plays the maze
// it has a position on the maze and a direction
// heading to.
// Player also has an inventory bag that stores all the
// items that the player picked up from the game
public class Player {
    private Coordinate position;
    private Direction direction;
    private Inventory inventory;

    public static final int TERMINAL_GUI_NUM_RESTRICT = 9;

    // EFFECTS: constructs a player at the position (0, 0)
    // and down direction and a new inventory
    public Player() {
        direction = Direction.DOWN;
        position = new Coordinate(0, 0);
        inventory = new Inventory();
    }

    // MODIFIES: this
    // EFFECTS: if the dir != getDirection()
    // this function will change the player's direction
    // to the given dir and not move the player
    // otherwise, this function will the player
    // 1 unit along the given direction
    public void move(Direction dir) {
        if (dir != direction) {
            direction = dir;
            return;
        }
        position.go(direction, 1);
    }

    public Direction getDirection() {
        return direction;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // MODIFIES: this
    // EFFECTS: set the player's position by the given position
    public void setPosition(Coordinate position) {
        this.position.setXY(position.getX(), position.getY());
    }

}
