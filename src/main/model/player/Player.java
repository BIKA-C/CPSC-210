package model.player;

import model.utility.Coordinate;
import model.utility.Direction;

public class Player {
    private Coordinate position;
    private Direction direction;
    private Inventory inventory;

    public Player() {
        direction = Direction.down;
        position = new Coordinate(0, 0);
        inventory = new Inventory();
    }

    public void move(Direction dir) {
        if (dir != direction) {
            direction = dir;
        }
        position.go(direction);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position.setXY(position.getX(), position.getY());
    }
}
