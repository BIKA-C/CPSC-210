package model.item;

import model.Game;

// Skip represents an in-game skip item
// that will allow the player to skip the
// current maze
public class Skip extends Item {

    // EFFECTS: constructs a skip
    public Skip() {
        isAutoApply = false;
        displayName = "Skip this maze";
        reportMessage = "You got a Skip";

        type = ItemType.SKIP;
    }

    // REQUIRES: g != null
    // MODIFIES: g
    // EFFECTS: g.getMaze() will return a new maze with
    // player's position updated to new maze's start position.
    // and report the maze is skipped from g.getGameMessage()
    @Override
    public void apply(Game g) {
        g.nextLevel(true, false);
        g.setGameMessage("Maze skipped!");
    }
}
