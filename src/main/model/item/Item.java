package model.item;

import org.json.JSONObject;
import org.json.JSONString;

import model.Game;

// Item is a game item that can be used to provide
// some  effect to the maze or the player
public abstract class Item implements JSONString {

    protected String displayName;
    protected boolean isAutoApply;
    protected String reportMessage;

    protected ItemType type;

    // MODIFIES: game, this
    // EFFECTS: apply the effect to the game
    public abstract void apply(Game g);

    // EFFECTS: true if the item will be auto-applied after picking up
    public boolean isAutoApply() {
        return isAutoApply;
    }

    // EFFECTS: returns the item name.
    // if isAtuoApply(), null will be returned
    public String getDisplayName() {
        return isAutoApply ? null : displayName;
    }

    // EFFECTS: return the item information
    // if isAtuoApply(), null will be returned
    public String report() {
        return isAutoApply ? null : reportMessage;
    }

    public JSONObject toJson() {
        JSONObject item = new JSONObject();
        item.put("type", type);
        item.put("autoApply", isAutoApply);
        return item;
    }

    // // MODIFIES: this
    // // EFFECTS: make the item inactive, so it is no longer usable.
    // // If the item is already inactive, the function will do nothing
    // public void deactivate();

    // // EFFECTS: true if the item is active, false if it is not
    // public boolean isActive();
}
