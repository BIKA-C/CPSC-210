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
