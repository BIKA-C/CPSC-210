package ui.panel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.json.JSONException;

import model.Game;
import model.item.Item;
import model.player.Inventory;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.MazeGame;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

// the panel that holds game information
public class InfoPanel extends JPanel {

    private Game game;
    private GamePanel gamePanel;
    private String gameLoaded = null;

    private Dimension size;

    private JPanel heading;
    private JPanel inventory;
    private JPanel info;
    private JPanel message;
    private JPanel instructions;

    private JLabel solved;
    private JLabel coins;
    private JLabel numItems;
    private JLabel direction;
    private JLabel position;
    private JLabel gameMessage;
    private JPanel items;

    private final Color backgroundColor = MazeGame.BACKGROUND_COLOR.brighter();
    private final Color buttonColor = backgroundColor.brighter();

    private final Font defaultFont = new Font("Serif", Font.PLAIN, 18);
    private final Font title = new Font("Serif", Font.BOLD, 24);
    private final Font subTitle = new Font("Serif", Font.BOLD, 22);

    private static final int LINE_BREAK = 10;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private JFileChooser jfc = new JFileChooser(MazeGame.DATA_STORAGE, FileSystemView.getFileSystemView());

    // EFFECTS: constructs a infoPanel
    public InfoPanel(Game game, Dimension size, GamePanel panel) {
        super();
        this.game = game;
        this.gamePanel = panel;
        this.size = new Dimension(size.width - size.height, size.height);
        init();

        super.setBackground(backgroundColor);
        super.setPreferredSize(this.size);
        super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        super.add(Box.createHorizontalStrut(size.width * 5 / 100));
        super.add(generateInfoPanel());
    }

    // MODIFIES: this
    // EFFECTS: initialize the fileds
    private void init() {
        this.heading = createHeadingSection();
        this.inventory = createInventorySection();
        this.info = createInfoSection();
        this.message = createMessageSection();
        this.instructions = createInstructionSection();
        initFileChooser();
    }

