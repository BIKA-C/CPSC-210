package model.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.TestHelpers;
import model.maze.Maze;
import model.utility.Coordinate;
import model.utility.Direction;

public class BreakerTest extends TestHelpers {
    private Breaker breaker;

    @BeforeEach
    public void setup() {
        breaker = new Breaker(3);
        game = new Game(20, 20);
    }

    @Test
    public void constructorTest() {
        assertEquals("wall breaker range 3", breaker.getDisplayName());
    }

    @Test
    public void applyTest() {
        // because of the randomness, I can't explictively set the player
        // the position to special positions on the maze. So, I test it 2000 times
        // 500 times for each directions
        for (int i = 0; i < 2000; i++) {
            game = new Game(20, 20);
            game.getPlayer().setDirection(Direction.values()[i % 4]);
            assertEffect();
        }
    }

    @Test
    public void autoApplyTest() {
        assertFalse(breaker.isAutoApply());
    }

    @Test
    public void getNameTest() {
        assertEquals("wall breaker range 3", breaker.getDisplayName());
    }

    @Test
    public void reportTest() {
        assertEquals("You got a range 3 wall breaker", breaker.report());
    }

    // EFFECTS: apply the effct once, and test if all things work properly
    private void assertEffect() {
        Coordinate coord = game.getPlayer().getPosition();

        makeCopyOfGame();

        breaker.apply(game);

        Coordinate copyCoord = new Coordinate(coord.getX(), coord.getY());
        assertNumberOfWallsDestroyed(saveMaze, game.getMaze(), copyCoord, savePlayerDir);
        assertWallsAreDestroyed(game.getMaze(), copyCoord, savePlayerDir);
        assertOnlyInRangeMazeBlocksAreChanged(game.getMaze(), saveMaze, copyCoord, savePlayerDir);

        assertGameItemsNotChanged();
        assertPlayerSolvedNotChanged();
        assertPlayerNotChanged();
    }

    // REQUIRES: maze != null && saveMaze != null && saveCoord != null
    // EFFECTS: check that except blocks in the range along the direction, all other
    // blocks are not changed
    private void assertOnlyInRangeMazeBlocksAreChanged(Maze maze, boolean[][] saveMaze, Coordinate saveCoord,
            Direction dir) {

        int x = saveCoord.getX();
        int y = saveCoord.getY();
        Coordinate temp = new Coordinate(0, 0);
        Coordinate[] skip = generateSkipPoints(saveCoord, dir);
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                temp.setXY(j, i);
                if (isSkipPoint(temp, skip)) {
                    continue;
                }
                assertEquals(!maze.isWall(temp), saveMaze[i][j]);
            }
        }
        saveCoord.setXY(x, y);
    }

    // EFFECTS: generate a list of 3 points along the direction dir from now
    private Coordinate[] generateSkipPoints(Coordinate now, Direction dir) {
        Coordinate[] list = new Coordinate[3];
        Coordinate save = new Coordinate(now.getX(), now.getY());
        for (int i = 0; i < 3; i++) {
            save.go(dir, 1);
            list[i] = new Coordinate(save.getX(), save.getY());
        }

        return list;
    }

    // EFFECTS: true if coord is in the skipPoints
    private boolean isSkipPoint(Coordinate coord, Coordinate[] skipPoints) {
        for (int i = 0; i < skipPoints.length; i++) {
            if (coord.isSame(skipPoints[i])) {
                return true;
            }
        }

        return false;
    }

    // REQUIRES: maze != null && savecoord != null
    // EFFECTS: check that all the walls in the range along the direction are
    // actually destroyed
    private void assertWallsAreDestroyed(Maze maze, Coordinate saveCoord, Direction dir) {
        int x = saveCoord.getX();
        int y = saveCoord.getY();
        for (int i = 0; i < 3; i++) {
            saveCoord.go(dir, 1);
            if (!maze.isInRange(saveCoord)) {
                continue;
            }
            assertFalse(maze.isWall(saveCoord));
        }
        saveCoord.setXY(x, y);
    }

    // REQUIRES: maze != null && savecoord != null
    // EFFECTS: check the number of walls that are destroyed is actaull correct
    private void assertNumberOfWallsDestroyed(boolean[][] saveMaze, Maze maze, Coordinate saveCoord, Direction dir) {
        int x = saveCoord.getX();
        int y = saveCoord.getY();
        int wallCounter = 0;
        for (int i = 0; i < 3; i++) {
            saveCoord.go(dir, 1);
            if (maze.isInRange(saveCoord) && !saveMaze[saveCoord.getY()][saveCoord.getX()]) {
                wallCounter++;
            }
        }
        assertEquals(wallCounter + " walls are destroyed", game.getGameMessage());
        saveCoord.setXY(x, y);
    }
}
