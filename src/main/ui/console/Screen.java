package ui.console;

import model.utility.Coordinate;

public class Screen {
    private final int width;
    private final int height;

    private final Pixel[][][] buffers;
    private int currentBuffer;

    public Screen(int width, int height) {
        this.width = width * 2;
        this.height = height;
        this.buffers = new Pixel[2][height][width * 2];
        this.currentBuffer = 0;
        clear();
    }

    public void clearAllAttribute() {
        System.out.print("\033[0m");
    }

    public void clear() {
        System.out.print("\033[H\033[2J");
        setCursorVisibale();
    }

    public void setCursor(Coordinate pos) {
        System.out.print("\033[" + pos.getY() + ";" + pos.getX() * 2 + "H");
    }

    public void setCursor(int x, int y) {
        System.out.print("\033[" + y + ";" + x * 2 + "H");
    }

    public void resetCursor() {
        System.out.print("\033[0;0H");
    }

    public void render() {

        resetCursor();

        Pixel pixel;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixel = buffers[currentBuffer][i][j];
                if (pixel == null) {
                    pixel = Pixel.EMPTY_PIXEL;
                }
                drawExact(pixel.getCharacter(), j + 1, i + 1, pixel.getAttribute());
            }
        }

        this.currentBuffer = currentBuffer == 0 ? 1 : 0;
        clearCurrentBuffer();
    }

    public void setCursorInvisibale() {
        System.out.print("\033[?25l");
    }

    public void setCursorVisibale() {
        System.out.print("\033[?25h");
    }

    public void setCursorVisiblity(boolean visiblity) {
        if (visiblity) {
            setCursorVisibale();
        } else {
            setCursorInvisibale();
        }
    }

    public void writePixel(Pixel p, Coordinate pos, boolean wide) {
        writePixel(p, pos.getX(), pos.getY(), wide);
    }

    public void writePixel(Pixel p, int x, int y, boolean wide) {
        buffers[currentBuffer][y][x * 2] = p;
        if (wide) {
            buffers[currentBuffer][y][(x * 2) + 1] = p;
        } else {
            buffers[currentBuffer][y][(x * 2) + 1] = Pixel.EMPTY_PIXEL;
        }
    }

    public void write(String s, Coordinate pos, TextAttribute style) {
        write(s, pos.getX(), pos.getY(), style);
    }

    public void write(String s, int x, int y, TextAttribute style) {
        Pixel pixel;
        for (int i = 0; i < s.length(); i++) {
            pixel = new Pixel(s.charAt(i), style);
            buffers[currentBuffer][y][x * 2 + i] = pixel;
        }
    }

    // REQUIRES: x >= 0 && x <= getWidth() && y>= 0 && y <= getHeight()
    // && s.length() + x <= getWidth()
    // EFFECTS: immediately put s onto the screen with the given position
    // and style. If wide, it will draw it twice
    public void draw(String s, int x, int y, TextAttribute style, boolean wide) {
        setCursor(x, y);

        applyStyle(style);
        System.out.print(s);
        if (wide) {
            System.out.print(s);
        }

        clearAllAttribute();
    }

    // REQUIRES: coord.getX() >= 0 && coord.getX() <= getWidth() &&
    // coord.getY()>= 0 && coord.getY() <= getHeight()
    // && s.length() + x <= getWidth()
    // EFFECTS: immediately put s onto the screen with the given position
    // and style. If wide, it will draw it twice
    public void draw(String s, Coordinate coord, TextAttribute style, boolean wide) {
        draw(s, coord.getX(), coord.getY(), style, wide);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void setCursorExact(int x, int y) {
        System.out.print("\033[" + y + ";" + x + "H");
    }

    private void applyStyle(TextAttribute style) {
        if (!style.isDefaultStyle()) {
            System.out.print("\u001b[" + style.getStyle() + "m");
        }
        if (!style.isDefaultForeground()) {
            System.out.print("\u001b[38;5;" + style.getForegroundColor() + "m");
        }
        if (!style.isDefaultBackground()) {
            System.out.print("\u001b[48;5;" + style.getBackgroundColor() + "m");
        }
    }

    private void drawExact(char c, int x, int y, TextAttribute style) {
        setCursorExact(x, y);

        applyStyle(style);
        System.out.print(c);

        clearAllAttribute();
    }

    private void clearCurrentBuffer() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buffers[currentBuffer][i][j] = null;
            }
        }
    }
}
