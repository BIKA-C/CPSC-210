package model.player;

import java.util.ArrayList;

import model.item.Item;

public class Inventory {
    private ArrayList<Item> inventory;
    private int coins;

    public Inventory() {
        inventory = new ArrayList<>();
        coins = 0;
    }

    public void addItem(Item i) {
        inventory.add(i);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public Item getItem(int i) {
        if (i >= inventory.size()) {
            return null;
        }
        return inventory.get(i);
    }

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
