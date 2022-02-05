package model.item;

import model.Game;

public class Coin implements Item {

    private final int value;

    // REQUIRES: value > 0
    // EFFECTS: constructs a Coin with the given value.
    // value can not be modified after initialized.
    // coin can not be deactivated and it is always active
    public Coin(int value) {
        this.value = value;
    }

    @Override
    public void apply(Game g) {
        g.getPlayer().getInventory().addCoins(this.value);
    }

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public boolean isActive() {
        return true;
    }

    // @Override
    // public boolean isCoin() {
    //     return true;
    // }

    // @Override
    // public int coinValue() {
    //     return this.value;
    // }

}
