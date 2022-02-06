package model.item;

import model.Game;

public class Skip implements Item {

    public Skip() {

    }

    @Override
    public void apply(Game g) {
        g.nextLevel();
    }

    @Override
    public boolean autoApply(Game g) {
        g.setItemMessage("You got a Skip");
        return false;
    }

    @Override
    public String getName() {
        return "Skip this maze";
    }

}
