package ui.console;

public class TextAttribute {
    private int foregroundColor;
    private int backgroundColor;
    private int style;

    public static final TextAttribute DEFAULT = new TextAttribute(-1, -1, -1);

    // REQUIRES: foregroundColor >= && foregroundColor <= 255 &&
    // backgroundColor >= 0 && backgroundColor <= 255 &&
    // style >= 0 && style <= 8
    // EFFECTS: constructs a textAttribute with the given properties
    // it follows console output style
    // if (foregroundColor >= && foregroundColor <= 255 &&
    // backgroundColor >= 0 && backgroundColor <= 255 &&
    // style >= 0 && style <= 8) is false, default will be applied

    public TextAttribute(int foregroundColor, int backgroundColor, int style) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.style = style;
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

    // REQUIRES: foregroundColor >= 0 && foregroundColor <= 255
    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    // REQUIRES: background >= 0 && background <= 255
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    // REQUIRES: style >= 0 && style <= 8
    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isDefault() {
        return foregroundColor < 0 && foregroundColor < 0 && style < 0;
    }

    public boolean isDefaultForeground() {
        return foregroundColor < 0;
    }

    public boolean isDefaultBackground() {
        return backgroundColor < 0;
    }

    public boolean isDefaultStyle() {
        return style < 0;
    }
}
