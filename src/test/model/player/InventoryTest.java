package model.player;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.item.Breaker;
import model.item.Hint;
import model.item.Item;

public class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    public void setup() {
        inventory = new Inventory();
    }

    @Test
    public void constructosTest() {
        assertEquals(inventory.getInventorySize(), 0);
        assertEquals(0, inventory.getCoins());
    }

    @Test
    public void addItemTest() {
        Item i = new Hint();

        inventory.addItem(i);
        assertEquals(1, inventory.getInventorySize());
        assertSame(i, inventory.getItem(0));
        inventory.addItem(i);
        assertEquals(2, inventory.getInventorySize());
        assertSame(i, inventory.getItem(1));

        Item breaker = new Breaker(3);
        inventory.addItem(breaker);
        assertEquals(3, inventory.getInventorySize());
        assertSame(breaker, inventory.getItem(2));
        inventory.addItem(breaker);
        assertEquals(4, inventory.getInventorySize());
        assertSame(breaker, inventory.getItem(3));
    }

    // @Test
    // public void addItemOverLimitTest() {
    //     Item i = new Hint();

    //     for (int j = 0; j < 9; j++) {
    //         inventory.addItem(i);
    //         assertEquals(j + 1, inventory.getInventorySize());
    //     }

    //     assertEquals(9, inventory.getInventorySize());
    //     inventory.addItem(i);
    //     assertEquals(9, inventory.getInventorySize());
    // }

    @Test
    public void getItemTest() {
        Item i = new Hint();
        inventory.addItem(i);
        assertEquals(1, inventory.getInventorySize());

        Item returned = inventory.getItem(0);
        assertEquals("Hint", returned.getDisplayName());
        assertSame(i, returned);
    }

    @Test
    public void getItemNonExistTest() {
        assertEquals(0, inventory.getInventorySize());

        Item i = inventory.getItem(9);
        assertNull(i);
    }

    @Test
    public void removeItemTest() {
        Item i = new Hint();
        inventory.addItem(i);
        assertEquals(1, inventory.getInventorySize());

        inventory.removeItem(0);
        assertEquals(0, inventory.getInventorySize());

        for (int j = 0; j < 9; j++) {
            inventory.addItem(i);
            assertEquals(j + 1, inventory.getInventorySize());
        }

        for (int j = 8; j >= 0; j--) {
            inventory.removeItem(j);
            assertEquals(j, inventory.getInventorySize());
        }
    }

    @Test
    public void removeItemOverLimitTest() {
        assertEquals(0, inventory.getInventorySize());
        inventory.removeItem(0);
        assertEquals(0, inventory.getInventorySize());

        Item i = new Hint();
        inventory.addItem(i);
        assertEquals(1, inventory.getInventorySize());

        inventory.removeItem(1);
        assertEquals(1, inventory.getInventorySize());
    }

    @Test
    public void addCoinsTest() {
        inventory.addCoins(1);
        assertEquals(1, inventory.getCoins());

        inventory.addCoins(5);
        assertEquals(6, inventory.getCoins());
    }
}
