package ui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import model.Game;
import ui.KeyHandler;

// gamePanel holds both mazePanel and infoPanel.
// it is the base panel for the game
public class GamePanel extends JPanel {

    private MazePanel mazePanel;
    private InfoPanel infoPanel;

    private KeyHandler keyHandler;

    // EFFECTS: cosntructs the panel with the given game and size
    public GamePanel(Game game, Dimension size) {
        super(new BorderLayout());
        this.mazePanel = new MazePanel(game, size);
        this.infoPanel = new InfoPanel(game, size, this);
        this.keyHandler = new KeyHandler(game, this);

        super.setPreferredSize(size);
        super.addKeyListener(keyHandler);
        super.add(mazePanel, BorderLayout.CENTER);
        super.add(infoPanel, BorderLayout.LINE_END);
        super.setFocusable(true);
        super.setRequestFocusEnabled(true);
        super.requestFocusInWindow();
    }

    // MODIFIES: this
    // EFFECTS: set the new game to all of it's sub panels
    public void setGame(Game newGame) {
        infoPanel.setGame(newGame);
        mazePanel.setGame(newGame);
        keyHandler.setGame(newGame);
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }
}
