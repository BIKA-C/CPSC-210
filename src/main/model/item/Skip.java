package model.item;

import model.Game;

// Skip represents a in-game skip item
// that will allow the player to skip the
// current maze
public class Skip implements Item {

    // EFFECTS: constructs a skip
    public Skip() {
    }

    // REQUIRES: g != null
    // MODIFIES: g
    // EFFECTS: g.getMaze() will return a new maze with
    // player's position updated to new maze's start position
    // report the maze is skipped from g.getGameMessage()
    @Override
    public void apply(Game g) {
        g.nextLevel(true);
        g.setGameMessage("Maze skipped!");
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
        return "Skip this maze";
    }

    // EFFECTS: return item description
    @Override
    public String report() {
        return "You got a Skip";
    }

}
