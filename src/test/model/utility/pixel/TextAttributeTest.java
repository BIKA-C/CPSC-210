package model.utility.pixel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TextAttributeTest {
    private TextAttribute randomStyle;

    private TextAttribute notDefaultForeground;
    private TextAttribute notDefaultBackground;
    private TextAttribute notDefaultStyle;

    private TextAttribute notDefaultForegroundBackground;
    private TextAttribute notDefaultForegroundStyle;
    private TextAttribute notDefaultBackgroundStyle;

    @BeforeEach
    public void setup() {
        randomStyle = new TextAttribute(200, 200, 1);

        notDefaultForeground = new TextAttribute(12, TextAttribute.DEFAULT_VALUE, TextAttribute.DEFAULT_VALUE);
        notDefaultBackground = new TextAttribute(TextAttribute.DEFAULT_VALUE, 12, TextAttribute.DEFAULT_VALUE);
        notDefaultStyle = new TextAttribute(TextAttribute.DEFAULT_VALUE, TextAttribute.DEFAULT_VALUE, 1);

        notDefaultForegroundBackground = new TextAttribute(12, 12, TextAttribute.DEFAULT_VALUE);
        notDefaultForegroundStyle = new TextAttribute(12, TextAttribute.DEFAULT_VALUE, 1);
        notDefaultBackgroundStyle = new TextAttribute(TextAttribute.DEFAULT_VALUE, 12, 1);
    }

    @Test
    public void setterAndGetterTest() {
        randomStyle.setForegroundColor(90);
        assertEquals(90, randomStyle.getForegroundColor());
        randomStyle.setBackgroundColor(20);
        assertEquals(20, randomStyle.getBackgroundColor());
        randomStyle.setStyle(4);
        assertEquals(4, randomStyle.getStyle());
    }

    @Test
    public void isDefaultTest() {
        assertTrue(TextAttribute.DEFAULT.isDefault());

        assertFalse(randomStyle.isDefault());

        assertFalse(notDefaultForeground.isDefault());
        assertFalse(notDefaultBackground.isDefault());
        assertFalse(notDefaultStyle.isDefault());
        assertFalse(notDefaultForegroundBackground.isDefault());
        assertFalse(notDefaultForegroundStyle.isDefault());
        assertFalse(notDefaultBackgroundStyle.isDefault());
    }

    @Test
    public void isDefaultForegroundTest() {
        assertTrue(TextAttribute.DEFAULT.isDefaultForeground());

        assertFalse(randomStyle.isDefaultForeground());

        assertFalse(notDefaultForeground.isDefaultForeground());
        assertTrue(notDefaultBackground.isDefaultForeground());
        assertTrue(notDefaultStyle.isDefaultForeground());
        assertFalse(notDefaultForegroundBackground.isDefaultForeground());
        assertFalse(notDefaultForegroundStyle.isDefaultForeground());
        assertTrue(notDefaultBackgroundStyle.isDefaultForeground());
    }

    @Test
    public void isDefaultBackgroundTest() {
        assertTrue(TextAttribute.DEFAULT.isDefaultBackground());

        assertFalse(randomStyle.isDefaultBackground());

        assertTrue(notDefaultForeground.isDefaultBackground());
        assertFalse(notDefaultBackground.isDefaultBackground());
        assertTrue(notDefaultStyle.isDefaultBackground());
        assertFalse(notDefaultForegroundBackground.isDefaultBackground());
        assertTrue(notDefaultForegroundStyle.isDefaultBackground());
        assertFalse(notDefaultBackgroundStyle.isDefaultBackground());
    }

    @Test
    public void isDefaultStyleTest() {
        assertTrue(TextAttribute.DEFAULT.isDefaultStyle());

        assertFalse(randomStyle.isDefaultStyle());

        assertTrue(notDefaultForeground.isDefaultStyle());
        assertTrue(notDefaultBackground.isDefaultStyle());
        assertFalse(notDefaultStyle.isDefaultStyle());
        assertTrue(notDefaultForegroundBackground.isDefaultStyle());
        assertFalse(notDefaultForegroundStyle.isDefaultStyle());
        assertFalse(notDefaultBackgroundStyle.isDefaultStyle());
    }

    @Test
    public void isSameTest() {
        TextAttribute a = new TextAttribute(1,2,3);
        TextAttribute b = new TextAttribute(1,2,3);
        TextAttribute c = new TextAttribute(1,2,3);
        TextAttribute d = new TextAttribute(1,2,4);
        TextAttribute e = new TextAttribute(1,4,3);
        TextAttribute f = new TextAttribute(4,2,3);

        assertFalse(a.isSame(d));
        assertFalse(b.isSame(d));
        assertFalse(c.isSame(d));

        assertFalse(a.isSame(e));
        assertFalse(a.isSame(f));

        assertTrue(a.isSame(a));
        assertTrue(b.isSame(b));

        assertTrue(a.isSame(b));
        assertTrue(b.isSame(a));

        assertTrue(a.isSame(b));
        assertTrue(b.isSame(c));
        assertTrue(a.isSame(c));

        a.setBackgroundColor(7);
        assertTrue(a.isSame(a));

        b.setStyle(0);
        assertTrue(b.isSame(b));
    }
}
