package model.item;

import org.json.JSONObject;

import model.Game;
import model.maze.Maze;
import model.utility.Coordinate;
import model.utility.Direction;

// Breaker - Wall breaker is an in game item
// that can break all the walls within it's
// range
public class Breaker extends Item {
    private final int range;

    // EFFECTS: constructs a breaker with the given range
    // the range can't be changed after initialized
    public Breaker(int blocks) {
        this.range = blocks;

        isAutoApply = false;
        displayName = "wall breaker range " + range;
        reportMessage = "You got a range " + range + " wall breaker";

        type = ItemType.BREAKER;
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

    @Override
    public String toJSONString() {
        JSONObject breaker = super.toJson();
        breaker.put("range", range);
        return breaker.toString();
    }
}
