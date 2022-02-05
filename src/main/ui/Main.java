package ui;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ConsoleApp game = new ConsoleApp();
        game.start();
        // System.out.print("\033[?25l");
        // System.in.read();
        // String[] cmd = { "/bin/sh", "-c", "stty raw </dev/tty" };
        // Runtime.getRuntime().exec(cmd).waitFor();

        // System.out.println("\u001b[4mHelloWorld!");
        // System.out.print("\033[0;2H");
        // System.out.print("hit a key: ");
        // Screen screen = new Screen(20, 20);
        // TextAttribute style = new TextAttribute(15, 10, 4);
        // TextAttribute blank = new TextAttribute(0, 0, 0);
        // screen.draw('A', 10, 10, style, true);
        // screen.write("HHHH", 0, 0, style);
        // screen.write("HHHH", 0, 1, blank);
        // screen.render();
        // System.in.read();
        // screen.render();
        // screen.draw('H', 0, 0, style, false);
        // screen.draw('H', 1, 0, style, false);

        // int x = 0;
        // int y = 1;
        // System.out.print("\033[" + y + ";" + x * 2 + "H");
        // System.out.print("1234567");
        // // System.in.read();
        // System.out.print("\033[0;0H");
        // System.in.read();
        // System.out.print("\033[0;1H");
        // System.in.read();
        // System.out.print("\033[0;2H");
        // screen.draw('I', 0, 1, style, false);
        // screen.draw('I', 1, 1, style, false);

        // screen.draw('J', 0, 2, style, false);
        // screen.draw('J', 1, 2, style, false);

        // int key;
        // do {
        //     key = System.in.read();
        //     System.out.println(key);
        // } while (key != 3);
        // System.out.print("\u001b[" + style.getStyle() + "m");
        // System.out.print("\u001b[38;5;" + style.getForegroundColor() + "m");
        // System.out.print("\u001b[48;5;" + style.getForegroundColor() + "m");
        // System.out.print('c');
        // cmd = new String[] { "/bin/sh", "-c", "stty sane </dev/tty" };
        // Runtime.getRuntime().exec(cmd).waitFor();
    }
}
