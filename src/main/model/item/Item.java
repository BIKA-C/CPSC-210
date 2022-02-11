package model.item;

import model.Game;

// Item is a game item that can be used to provide
// some  effect to the maze or the player
public interface Item {

    // MODIFIES: game, this
    // EFFECTS: apply the effect to the game
    public void apply(Game g);


    // MODIFIES: game, this
    // EFFECTS: true if the item will be auto-applied after picking up
    public boolean isAutoApply();

    // REQUIRES: autoApply(g) == false
    // EFFECTS: returns the item name.
    public String getName();

    // REQUIRES: autoApply(g) == false
    // EFFECTS: return the item information
    public String report();

    // // MODIFIES: this
    // // EFFECTS: make the item inactive, so it is no longer usable.
    // // If the item is already inactive, the function will do nothing
    // public void deactivate();

    // // EFFECTS: true if the item is active, false if it is not
    // public boolean isActive();
}
