package model.utility.menu;

// it represents some menuOptions
public class MenuOption {

    public static final MenuOption NEWGAME = new MenuOption("New Game");
    public static final MenuOption LOADGAME = new MenuOption("Load Game");
    public static final MenuOption QUIT = new MenuOption("Quit");
    public static final MenuOption OTHER = new MenuOption("");

    private String option;

    public MenuOption(String option) {
        this.option = option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

}
