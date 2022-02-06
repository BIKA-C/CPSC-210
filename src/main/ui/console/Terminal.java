package ui.console;

import java.io.IOException;

public class Terminal {
    private Screen screen;

    // REQUIRES: width > 0 and height > 0
    // EFFECTS: creates a new terminal screen with the given width and height
    public Terminal(int width, int height) throws InterruptedException, IOException {
        screen = new Screen(width, height);
        String[] cmd = { "/bin/sh", "-c", "stty raw </dev/tty" };
        Runtime.getRuntime().exec(cmd).waitFor();
    }

    // REQUIRES: getScreen() != null
    // MODIFIES: this
    // EFFECTS: revert the terminal to be the normal state and
    // and set screen to be null. Once the terminal is closed, screen is unusable
    // (you can't write/draw anything anymore)
    public void close() throws InterruptedException, IOException {
        String[] cmd = new String[] { "/bin/sh", "-c", "stty sane </dev/tty" };
        Runtime.getRuntime().exec(cmd).waitFor();
        screen.clear();
        screen = null;
    }

    // REQUIRES: getScreen() != null
    // EFFECTS: return the width of the screen
    public int getScreenWidth() {
        return screen.getWidth();
    }

    // REQUIRES: getScreen() != null
    // EFFECTS: return the height of the screen
    public int getScreenHeight() {
        return screen.getHeight();
    }

    public Screen getScreen() {
        return screen;
    }

    // REQUIRES: getScreen() != null
    // EFFECTS: read a key from the terminal immediately
    // (it does not wait for the enter), if ctrl+c is
    // pressed, this function will end the entire program
    // with a status code of 0
    public int getKey() throws IOException, InterruptedException {
        int key = System.in.read();
        if (key == 3) {
            close();
            System.exit(0);
        }
        return key;
    }

    // REQUIRES: getScreen() != null
    // EFFECTS: returns true if one or more keys is down
    public boolean isKeyDown() throws IOException {
        return System.in.available() > 0;
    }
}
