package ui.console;

public class Pixel {
    private final char character;
    private final TextAttribute attribute;

    public static final Pixel EMPTY_PIXEL = new Pixel(' ', TextAttribute.DEFAULT);

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
}
