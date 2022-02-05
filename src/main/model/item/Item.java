package model.item;

import model.Game;

public interface Item {

    // REQUIRES: isActive() == true
    // MODIFIES: game, this
    // EFFECTS: apply the effect to the game
    public void apply(Game g);

    // MODIFIES: this
    // EFFECTS: active the item, so it is useable. If the item is
    // already actived, the function will do nothing
    public void activate();

    // MODIFIES: this
    // EFFECTS: make the item inactive, so it is no longer useable.
    // If the item is already inactive, the function will do nothing
    public void deactivate();

    // EFFECTS: true if the item is active, false if it is not
    public boolean isActive();

    // EFFECTS: true if this item is a coin
    // public boolean isCoin();

    // REQUIRES isCoin()
    // return the value of the coin
    // public int coinValue();
}
