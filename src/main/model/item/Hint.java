package model.item;

import model.Game;

// Hint represents an in game hints
// that gives some hint to the player
public class Hint extends Item {

    // EFFECTS: constructs a hint
    public Hint() {
        isAutoApply = false;
        displayName = "Hint";
        reportMessage = "You got a Hint";

        type = ItemType.HINT;
    }

    // REQUIRES: g != null
    // MODIFIES: g
    // EFFECTS: report the exit position by g.getGameMessage()
    @Override
    public void apply(Game g) {
        g.setGameMessage("Exit Position: " + g.getMaze().getExit());
    }

    @Override
    public String toJSONString() {
        return super.toJson().toString();
    }
}
