package model.item;

import model.Game;

// Coin represents a coin with a value
public class Coin implements Item {

    private final int value;

    // REQUIRES: value > 0
    // EFFECTS: constructs a Coin with the given value.
    // value can not be modified after initialized.
    // coin can not be deactivated and it is always active
    public Coin(int value) {
        this.value = value;
    }

    // MODIFIES: game
    // EFFECTS: apply the effect to the game,
    // add coin's to the player inventory bag.
    // and report to the game from game.getGameMessage
    @Override
    public void apply(Game g) {
        g.getPlayer().getInventory().addCoins(this.value);
        g.setGameMessage("You got " + this.value + " coins");
    }

    // EFFECTS: this item will be auto applied after
    // picking up
    @Override
    public boolean isAutoApply() {
        return true;
    }

    // EFFECTS: always return null since this item will be
    // auto applied
    @Override
    public String getName() {
        return null;
    }

    // EFFECTS: does nothing since this item will be
    // auto applied
    @Override
    public String report() {
        return null;
    }

    // @Override
    // public boolean isCoin() {
    // return true;
    // }

    // @Override
    // public int coinValue() {
    // return this.value;
    // }

}
