package model.maze;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.utility.Coordinate;

public class MazeTest {

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
                assertFalse(maze.isWall(maze.getaRoad(j)));
            }

        }
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

    // xxx mabe more tests for getaroad and getnumofroad
}
