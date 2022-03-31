package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

import model.Event;
import model.EventLog;
import model.Game;
import ui.panel.GamePanel;

// the gui maze game
public class MazeGame extends JFrame {

    private GamePanel gamePanel;

    public static final Color BACKGROUND_COLOR = new Color(4, 28, 50);
    public static final Color WALL_COLOR = new Color(0, 146, 202).darker();
    public static final Color EXIT_COLOR = new Color(228, 63, 90);
    public static final Color ITEM_COLOR = new Color(23, 183, 148);
    public static final Color PLAYER_COLOR = new Color(241, 208, 10);

    public static final String DATA_STORAGE = "./data/";
    public static final String FILE_EXTENSION = ".json";

    public static final boolean DEBUG = false;
    public static final Border DEBUG_BORDER = BorderFactory.createLineBorder(Color.RED);

    // prepare the game and the gui
    public MazeGame() {
        super("Maze Game");
        Game game = new Game(40);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = determWindowSize(game);

        super.setLayout(new BorderLayout());
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setSize(window);

        gamePanel = new GamePanel(game, window);

        super.add(gamePanel, BorderLayout.CENTER);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event);
                }
                super.windowClosing(e);
            }
        });
        super.pack();
        super.setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    //EFFECTS: start the game
    public void start() {
        super.setVisible(true);
    }

    //EFFECTS: determine the proper window size based on the game(maze) size
    private Dimension determWindowSize(Game game) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = new Dimension((screen.width * 8 / 9), screen.height * 6 / 7);
        // Dimension window = new Dimension(screen.width, screen.height * 6 / 7);

        int left = window.height % game.getMazeSize();
        if (left >= game.getMazeSize() / 2) {
            window.height += game.getMazeSize() - left;
        } else if (left < game.getMazeSize() / 2) {
            window.height -= left;
        }
        return window;
    }
}