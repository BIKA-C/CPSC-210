package model.item;

import model.Game;
import model.maze.Maze;
import model.utility.Coordinate;
import model.utility.Direction;

public class Breaker implements Item {
    private int range;

    public Breaker(int blocks) {
        this.range = blocks;
    }

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
            g.getMaze().setBloack(save, true);
            destroyed++;
        }

        g.setItemMessage(destroyed + " walls are destroyed");
    }

    @Override
    public boolean autoApply(Game g) {
        g.setItemMessage("You got a range " + range + " wall breaker.");
        return false;
    }

    @Override
    public String getName() {
        return "wall breaker range " + range;
    }

}
