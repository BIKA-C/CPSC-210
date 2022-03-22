package ui;

import java.awt.event.KeyListener;

import javax.swing.JFrame;

import model.Game;
import model.exceptions.NotRecognizedKeyException;
import model.exceptions.PlayerMovementException;
import model.utility.Direction;
import ui.panel.GamePanel;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyListener {

    private Game game;
    private GamePanel panel;

    public KeyHandler(Game game, GamePanel panel) {
        this.game = game;
        this.panel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            game.movePlayer(e.getKeyChar());
        } catch (PlayerMovementException e1) {
            return;
        }
        panel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

}
