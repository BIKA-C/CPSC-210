package ui.console;

import model.utility.Coordinate;
import model.utility.menu.MessageBoxOption;
import model.utility.pixel.Pixel;
import model.utility.pixel.TextAttribute;

// Screen represents a terminal Screen with a width and height
// this class represents a Screen Pixel as 2 characters to make
// it looks like a square. So x is automatically multiplied by 2
// assuming top right corner is (0, 0)
public class Screen {
    private final int width;
    private final int height;

    private final Pixel[][][] buffers;
    private int currentBuffer;

    // REQUIRES: width > 0 && height > 0
    // EFFECTS: constructs an empty Screen with the given width and height
    // and it clears the terminal screen
    public Screen(int width, int height) {
        this.width = width * 2;
        this.height = height;
        this.buffers = new Pixel[2][height][width * 2];
        this.currentBuffer = 0;
        clear();
    }

    // EFFECTS: clear all special text attribute
    public void clearAllAttribute() {
        System.out.print("\033[0m");
    }

    // EFFECTS: clears the screen and all text attribute
    // set the cursor to be visible
    public void clear() {
        System.out.print("\033[H\033[2J");
        setCursorVisibale();
    }

    // REQUIRES: coord.getX() >= 0 && coord.getX() < getWidth() &&
    // coord.getY() >= 0 && coord.getY() < getHeight() &&
    // EFFECTS: set the cursor the the given Coordinate pos
    public void setCursor(Coordinate coord) {
        System.out.print("\033[" + coord.getY() + ";" + coord.getX() * 2 + "H");
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // EFFECTS: set the cursor the the given x y
    public void setCursor(int x, int y) {
        System.out.print("\033[" + y + ";" + x * 2 + "H");
    }

    // EFFECTS: put the cursor back to (0, 0)
    public void resetCursor() {
        System.out.print("\033[0;0H");
    }

    // EFFECTS: put the current buffer to the screen
    // after write or writePixel, render should be called
    // to render the buffer to the screen.
    // if render() is called twice without write or writePixel
    // in between, you will have a empty screen
    // this function has a little diff algorithm, so if it detects
    // a pixel that is not changed(deep) between two render,
    // this pixel will not be re-rendered (it will be skipped)
    public void render() {
        Pixel pixel;
        Pixel previousPixel;
        int previousBuffer = currentBuffer ^ 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixel = buffers[currentBuffer][i][j];
                previousPixel = buffers[previousBuffer][i][j];
                if (previousPixel != null && (previousPixel == pixel || previousPixel.isSame(pixel))) {
                    continue;
                }
                if (pixel == null) {
                    pixel = Pixel.EMPTY_PIXEL;
                }
                drawExact(pixel.getCharacter(), j + 1, i + 1, pixel.getAttribute());
            }
        }

