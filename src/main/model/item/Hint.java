package model.item;

import model.Game;

// Hint represents a in game hints
// that gives some hint to the player
public class Hint implements Item {

    // EFFECTS: constructs a hint
    public Hint() {

    }

    // REQUIRES: g != null
    // MODIFIES: g
    // EFFECTS: report the exit position by g.getGameMessage()
    @Override
    public void apply(Game g) {
        g.setGameMessage("Exit Position: " + g.getMaze().getExit());
    }

    // EFFECTS: this item will not be auto-applied
    // after picking up
    @Override
    public boolean isAutoApply() {
        return false;
    }

    // EFFECTS: return the item name
    @Override
    public String getName() {
        return "Hint";
    }

    // EFFECTS: return the item description
    @Override
    public String report() {
        return "You got a Hint";
    }
}
