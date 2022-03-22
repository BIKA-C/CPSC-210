package ui.panel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Game;
import ui.MazeGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class InfoPanel extends JPanel {

    private Game game;

    private JLabel sovled;

    public InfoPanel(Game game, Dimension size) {
        super(new BorderLayout());
        super.setBackground(MazeGame.BACKGROUND_COLOR.brighter());
        this.setPreferredSize(new Dimension(size.width - size.height, size.height));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
