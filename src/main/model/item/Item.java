package model.item;

import model.Game;

// Item is a game item that can be used to provide
// some  effect to the maze or the player
public abstract class Item {

    protected String name;
    protected boolean isAutoApply;
    protected String reportMessage;

    // MODIFIES: game, this
    // EFFECTS: apply the effect to the game
    public abstract void apply(Game g);

    // EFFECTS: true if the item will be auto-applied after picking up
    public boolean isAutoApply() {
        return isAutoApply;
    }

    // EFFECTS: returns the item name.
    // if isAtuoApply(), null will be returned
    public String getName() {
        return isAutoApply ? null : name;
    }

    // EFFECTS: return the item information
    // if isAtuoApply(), null will be returned
    public String report() {
        return isAutoApply ? null : reportMessage;
    }

    // // MODIFIES: this
    // // EFFECTS: make the item inactive, so it is no longer usable.
    // // If the item is already inactive, the function will do nothing
    // public void deactivate();

    // // EFFECTS: true if the item is active, false if it is not
    // public boolean isActive();
}
