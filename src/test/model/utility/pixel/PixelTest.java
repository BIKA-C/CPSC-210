package model.utility.pixel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PixelTest {
    private Pixel pixel;

    @BeforeEach
    public void setup() {
        TextAttribute textAttribute = TextAttribute.DEFAULT;
        pixel = new Pixel('A', textAttribute);
    }

    @Test
    public void settersTest() {
        pixel.setCharacter('c');
        assertEquals('c', pixel.getCharacter());

        TextAttribute style = new TextAttribute(12, 12, 3);
        pixel.setAttribute(style);
        assertTrue(style.isSame(pixel.getAttribute()));
    }

    @Test
    public void isSameTest() {

        TextAttribute styleA = TextAttribute.DEFAULT;
        TextAttribute styleB = new TextAttribute(12, 12, 6);
        TextAttribute styleC = new TextAttribute(12, 13, 6);
        TextAttribute styleD = new TextAttribute(12, 12, 3);

        Pixel a = new Pixel('a', styleB);
        Pixel b = new Pixel('a', styleB);
        Pixel c = new Pixel('a', styleB);
        Pixel d = new Pixel('c', styleA);

        assertFalse(a.isSame(d));
        assertFalse(b.isSame(d));
        assertFalse(c.isSame(d));

        assertTrue(a.isSame(a));
        assertTrue(b.isSame(b));

        assertTrue(a.isSame(b));
        assertTrue(b.isSame(a));

        assertTrue(a.isSame(b));
        assertTrue(b.isSame(c));
        assertTrue(a.isSame(c));

        a.setAttribute(styleC);
        assertFalse(a.isSame(b));
        a.setAttribute(styleD);
        assertFalse(a.isSame(b));
    }
}
