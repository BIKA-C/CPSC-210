package ui.console;

import java.io.IOException;

// Terminal represents a terminal, it has a screen
// and it receives key inputs
public class Terminal {
    private Screen screen;

    // REQUIRES: width > 0 and height > 0
    // EFFECTS: creates a new terminal screen with the given width and height
    // if anything causes InterruptedException or IOException, the program terminates
    // with a status code 1;
    public Terminal(int width, int height) {
        screen = new Screen(width, height);
        String[] cmd = { "/bin/sh", "-c", "stty raw </dev/tty" };
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // EFFECTS: return true if the terminal is closed
    public boolean isClosed() {
        return screen == null;
    }

    // REQUIRES: getScreen() != null
    // MODIFIES: this
    // EFFECTS: revert the terminal to be the normal state and
    // and set screen to be null. Once the terminal is closed, screen is unusable
    // (you can't write/draw anything anymore)
    // if anything causes InterruptedException or IOException, the program terminates
    // with a status code 1;
    public void close() {
        String[] cmd = new String[] { "/bin/sh", "-c", "stty sane </dev/tty" };
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
    // if anything causes InterruptedException or IOException, the program terminates
    // with a status code 1;
    public int getKey() {
        int key = -1;
        try {
            key = System.in.read();
        } catch (IOException e) {
            close();
            e.printStackTrace();
            System.exit(0);
        }
        if (key == 3) {
            close();
            System.exit(0);
        }
        return key;
    }

    // REQUIRES: getScreen() != null
    // EFFECTS: returns true if one or more keys is down
    public boolean isKeyDown() {
        boolean keydown = false;
        try {
            keydown = System.in.available() > 0;
        } catch (IOException e) {
            return false;
        }

        return keydown;
    }
}
