package model.item;

import model.Game;

// Hint represents an in game hints
// that gives some hint to the player
public class Hint extends Item {

    // EFFECTS: constructs a hint
    public Hint() {
        isAutoApply = false;
        name = "Hint";
        reportMessage = "You got a Hint";
    }

    // REQUIRES: g != null
    // MODIFIES: g
    // EFFECTS: report the exit position by g.getGameMessage()
    @Override
    public void apply(Game g) {
        g.setGameMessage("Exit Position: " + g.getMaze().getExit());
    }
}
