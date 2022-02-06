package model.item;

import model.Game;

public class Hint implements Item {

    @Override
    public void apply(Game g) {
        g.setItemMessage("Exit Position: " + g.getMaze().getExit());
    }

    @Override
    public boolean autoApply(Game g) {
        g.setItemMessage("You got a Hint");
        return false;
    }

    @Override
    public String getName() {
        return "Hint";
    }

}
