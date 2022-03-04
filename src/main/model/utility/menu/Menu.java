package model.utility.menu;

import java.util.ArrayList;

// represents a menu that has a title and MenuOptions
// a boolean to represent it's status
public class Menu {
    private boolean quit;
    private String title;
    private ArrayList<MenuOption> options;

    // EFFECTS: constructs a menu with not quit status
    public Menu(String title, ArrayList<MenuOption> options) {
        quit = false;
        this.title = title;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<MenuOption> getOptions() {
        return options;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
