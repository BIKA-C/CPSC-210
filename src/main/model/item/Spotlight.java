package model.item;

import model.Game;
import model.player.Player;

public class Spotlight implements Item {

    private final Player player;
    private boolean isActive;

    // EFFECTS: Constructs an active spotlight, with a reference to the given
    // player
    public Spotlight(Player player) {
        this.player = player;
        this.isActive = true;
    }

    // REQUIRES: isActive() == true
    // MODIFIES: game, this
    // EFFECTS: make everything visible within its range unless blocked by a wall
    // or exceeds its range
    @Override
    public void apply(Game g) {
        // TODO Auto-generated method stub

    }

    // MODIFIES: this
    // EFFECTS: active the item, so it is usable. If the item is
    // active, the function will do nothing
    @Override
    public void activate() {
        this.isActive = true;
    }

    // MODIFIES: this
    // EFFECTS: deactivate the item, so it is usable. If the item is
    // not active, the function will do nothing
    @Override
    public void deactivate() {
        this.isActive = false;
    }

    // EFFECTS: true if the item is active, false if it is not
    @Override
    public boolean isActive() {
        return this.isActive;
    }

    // @Override
    // public boolean isCoin() {
    //     return false;
    // }

    // @Override
    // public int coinValue() {
    //     return -1;
    // }

}
