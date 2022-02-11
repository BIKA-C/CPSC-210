package model.maze;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.TestHelpers;
import model.utility.Coordinate;

public class MazeTest extends TestHelpers {

    private Maze maze;

    @BeforeEach
    public void setup() {
        maze = new Maze(10, 10);
    }

    @Test
    public void constructorTest() {
        // because mazes are random, I test 5000 of them to make sure
        for (int i = 0; i < 5000; i++) {
            maze = new Maze(40, 30);

            Coordinate exit = maze.getExit();
            Coordinate start = maze.getStart();
            assertFalse(maze.isWall(exit));
            assertFalse(maze.isWall(start));

            assertTrue(exit.getX() >= 40 / 2);
            assertTrue(exit.getX() <= 40 - 1);
            assertTrue(exit.getY() >= 30 / 2);
            assertTrue(exit.getY() <= 30 - 1);

            assertTrue(start.getX() >= 0);
            assertTrue(start.getX() < 40 / 2);
            assertTrue(start.getY() >= 0);
            assertTrue(start.getY() < 30);
            // check all the roads are actually roads
            for (int j = 0; j < maze.getNumOfRoad(); j++) {
                assertFalse(maze.isWall(maze.getRoad(j)));
            }

            assertEquals(40, maze.getWidth());
            assertEquals(30, maze.getHeight());
        }
    }

    @Test
    public void getRoadTest() {
        for (int i = 0; i < maze.getNumOfRoad(); i++) {
            assertFalse(maze.isWall(maze.getRoad(i)));
        }
    }

    @Test
    public void setBlockTest() {
        Coordinate coordinate = new Coordinate(9, 7);
        boolean[][] save = copyMaze(maze);

        maze.setBlock(coordinate, false);
        assertTrue(maze.isWall(coordinate));
        isSameMazeExcept(maze, save, coordinate);

        maze.setBlock(coordinate, true);
        assertFalse(maze.isWall(coordinate));
        isSameMazeExcept(maze, save, coordinate);

        coordinate = new Coordinate(1, 2);
        save = copyMaze(maze);

        maze.setBlock(coordinate, false);
        assertTrue(maze.isWall(coordinate));
        isSameMazeExcept(maze, save, coordinate);

        maze.setBlock(coordinate, true);
        assertFalse(maze.isWall(coordinate));
        isSameMazeExcept(maze, save, coordinate);
    }

    @Test
    public void isInRangeTest() {
        Coordinate c = new Coordinate(10, 10);
        assertFalse(maze.isInRange(c));

        c = new Coordinate(9, 10);
        assertFalse(maze.isInRange(c));

        c = new Coordinate(10, 9);
        assertFalse(maze.isInRange(c));

        c = new Coordinate(1, 10);
        assertFalse(maze.isInRange(c));

        c = new Coordinate(9, 9);
        assertTrue(maze.isInRange(c));

        c = new Coordinate(-1, 10);
        assertFalse(maze.isInRange(c));

        c = new Coordinate(10, -1);
        assertFalse(maze.isInRange(c));

        c = new Coordinate(-1, -1);
        assertFalse(maze.isInRange(c));
    }
}
