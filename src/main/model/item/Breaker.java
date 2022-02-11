package model.item;

import model.Game;
import model.maze.Maze;
import model.utility.Coordinate;
import model.utility.Direction;

// Breaker - Wall breaker is an in game item
// that can break all the walls within it's
// range
public class Breaker implements Item {
    private final int range;

    // EFFECTS: constructs a breaker with the given range
    // the range can't be changed after initialized
    public Breaker(int blocks) {
        this.range = blocks;
    }

    // REQUIRES: g != null
    // MODIFIES: g
    // EFFECTS: breaks all the walls (turn them into roads)
    // within the range in the direction of the player
    // and g.getGameMessage() will be the report of
    // the number of the walls that are destroyed
    @Override
    public void apply(Game g) {
        Direction dir = g.getPlayer().getDirection();
        Coordinate coord = g.getPlayer().getPosition();
        Maze maze = g.getMaze();

        int saveX = coord.getX();
        int saveY = coord.getY();

        Coordinate save = new Coordinate(saveX, saveY);

        int destroyed = 0;
        for (int i = 1; i <= range; i++) {
            save.go(dir, 1);
            if (!maze.isInRange(save) || !maze.isWall(save)) {
                continue;
            }
            g.getMaze().setBlock(save, true);
            destroyed++;
        }

        g.setGameMessage(destroyed + " walls are destroyed");
    }

    // EFFECTS: this item will not be auto-applied
    // after picking up
    @Override
    public boolean isAutoApply() {
        return false;
    }

    // EFFECTS: returns the name of the item + it's range
    // that is initialized with
    @Override
    public String getName() {
        return "wall breaker range " + range;
    }

    // EFFECTS: return the item description
    @Override
    public String report() {
        return "You got a range " + range + " wall breaker.";
    }
}
