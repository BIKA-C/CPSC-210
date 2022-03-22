package ui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.Game;

public class GamePanel extends JPanel {

    private MazePanel mazePanel;
    private InfoPanel infoPanel;

    public GamePanel(Game game, Dimension size) {
        super(new BorderLayout());
        mazePanel = new MazePanel(game, size);
        infoPanel = new InfoPanel(game, size);
        super.setPreferredSize(size);
        super.add(mazePanel, BorderLayout.CENTER);
        super.add(infoPanel, BorderLayout.LINE_END);
    }
}
