package model.player;

import java.util.ArrayList;

import model.item.Item;

// Inventory represents an inventory for the player
// that stores all the items the player picked up
// from the game.
// the inventory bag can store upto 9 items
// except auto-applied items like coins
public class Inventory {
    private ArrayList<Item> inventory;
    private int coins;

    public static final int TERMINAL_GUI_NUM_RESTRICT = 9;

    // EFFECTS: constructs a inventory bag with zero items in
    // and zero coins
    public Inventory() {
        inventory = new ArrayList<>();
        coins = 0;
    }

    // REQUIRES: i != null
    // MODIFIES: this
    // EFFECTS: add i into the inventory
    // if getInventorySize() >= TERMINAL_GUI_NUM_RESTRICT, i will not be added
    public void addItem(Item i) {
        if (inventory.size() >= TERMINAL_GUI_NUM_RESTRICT) {
            return;
        }
        inventory.add(i);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    // REQUIRES: i >= 0
    // EFFECTS: get the item with i index
    // if the index i >= getInventorySize()
    // null will be returned
    public Item getItem(int i) {
        if (i >= inventory.size()) {
            return null;
        }
        return inventory.get(i);
    }

    // REQUIRES: i >= 0
    // MODIFIES: this
    // EFFECTS: remove the item with i index
    // if the index i >= getInventorySize()
    // this function will do nothing
    public void removeItem(int i) {
        if (i >= inventory.size()) {
            return;
        }
        inventory.remove(i);
    }

    // REQUIRES: amount>0
    // MODIFIES: this
    // EFFECTS: add amount of coins
    public void addCoins(int amount) {
        this.coins += amount;
    }

    public int getCoins() {
        return coins;
    }

}