        this.currentBuffer ^= 1;
        clearCurrentBuffer();
    }

    // EFFECTS: set the cursor to be invisible
    public void setCursorInvisible() {
        System.out.print("\033[?25l");
    }

    // EFFECTS: set the cursor to be visible
    public void setCursorVisibale() {
        System.out.print("\033[?25h");
    }

    // EFFECTS: set the cursor to be visible/invisible by the
    // boolean visibility
    public void setCursorVisiblity(boolean visibility) {
        if (visibility) {
            setCursorVisibale();
        } else {
            setCursorInvisible();
        }
    }

    // REQUIRES: coord.getX() >= 0 && coord.getX() < getWidth() &&
    // coord.getY() >= 0 && coord.getY() < getHeight() &&
    // EFFECTS: write Pixel p on to the background buffer at the given position.
    // If wide it will draw it twice, else an EMPTY_PIXEL will be placed
    // requires render() to render the buffer to the screen
    public void writePixel(Pixel p, Coordinate coord, boolean wide) {
        writePixel(p, coord.getX(), coord.getY(), wide);
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // EFFECTS: write Pixel p on to the background buffer at the given position.
    // If wide it will draw it twice, else an EMPTY_PIXEL will be placed
    // requires render() to render the buffer to the screen
    public void writePixel(Pixel p, int x, int y, boolean wide) {
        buffers[currentBuffer][y][x * 2] = p;
        if (wide) {
            buffers[currentBuffer][y][(x * 2) + 1] = p;
        } else {
            buffers[currentBuffer][y][(x * 2) + 1] = Pixel.EMPTY_PIXEL;
        }
    }

    // REQUIRES: coord.getX() >= 0 && coord.getX() < getWidth() &&
    // coord.getY() >= 0 && coord.getY() < getHeight() &&
    // s.length() + coord.getX() < getWidth()
    // EFFECTS: write s onto the buffer with the given position
    // and style. If wide and s.length() == 1, it will draw the string twice
    // if not wide and s.length() == 1 and not exact, an EMPTY_PIXEL will be added
    // requires render() to render the buffer to the screen
    // if exact, x position will not be multiplied by 2 (for writing actual strings)
    public void write(String s, Coordinate coord, TextAttribute style, boolean wide, boolean exact) {
        write(s, coord.getX(), coord.getY(), style, wide, exact);
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // s.length() + x < getWidth()
    // EFFECTS: write s onto the buffer with the given position
    // and style. If wide and s.length() == 1, it will draw the string twice
    // if not wide and s.length() == 1 and not exact, an EMPTY_PIXEL will be added
    // requires render() to render the buffer to the screen
    // if exact, x position will not be multiplied by 2 (for writing actual strings)
    public void write(String s, int x, int y, TextAttribute style, boolean wide, boolean exact) {
        Pixel pixel;
        int confirmX = x;
        if (!exact) {
            confirmX *= 2;
        }

        for (int i = 0; i < s.length(); i++) {
            pixel = new Pixel(s.charAt(i), style);
            buffers[currentBuffer][y][confirmX + i] = pixel;
        }
        if (exact) {
            return;
        }

        if (wide && s.length() == 1) {
            buffers[currentBuffer][y][x * 2 + 1] = buffers[currentBuffer][y][x * 2];
        } else if (s.length() == 1) {
            buffers[currentBuffer][y][x * 2 + 1] = Pixel.EMPTY_PIXEL;
        }
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // s.length() + x < getWidth()
    // EFFECTS: immediately put s onto the screen with the given position
    // and style. If wide and s.length() == 1 and !exact, it will draw the string
    // twice
    // if not wide and s.length() == 1, an empty space will be added
    // if exact, x position will not be multiplied by 2 (for writing actual strings)
    public void draw(String s, int x, int y, TextAttribute style, boolean wide, boolean exact) {
        if (exact) {
            setCursorExact(x + 1, y + 1);
        } else {
            setCursor(x + 1, y + 1);
        }

        applyStyle(style);
        System.out.print(s);
        if (exact) {
            clearAllAttribute();
            return;
        }
        if (wide && s.length() == 1) {
            System.out.print(s);
        } else if (s.length() == 1) {
            System.out.print(" ");
        }

        clearAllAttribute();
    }

    // REQUIRES: coord.getX() >= 0 && coord.getX() < getWidth() &&
    // coord.getY() >= 0 && coord.getY() < getHeight() &&
    // s.length() + coord.getX() < getWidth()
    // EFFECTS: immediately put s onto the screen with the given position
    // and style. If wide and s.length() == 1 and !exact, it will draw the string
    // twice
    // if not wide and s.length() == 1, an empty space will be added
    // if exact, x position will not be multiplied by 2 (for writing actual strings)
    public void draw(String s, Coordinate coord, TextAttribute style, boolean wide, boolean exact) {
        draw(s, coord.getX(), coord.getY(), style, wide, exact);
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight()
    // EFFECTS: immediately put Pixel p on to the screen with the given position.
    // If wide it will draw it twice, else an EMPTY_PIXEL will be placed
    // if exact, x position will not be multiplied by 2 (for writing actual strings)
    public void drawPixel(Pixel p, int x, int y, boolean wide) {
        draw(Character.toString(p.getCharacter()), x, y, p.getAttribute(), wide, false);
    }

    // REQUIRES: coord.getX() >= 0 && coord.getX() < getWidth() &&
    // coord.getY() >= 0 && coord.getY() < getHeight() &&
    // EFFECTS: immediately put Pixel p on to the screen with the given position.
    // If wide it will draw it twice, else an EMPTY_PIXEL will be placed
    public void drawPixel(Pixel p, Coordinate coord, boolean wide) {
        drawPixel(p, coord.getX(), coord.getY(), wide);
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // x + max(title.length, options[i].length) +10 for all i is < getWidth() and
    // y + options.length + 5 is < getHeight()
    // EFFECTS: write an menu to the buffer with given title and options
    // if selected is with in the range of (0, options.length),
    // the options[selected] will be styled reversely (background color to foreground vice-versa)
    public void writeMenu(int x, int y, String title, String[] options, int selected) {
        int optionsMaxLength = maxLength(options);
        int maxLength = title.length() > optionsMaxLength + 3 ? title.length() : optionsMaxLength + 3;
        writeTextBox(x, y, maxLength + 10, options.length + 1 + 4, false, true);

        int titleX = (maxLength + 10 - title.length()) / 2 + x;
        int optionsX = (maxLength + 10 - optionsMaxLength - 3) / 2 + x;

        write(title, titleX, y + 1, TextAttribute.DEFAULT, false, true);
        int line = y + 3;
        for (int i = 0; i < options.length; i++) {
            if (options[i].length() < optionsMaxLength) {
                options[i] += (new String(new char[optionsMaxLength - options[i].length()]).replace("\0", " "));
            }
            if (i == selected) {
                write(i + 1 + ".  " + options[i], optionsX, line++, TextAttribute.REVERSED, false, true);
                continue;
            }
            write(i + 1 + ".  " + options[i], optionsX, line++, TextAttribute.DEFAULT, false, true);
        }
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // x + max(title.length, message.length) + 10 is < getWidth() and
    // y + 7 < getHeight()
    // EFFECTS: write a messageBox to the buffer with the given title and message
    // and the given option at the given x and y (x is precise)
    public void writeMessageBox(String title, String message, MessageBoxOption option, int x, int y) {
        int maxLength = title.length() > message.length() ? title.length() : message.length();
        int optionsMaxLength = option.getLength();
        maxLength = maxLength > optionsMaxLength ? maxLength : optionsMaxLength;
        int actualLength = maxLength + 10;

        writeTextBox(x, y, maxLength + 10, 7, true, false);

        for (int i = 0; i < maxLength + 10 - 2; i++) {
            write(" ", x + i + 1, y + 1, TextAttribute.REVERSED, false, true);
            write(" ", x + i + 1, y + 2, TextAttribute.REVERSED, false, true);
            write(" ", x + i + 1, y + 3, TextAttribute.REVERSED, false, true);
            write(" ", x + i + 1, y + 4, TextAttribute.REVERSED, false, true);
            write(" ", x + i + 1, y + 5, TextAttribute.REVERSED, false, true);
        }

        title += (new String(new char[actualLength - title.length() - 1]).replace("\0", " "));
        write(title, x + 1, y + 1, TextAttribute.REVERSED, false, true);

        message += (new String(new char[actualLength - message.length() - 1]).replace("\0", " "));
        write(message, x + 1, y + 3, TextAttribute.REVERSED, false, true);

        if (option == MessageBoxOption.YES_NO_CANCEL) {
            write("Yes(y)  No(n)  Cancel(c)", actualLength - 24 + x - 3, y + 5, TextAttribute.REVERSED, false, true);
        } else if (option == MessageBoxOption.OK) {
            write("OK(enter)", actualLength - 9 + x - 3, y + 5, TextAttribute.REVERSED, false, true);
        }
    }

    // REQUIRES: x >= 0 && x < getWidth() &&
    // y >= 0 && y < getHeight() &&
    // x + width < getWdith() &&
    // y + height < getHeight()
    // MODIFIES: write a textBox to the buffer with the width and height
    // at the given x and y (x is precise)
    // if contrase, the textBox will be in reversed color
    // if showBorderLine, the textBox will have a border
    public void writeTextBox(int x, int y, int width, int height, boolean contrast, boolean showBorderLine) {
        TextAttribute style = contrast ? TextAttribute.REVERSED : TextAttribute.DEFAULT;
        String corner = showBorderLine ? "+" : " ";
        String borderV = showBorderLine ? "|" : " ";
        String borderH = showBorderLine ? "-" : " ";
        for (int i = 0; i < width; i++) {
            if (i == 0 || i == width - 1) {
                write(corner, i + x, 0 + y, style, false, true);
                write(corner, i + x, height + y - 1, style, false, true);
            } else {
                write(borderH, i + x, 0 + y, style, false, true);
                write(borderH, i + x, height + y - 1, style, false, true);
            }
        }

        for (int i = 0; i < height; i++) {
            if (i == 0 || i == height - 1) {
                continue;
            } else {
                write(borderV, 0 + x, i + y, style, false, true);
                write(borderV, width + x - 1, i + y, style, false, true);
            }
        }

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // REQUIRES: x >= 0 && x < getWidth() * 2 && y >= o && y <= getHeight
    // EFFECTS: set the cursor to the given x y position. This position is
    // exact. Meaning x is not multiplied by 2
    private void setCursorExact(int x, int y) {
        System.out.print("\033[" + y + ";" + x + "H");
    }

    // REQUIRES: style != null
    // EFFECTS: print the style to the console. All the following
    // text will apply the style until clearAllAttribute() is called
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

    // REQUIRES: x >= 0 && x < getWidth() -1 &&
    // y >= 0 && y < getHeight() &&
    // EFFECTS: immediately put c onto the screen with the given position
    // and style. This position is exact, meaning x is not multiplied by 2
    // and c will not be drew twice
    private void drawExact(char c, int x, int y, TextAttribute style) {
        setCursorExact(x, y);

        applyStyle(style);
        System.out.print(c);

        clearAllAttribute();
    }

    // REQUIRES: buffers != null
    // MODIFIES: this
    // EFFECTS: set all pixels in current buffer to be Pixel.EMPTY_PIXEL
    private void clearCurrentBuffer() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buffers[currentBuffer][i][j] = Pixel.EMPTY_PIXEL;
            }
        }
    }

    // EFFECTS: return the max length of string in the strings array
    private int maxLength(String[] strings) {
        int max = 0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length() > max) {
                max = strings[i].length();
            }
        }
        return max;
    }
}
