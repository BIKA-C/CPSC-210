package ui.console;

// Pixel represents a Pixel on the console screen
// it has a character that can be printed and
// TextAttribute which describes it's style
public class Pixel {
    private final char character;
    private final TextAttribute attribute;

    public static final Pixel EMPTY_PIXEL = new Pixel(' ', TextAttribute.DEFAULT);

    // EFFECTS: constructs a pixel with the given character and attribute
    public Pixel(char character, TextAttribute attribute) {
        this.character = character;
        this.attribute = attribute;
    }

    public TextAttribute getAttribute() {
        return attribute;
    }

    public char getCharacter() {
        return character;
    }

    // REQUIRES: p != null
    // EFFECTS: returns true if the given pixel has the same character and style
    public boolean isSame(Pixel p) {
        return p.character == character && attribute.isSame(p.attribute);
    }
}
