package model.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoordinateTest {

    private Coordinate coord;
    private Coordinate orgin;

    @BeforeEach
    public void setup() {
        coord = new Coordinate(9, 2);
        orgin = new Coordinate(0, 0);
    }

    @Test
    public void constructorTest() {
        assertEquals(9, coord.getX());
        assertEquals(2, coord.getY());

        assertEquals(0, orgin.getX());
        assertEquals(0, orgin.getY());
    }

    @Test
    public void goTest() {
        // this internally test all goUp goDown goLeft and goRight methods
        for (Direction direction : Direction.values()) {
            coord = new Coordinate(9, 2);
            coord.go(direction);
            switch (direction) {
                case up:
                    assertEquals(1, coord.getY());
                    assertEquals(9, coord.getX());
                    break;
                case down:
                    assertEquals(3, coord.getY());
                    assertEquals(9, coord.getX());
                    break;
                case right:
                    assertEquals(2, coord.getY());
                    assertEquals(10, coord.getX());
                    break;
                case left:
                    assertEquals(2, coord.getY());
                    assertEquals(8, coord.getX());
                    break;
            }
        }
    }

    // @Test
    // public void resetTest() {
    //     coord.setX(100);
    //     coord.setY(200);
    //     coord.reset();
    //     assertEquals(9, coord.getX());
    //     assertEquals(2, coord.getY());

    //     coord.setX(900);
    //     coord.setY(15);
    //     coord.reset();
    //     assertEquals(9, coord.getX());
    //     assertEquals(2, coord.getY());
    // }

    @Test
    public void setXTest() {
        coord.setX(10);
        assertEquals(10, coord.getX());

        coord.setX(20);
        assertEquals(20, coord.getX());
    }

    @Test
    public void setYTest() {
        coord.setY(200);
        assertEquals(200, coord.getY());

        coord.setY(206);
        assertEquals(206, coord.getY());
    }

    @Test
    public void setXYTest() {
        coord.setXY(200, 300);
        assertEquals(200, coord.getX());
        assertEquals(300, coord.getY());

        coord.setXY(400, 200);
        assertEquals(400, coord.getX());
        assertEquals(200, coord.getY());
    }

    @Test
    public void increaseXTest() {
        coord.increaseX(10);
        assertEquals(19, coord.getX());
        assertEquals(2, coord.getY());

        coord.increaseX(20);
        assertEquals(39, coord.getX());
        assertEquals(2, coord.getY());
    }

    @Test
    public void decreaseXTest() {
        coord.decreaseX(2);
        assertEquals(7, coord.getX());
        assertEquals(2, coord.getY());

        coord.decreaseX(3);
        assertEquals(4, coord.getX());
        assertEquals(2, coord.getY());
    }

    @Test
    public void increaseYTest() {
        coord.increaseY(10);
        assertEquals(12, coord.getY());
        assertEquals(9, coord.getX());

        coord.increaseY(20);
        assertEquals(32, coord.getY());
        assertEquals(9, coord.getX());
    }

    @Test
    public void decreaseYTest() {
        coord.decreaseY(1);
        assertEquals(1, coord.getY());
        assertEquals(9, coord.getX());

        coord.decreaseY(3);
        assertEquals(-2, coord.getY());
        assertEquals(9, coord.getX());
    }

    @Test
    public void increaseXYTest() {
        coord.increaseXY(10, 20);
        assertEquals(19, coord.getX());
        assertEquals(22, coord.getY());

        coord.increaseXY(20, 30);
        assertEquals(39, coord.getX());
        assertEquals(52, coord.getY());
    }

    @Test
    public void decreaseXYTest() {
        coord.decreaseXY(2, 1);
        assertEquals(7, coord.getX());
        assertEquals(1, coord.getY());

        coord.decreaseXY(3, 1);
        assertEquals(4, coord.getX());
        assertEquals(0, coord.getY());
    }

    @Test
    public void isSameTest() {
        Coordinate a = new Coordinate(1, 2);
        Coordinate b = new Coordinate(1, 2);
        Coordinate c = new Coordinate(1, 2);
        Coordinate d = new Coordinate(1, 20);

        assertFalse(a.isSame(d));
        assertFalse(b.isSame(d));
        assertFalse(c.isSame(d));

        assertTrue(a.isSame(a));
        assertTrue(b.isSame(b));

        assertTrue(a.isSame(b));
        assertTrue(b.isSame(a));

        assertTrue(a.isSame(b));
        assertTrue(b.isSame(c));
        assertTrue(a.isSame(c));

        a.setX(2);
        assertTrue(a.isSame(a));

        a.setX(3);
        assertTrue(a.isSame(a));

        b.setX(3);
        assertTrue(b.isSame(b));
    }
}
