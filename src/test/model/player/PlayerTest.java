package model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.utility.Coordinate;
import model.utility.Direction;

public class PlayerTest {
    private Player player;
    Coordinate zero;

    @BeforeEach
    public void setup() {
        player = new Player();
        zero = new Coordinate(0, 0);
    }

    @Test
    public void constructorTest() {
        assertEquals(Direction.down, player.getDirection());
        assertTrue(player.getPosition().isSame(zero));

        assertNotNull(player.getInventory());
    }

    @Test
    public void moveRightTest() {
        Coordinate coord = new Coordinate(0, 0);

        player.move(Direction.right);
        assertEquals(Direction.right, player.getDirection());
        assertTrue(player.getPosition().isSame(zero));

        coord.setXY(1, 0);
        player.move(Direction.right);
        assertEquals(Direction.right, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));

        coord.setXY(2, 0);
        player.move(Direction.right);
        assertEquals(Direction.right, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));
    }

    @Test
    public void moveLeftTest() {
        Coordinate coord = new Coordinate(0, 0);

        player.move(Direction.left);
        assertEquals(Direction.left, player.getDirection());
        assertTrue(player.getPosition().isSame(zero));

        coord.setXY(-1, 0);
        player.move(Direction.left);
        assertEquals(Direction.left, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));

        coord.setXY(-2, 0);
        player.move(Direction.left);
        assertEquals(Direction.left, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));
    }

    @Test
    public void moveUpTest() {
        Coordinate coord = new Coordinate(0, 0);

        player.move(Direction.up);
        assertEquals(Direction.up, player.getDirection());
        assertTrue(player.getPosition().isSame(zero));

        coord.setXY(0, -1);
        player.move(Direction.up);
        assertEquals(Direction.up, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));

        coord.setXY(0, -2);
        player.move(Direction.up);
        assertEquals(Direction.up, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));

    }

    @Test
    public void moveDownTest() {
        Coordinate coord = new Coordinate(0, 0);
        player.setDirection(Direction.left);

        player.move(Direction.down);
        assertEquals(Direction.down, player.getDirection());
        assertTrue(player.getPosition().isSame(zero));

        coord.setXY(0, 1);
        player.move(Direction.down);
        assertEquals(Direction.down, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));

        coord.setXY(0, 2);
        player.move(Direction.down);
        assertEquals(Direction.down, player.getDirection());
        assertTrue(player.getPosition().isSame(coord));
    }

    @Test
    public void setPositionTest() {
        Coordinate coord = new Coordinate(12, 33);

        player.setPosition(coord);
        assertTrue(player.getPosition().isSame(coord));

        coord.setXY(44, 55);
        player.setPosition(coord);
        assertTrue(player.getPosition().isSame(coord));
    }
}