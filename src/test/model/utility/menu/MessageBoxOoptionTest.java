package model.utility.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MessageBoxOoptionTest {
    @Test
    public void enumTest() {
        MessageBoxOption yesNoCancel = MessageBoxOption.YES_NO_CANCEL;
        assertEquals(24, yesNoCancel.getLength());
        MessageBoxOption ok = MessageBoxOption.OK;
        assertEquals(9, ok.getLength());
    }
}