    // MODIFIES: this
    // EFFECTS: initialize the JFileChooser
    private void initFileChooser() {
        jfc.setDialogTitle("Select a file to load");
        jfc.setApproveButtonText("Load");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("json", "json"));
    }

    // EFFECTS: return an invisible box with the given height
    // used to break up te text
    private Component lineBreak(int height) {
        return Box.createVerticalStrut(height);
    }

    // EFFECTS: create the heading secion of the info panel
    private JPanel createHeadingSection() {
        JPanel heading = sectionPanelTemplate();
        this.solved = labelTemplate("You have solve:  " + game.getPlayer().getSolved(), defaultFont);
        heading.add(labelTemplate("A nice maze game", title));
        heading.add(lineBreak(LINE_BREAK));
        heading.add(solved);
        return heading;
    }

    // EFFECTS: create the heading secion of the info panel
    private JPanel createInventorySection() {
        JPanel inventory = sectionPanelTemplate();
        this.coins = labelTemplate("Coins: " + game.getPlayer().getInventory().getCoins(), defaultFont);
        this.numItems = labelTemplate("You have " + game.getPlayer().getInventory().getInventorySize() + "/"
                + Inventory.NUM_RESTRICT + " items:", defaultFont);
        this.items = craeteItemsPanel();
        inventory.add(labelTemplate("Inventory", subTitle));
        inventory.add(lineBreak(LINE_BREAK));
        inventory.add(coins);
        inventory.add(lineBreak(LINE_BREAK));
        inventory.add(numItems);
        inventory.add(lineBreak(LINE_BREAK));
        inventory.add(items);
        return inventory;
    }

    // EFFECTS: create the items secion of the info panel
    private JPanel craeteItemsPanel() {
        JPanel items = sectionPanelTemplate();

        for (int i = 0; i < game.getPlayer().getInventory().getInventorySize(); i++) {
            Item item = game.getPlayer().getInventory().getItem(i);
            items.add(labelTemplate((i + 1) + ". " + item.getDisplayName(), defaultFont));
        }
        return items;
    }

    // EFFECTS: create the info secion of the info panel
    private JPanel createInfoSection() {
        JPanel info = sectionPanelTemplate();
        this.direction = labelTemplate("Direction: " + game.getPlayer().getDirection(), defaultFont);
        this.position = labelTemplate("Position:  " + game.getPlayer().getPosition(), defaultFont);
        info.add(labelTemplate("Info", subTitle));
        info.add(lineBreak(LINE_BREAK));
        info.add(direction);
        info.add(lineBreak(LINE_BREAK));
        info.add(position);
        return info;
    }

    // EFFECTS: create the message secion of the info panel
    private JPanel createMessageSection() {
        JPanel message = sectionPanelTemplate();
        this.gameMessage = labelTemplate(game.getGameMessage(), defaultFont, MazeGame.ITEM_COLOR);
        message.add(labelTemplate("Message", subTitle));
        message.add(lineBreak(LINE_BREAK));
        message.add(gameMessage);
        return message;
    }

    // EFFECTS: create the instruction secion of the info panel
    private JPanel createInstructionSection() {
        JPanel instruction = sectionPanelTemplate();
        instruction.add(labelTemplate("WASD to move, 1-9 to use items", defaultFont));
        instruction.add(lineBreak(LINE_BREAK + 10));

        JPanel saveLoadUI = sectionPanelTemplate();
        saveLoadUI.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        saveLoadUI.add(labelTemplate("You can ", defaultFont));
        saveLoadUI.add(buttonTemplate("Save", saveButtonActionListener()));
        saveLoadUI.add(labelTemplate(" or ", defaultFont));
        saveLoadUI.add(buttonTemplate("Load", loadButtonActionListener()));

        instruction.add(saveLoadUI);

        return instruction;
    }

    // EFFECTS: returns an actionListener that is responsible for saving
    private ActionListener saveButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        };
    }

    // EFFECTS: returns an actionListener that is responsible for loading
    private ActionListener loadButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                gameLoaded = MazeGame.DATA_STORAGE + jfc.getSelectedFile().getName();
                try {
                    game = JsonReader.parseGame(gameLoaded);
                    gamePanel.setGame(game);
                    System.out.println("Game loaded");
                    updateItems();
                    gamePanel.repaint();
                } catch (JSONException | IOException e1) {
                    JOptionPane.showMessageDialog(null,
                            e1.getMessage(),
                            "File not loaded",
                            JOptionPane.ERROR_MESSAGE);
                }
                gamePanel.requestFocusInWindow();
            }
        };
    }

    // EFFECTS: returns a panel with some pre-settings for the infoPanel
    private JPanel sectionPanelTemplate() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //xxx
        if (MazeGame.DEBUG) {
            panel.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        return panel;
    }

    // EFFECTS: returns a button with the given text and listener
    // and some pre-settings for the infoPanel
    private JButton buttonTemplate(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(defaultFont);
        button.setForeground(Color.WHITE);
        button.setBackground(buttonColor);
        button.addActionListener(listener);
        return button;
    }

    // EFFECTS: returns a label with the given str and font
    // and some pre-settings for the infoPanel
    private JLabel labelTemplate(String str, Font font) {
        JLabel label = new JLabel(str, JLabel.LEFT);
        label.setForeground(Color.WHITE);
        label.setFont(font);
        return label;
    }

    // EFFECTS: returns a label with the given str and font and color
    // and some pre-settings for the infoPanel
    private JLabel labelTemplate(String str, Font font, Color color) {
        JLabel label = new JLabel(str, JLabel.LEFT);
        label.setForeground(Color.WHITE);
        label.setForeground(color);
        label.setFont(font);
        return label;
    }

    // REQUIRES: init() has been called before this function
    // MODIFIES: this
    // EFFECTS: return a new panel with a colletion of all sections
    private JPanel generateInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(backgroundColor);

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(heading);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(inventory);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(info);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(message);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(instructions);
        infoPanel.add(Box.createVerticalGlue());

        //xxx
        if (MazeGame.DEBUG) {
            infoPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        return infoPanel;
    }

    // REQUIRES: init() has been called before this function
    // MODIFIES: this
    // EFFECTS: saves the game to the file
    public void saveGame() {
        String gameToSave = gameLoaded == null ? MazeGame.DATA_STORAGE + getTimeStamp() + MazeGame.FILE_EXTENSION
                : gameLoaded;
        try {
            JsonWriter.saveToFile(gameToSave, game);
            gameLoaded = gameLoaded == null ? gameToSave : gameLoaded;
            JOptionPane.showMessageDialog(null,
                    "Game Saved!",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "File not saved",
                    JOptionPane.ERROR_MESSAGE);

        }
        gamePanel.requestFocusInWindow();
    }

    // EFFECTS: return a string of the local time
    // in the form of yyyy-mm-dd hh:mm:ss
    private String getTimeStamp() {
        return dtf.format(LocalDateTime.now());
    }

    // REQUIRES: init() has been called before this function
    // MODIFIES: g
    // EFFECTS: paint the components, not managed by this class
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.solved.setText("You have solve:  " + game.getPlayer().getSolved());
        this.direction.setText("Direction: " + game.getPlayer().getDirection());
        this.position.setText("Position:  " + game.getPlayer().getPosition());
        this.coins.setText("Coins: " + game.getPlayer().getInventory().getCoins());
        this.numItems.setText("You have " + game.getPlayer().getInventory().getInventorySize() + "/"
                + Inventory.NUM_RESTRICT + " items:");
        this.gameMessage.setText(game.getGameMessage());
    }

    // REQUIRES: init() has been called before this function
    // MODIFIES: this
    // EFFECTS: upate the items list
    public void updateItems() {
        items.removeAll();
        for (int i = 0; i < game.getPlayer().getInventory().getInventorySize(); i++) {
            Item item = game.getPlayer().getInventory().getItem(i);
            this.items.add(labelTemplate((i + 1) + ". " + item.getDisplayName(), defaultFont));
            if (item.getDisplayName() == null) {
                System.out.println(item.getType());
            }
        }
        items.revalidate();
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
