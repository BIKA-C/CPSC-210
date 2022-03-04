package model.utility.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MenuOptionTest {

    @Test
    public void constructorTest() {
        MenuOption option = new MenuOption("Option");
        assertEquals("Option", option.getOption());
        option.setOption("Test");
        assertEquals("Test", option.getOption());
    }
}
