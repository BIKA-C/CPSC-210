package model.item;

import org.json.JSONObject;

import model.Game;

// Coin represents a coin with a value
public class Coin extends Item {

    private final int value;

    // REQUIRES: value > 0
    // EFFECTS: constructs a Coin with the given value.
    // value can not be modified after initialized.
    // coin can not be deactivated and it is always active
    public Coin(int value) {
        this.value = value;

        isAutoApply = true;
        type = ItemType.COIN;
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

    // EFFECTS: converts the item to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject coin = super.toJson();
        coin.put("value", value);
        return coin;
    }

    @Override
    public String toString() {
        return super.toString() + " value of " + value;
    }
}
