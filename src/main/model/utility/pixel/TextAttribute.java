package model.utility.pixel;

// TextAttribute describes the console text style. It has foreground color
// background color and text style bold, underline etc. It follows the
// 255 color terminal output style
public class TextAttribute {
    private int foregroundColor;
    private int backgroundColor;
    private int style;

    public static final TextAttribute DEFAULT = new TextAttribute(-1, -1, -1);
    public static final TextAttribute REVERSED = new TextAttribute(-1, -1, 7);
    public static final int DEFAULT_VALUE = -1;

    // EFFECTS: constructs a textAttribute with the given properties
    // it follows the 255 color terminal output style
    // if (foregroundColor >= && foregroundColor <= 255 &&
    // backgroundColor >= 0 && backgroundColor <= 255 &&
    // style >= 0 && style <= 8) is false, default will be applied
    public TextAttribute(int foregroundColor, int backgroundColor, int style) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.style = style;
    }

    // EFFECTS: returns true if all isDefaultForeground(), isDefaultBackground, and
    // isDefaultStyle are true
    public boolean isDefault() {
        return foregroundColor < 0 && backgroundColor < 0 && style < 0;
    }

    // EFFECTS: true if getForegroundColor() < 0
    public boolean isDefaultForeground() {
        return foregroundColor < 0;
    }

    // EFFECTS: true if getBackgroundColor() < 0
    public boolean isDefaultBackground() {
        return backgroundColor < 0;
    }

    // EFFECTS: true if getStyle() < 0
    public boolean isDefaultStyle() {
        return style < 0;
    }

    // EFFECTS: true if getForegroundColor() == t.getForegroundColor() &&
    // getBackgroundColor() == t.getBackgroundColor() &&
    // getStyle() == t.getStyle()
    public boolean isSame(TextAttribute t) {
        return foregroundColor == t.foregroundColor && backgroundColor == t.backgroundColor && style == t.style;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getStyle() {
        return style;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
