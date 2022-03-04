package model.utility.menu;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class MenuTest {

    private Menu menu;

    @Test
    public void constructorTest() {
        ArrayList<MenuOption> options = new ArrayList<>();
        options.add(new MenuOption("Test"));
        menu = new Menu("ABC", options);

        assertFalse(menu.isQuit());
        assertEquals("ABC", menu.getTitle());
        assertSame(options, menu.getOptions());

        menu.setQuit(true);
        assertTrue(menu.isQuit());
        menu.setTitle("new");
        assertEquals("new", menu.getTitle());
    }

}
