package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;

import model.Game;
import ui.panel.GamePanel;

public class MazeGame extends JFrame {

    private Game game;
    private GamePanel gamePanel;

    public static final Color BACKGROUND_COLOR = new Color(4, 28, 50);

    public MazeGame() {
        super("Maze Game");
        game = new Game(40);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = determWindowSize();

        super.setLayout(new BorderLayout());
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setSize(window);

        gamePanel = new GamePanel(game, window);

        super.add(gamePanel, BorderLayout.CENTER);
        super.addKeyListener(new KeyHandler(game, gamePanel));

        super.pack();
        super.setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
        super.setVisible(true);
    }

    private Dimension determWindowSize() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = new Dimension(screen.width * 6 / 7, screen.height * 6 / 7);

        int left = window.height % game.getMazeSize();
        if (left >= game.getMazeSize() / 2) {
            window.height += game.getMazeSize() - left;
        } else if (left < game.getMazeSize() / 2) {
            window.height -= left;
        }
        return window;
    }
}