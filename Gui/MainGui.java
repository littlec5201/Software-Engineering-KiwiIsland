package Gui;

import GameModel.Difficulty;
import GameModel.Game;
import GameModel.GameEventListener;
import GameModel.GameState;
import GameModel.MoveDirection;
import GameModel.Multiplayer;
import GameModel.Player;
import GameModel.Score;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author Ethan
 */
public class MainGui extends javax.swing.JPanel implements GameEventListener, ActionListener {

    private JFrame frame;
    private Game game;
    private Difficulty difficulty;
    private Timer timer;
    private Timer panelSwitchTimer;
    private int timeMin = 0;
    private int timeSec = 0;
    private Multiplayer multiplayer;
    private MainGui otherPlayer;
    private boolean canMove = true;

    /**
     * Creates new form MainGui
     */
    public MainGui(JFrame frame, Game game, Difficulty difficulty, Multiplayer multiplayer) {

        this.setPreferredSize(new Dimension(780, 685));
        frame.setPreferredSize(this.getPreferredSize());
        this.frame = frame;
        this.game = game;
        this.multiplayer = multiplayer;
        this.panelSwitchTimer = new Timer(500, this);
        this.difficulty = difficulty;

        setAsGameListener();
        
        initComponents();

        initialiseKeyBindings();

        setUpLists();

        txtPlayerName.setEditable(false);
        initIslandGrid();
        update();

        if (difficulty == Difficulty.EASY) {
            txtDifficulty.setText("Easy");
        }
        if (difficulty == Difficulty.MEDIUM) {
            txtDifficulty.setText("Medium");
        }
        if (difficulty == Difficulty.HARD) {
            txtDifficulty.setText("Hard");
            timer = new Timer(1000, this);
            labelTime.setText("Time: ");
            timer.start();
            labelKiwisKilled.setText("Kiwis killed:");
            textKiwisKilled.setText("0");
        }

        pnlIsland.setFocusable(true);
        pnlIsland.requestFocusInWindow();
    }

    public void setPlayerOneColour() {
        jPanel1.setBackground(new java.awt.Color(0, 204, 255));
        jPanel2.setBackground(new java.awt.Color(0, 204, 255));
        pnlIsland.setBackground(new java.awt.Color(0, 204, 255));
        this.setBackground(new java.awt.Color(0, 204, 255));
    }

    public void setPlayerTwoColour() {
        jPanel1.setBackground(Color.RED);
        jPanel2.setBackground(Color.RED);
        pnlIsland.setBackground(Color.RED);
        this.setBackground(Color.RED);
    }

    public void setOtherPlayer(MainGui otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public void setUpLists() {
        listInventory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listInventory.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listInventoryValueChanged(evt);
            }
        });
        listInventory.setVisibleRowCount(20);
        listObjects.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listObjects.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listObjectsValueChanged(evt);
            }
        });
        listObjects.setVisibleRowCount(20);
    }

    public void switchPanel() {
        frame.remove(this);
        frame.add(otherPlayer);
        frame.revalidate();
        frame.repaint();
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - frame.getWidth() / 2, screenHeight / 2 - frame.getHeight() / 2);
    }

    @Override
    public void gameStateChanged() {
        update();
        if (difficulty == Difficulty.HARD) {
            txtKiwisKilled.setText("" + game.getKiwisKilled());
        }
        
        // check for "game over" or "game won"
        if (game.getState() == GameState.LOST) {
            if (difficulty == Difficulty.HARD) {
                timer.stop();
            }
            if(multiplayer == Multiplayer.ONE){
                JOptionPane.showMessageDialog(
                        this,
                        game.getLoseMessage(), "Game over!",
                        JOptionPane.INFORMATION_MESSAGE);
                
                game.removeGameEventListener(this);
                ArrayList<String> results = game.viewScores();
                ScoresGui scoresGui = new ScoresGui(frame, results, game, difficulty, multiplayer);
                frame.remove(this);
                frame.add(scoresGui, BorderLayout.CENTER);
                frame.pack();
                frame.revalidate();
                frame.repaint();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                frame.setLocation(screenWidth / 2 - frame.getWidth() / 2, screenHeight / 2 - frame.getHeight() / 2);
                frame.requestFocus();
            }else{
                JOptionPane.showMessageDialog(
                        this,
                        "You have lost. The other player wins!", "Game over!",
                        JOptionPane.INFORMATION_MESSAGE);
                game.removeGameEventListener(this);
                ArrayList<String> results = game.viewScores();
                ScoresGui scoresGui = new ScoresGui(frame, results, game, difficulty, multiplayer);
                frame.remove(this);
                frame.add(scoresGui, BorderLayout.CENTER);
                frame.pack();
                frame.revalidate();
                frame.repaint();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                frame.setLocation(screenWidth / 2 - frame.getWidth() / 2, screenHeight / 2 - frame.getHeight() / 2);
            }
                
        } else if (game.getState() == GameState.WON) {
            if(difficulty == Difficulty.HARD){
                timer.stop();
            }
            if(multiplayer == Multiplayer.ONE){
                int result = JOptionPane.showConfirmDialog(
                        this,
                        game.getWinMessage(), "Well Done!",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {

                    if (game.saveScores()) {
                        JOptionPane.showMessageDialog(this, "Score has been saved!", "Score Saved!", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Score could not be saved...", "Score not saved...", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                game.removeGameEventListener(this);
                ArrayList<String> results = game.viewScores();
                ScoresGui scoresGui = new ScoresGui(frame, results, game, difficulty, multiplayer);
                frame.remove(this);
                frame.add(scoresGui, BorderLayout.CENTER);
                frame.pack();
                frame.revalidate();
                frame.repaint();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                frame.setLocation(screenWidth / 2 - frame.getWidth() / 2, screenHeight / 2 - frame.getHeight() / 2);
            }else{
                JOptionPane.showMessageDialog(
                        this,
                        "You have won! The other player loses!", "Game over!",
                        JOptionPane.INFORMATION_MESSAGE);
                game.removeGameEventListener(this);
                ArrayList<String> results = game.viewScores();
                ScoresGui scoresGui = new ScoresGui(frame, results, game, difficulty, multiplayer);
                frame.remove(this);
                frame.add(scoresGui, BorderLayout.CENTER);
                frame.pack();
                frame.revalidate();
                frame.repaint();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = screenSize.width;
                int screenHeight = screenSize.height;
                frame.setLocation(screenWidth / 2 - frame.getWidth() / 2, screenHeight / 2 - frame.getHeight() / 2);
            }
        } else if (game.messageForPlayer()) {
            JOptionPane.showMessageDialog(
                    this,
                    game.getPlayerMessage(), "Important Information",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.requestFocus();
        }
    }

    private void setAsGameListener() {
        game.addGameEventListener(this);
    }

    private void update() {
        // update the grid square panels
        Component[] components = pnlIsland.getComponents();
        for (Component c : components) {
            // all components in the panel are GridSquarePanels,
            // so we can safely cast
            GridSquarePanel gsp = (GridSquarePanel) c;
            gsp.update();
        }

        // update player information
        int[] playerValues = game.getPlayerValues();
        txtPlayerName.setText(game.getPlayerName());
        progPlayerStamina.setMaximum(playerValues[Game.MAXSTAMINA_INDEX]);
        progPlayerStamina.setValue(playerValues[Game.STAMINA_INDEX]);
        progBackpackWeight.setMaximum(playerValues[Game.MAXWEIGHT_INDEX]);
        progBackpackWeight.setValue(playerValues[Game.WEIGHT_INDEX]);
        progBackpackSize.setMaximum(playerValues[Game.MAXSIZE_INDEX]);
        progBackpackSize.setValue(playerValues[Game.SIZE_INDEX]);

        //Update Kiwi and Predator information
        txtKiwisCounted.setText(Integer.toString(game.getKiwiCount()));
        txtPredatorsLeft.setText(Integer.toString(game.getPredatorsRemaining()));

        // update inventory list
        listInventory.setListData(game.getPlayerInventory());
        listInventory.clearSelection();
        listInventory.setToolTipText(null);
        btnUse.setEnabled(false);
        btnDrop.setEnabled(false);

        // update list of visible objects
        listObjects.setListData(game.getOccupantsPlayerPosition());
        listObjects.clearSelection();
        listObjects.setToolTipText(null);
        btnCollect.setEnabled(false);
        btnCount.setEnabled(false);

        // update movement buttons
        btnMoveNorth.setEnabled(game.isPlayerMovePossible(MoveDirection.NORTH));
        btnMoveEast.setEnabled(game.isPlayerMovePossible(MoveDirection.EAST));
        btnMoveSouth.setEnabled(game.isPlayerMovePossible(MoveDirection.SOUTH));
        btnMoveWest.setEnabled(game.isPlayerMovePossible(MoveDirection.WEST));
    }

    public void playerPromptedToMove(MoveDirection direction) {
        if (multiplayer == Multiplayer.TWO) {
            if (canMove) {
                game.playerMove(direction);
                panelSwitchTimer.start();
                canMove = false;
            }
        } else {
            game.playerMove(direction);
            frame.requestFocus();
        }
    }

    public void initialiseKeyBindings() {
        Action cAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.countKiwi();
            }
        };

        Action pAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = listObjects.getSelectedValue();
                game.collectItem(obj);
            }
        };

        Action uAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.useItem(listInventory.getSelectedValue());
            }
        };

        Action dAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.dropItem(listInventory.getSelectedValue());
            }
        };
        Action upAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPromptedToMove(MoveDirection.NORTH);
            }
        };
        Action downAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPromptedToMove(MoveDirection.SOUTH);
            }
        };
        Action leftAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPromptedToMove(MoveDirection.WEST);
            }
        };
        Action rightAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerPromptedToMove(MoveDirection.EAST);
            }
        };

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Character.valueOf('c')), "cPressed");
        this.getActionMap().put("cPressed", cAction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Character.valueOf('p')), "pPressed");
        this.getActionMap().put("pPressed", pAction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Character.valueOf('u')), "uPressed");
        this.getActionMap().put("uPressed", uAction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Character.valueOf('d')), "dPressed");
        this.getActionMap().put("dPressed", dAction);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "upPressed");
        this.getActionMap().put("upPressed", upAction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
        this.getActionMap().put("downPressed", downAction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "leftPressed");
        this.getActionMap().put("leftPressed", leftAction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "rightPressed");
        this.getActionMap().put("rightPressed", rightAction);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnUse = new javax.swing.JButton();
        btnDrop = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listInventory = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        progPlayerStamina = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        txtPlayerName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listObjects = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        btnCollect = new javax.swing.JButton();
        btnCount = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        progBackpackSize = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        progBackpackWeight = new javax.swing.JProgressBar();
        pnlIsland = new javax.swing.JPanel();
        btnMoveNorth = new javax.swing.JButton();
        labelTimer = new javax.swing.JLabel();
        labeltime = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kiwisKilled = new javax.swing.JLabel();
        txtKiwisKilled = new javax.swing.JLabel();
        btnMoveWest = new javax.swing.JButton();
        btnMoveEast = new javax.swing.JButton();
        btnMoveSouth = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtDifficulty = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPredatorsLeft = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtKiwisCounted = new javax.swing.JLabel();
        labelKiwisKilled = new javax.swing.JLabel();
        textKiwisKilled = new javax.swing.JLabel();
        labelTime = new javax.swing.JLabel();
        textTime = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 204, 255));
        setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        setPreferredSize(new java.awt.Dimension(500, 20));

        jPanel1.setBackground(new java.awt.Color(0, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 225));

        jLabel1.setText("Inventory:");

        btnUse.setText("Use");
        btnUse.setPreferredSize(new java.awt.Dimension(90, 25));
        btnUse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUseActionPerformed(evt);
            }
        });

        btnDrop.setText("Drop");
        btnDrop.setPreferredSize(new java.awt.Dimension(90, 25));
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
            }
        });

        listInventory.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listInventory.setPreferredSize(new java.awt.Dimension(120, 500));
        jScrollPane1.setViewportView(listInventory);

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel5.setText("Stamina:");

        progPlayerStamina.setPreferredSize(new java.awt.Dimension(150, 20));

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel6.setText("Player:");

        txtPlayerName.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtPlayerName, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progPlayerStamina, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDrop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addComponent(btnUse, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progPlayerStamina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPlayerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 204, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 225));

        listObjects.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listObjects.setMaximumSize(new java.awt.Dimension(33, 300));
        listObjects.setPreferredSize(new java.awt.Dimension(90, 500));
        listObjects.setVisibleRowCount(20);
        jScrollPane2.setViewportView(listObjects);

        jLabel2.setText("Objects:");

        btnCollect.setText("Pick Up");
        btnCollect.setPreferredSize(new java.awt.Dimension(90, 25));
        btnCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollectActionPerformed(evt);
            }
        });

        btnCount.setText("Catch");
        btnCount.setPreferredSize(new java.awt.Dimension(90, 25));
        btnCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCountActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jLabel4.setText("Backpack Capacity:");

        progBackpackSize.setPreferredSize(new java.awt.Dimension(150, 20));

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLabel3.setText("Backpack Weight:");

        progBackpackWeight.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnCount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addComponent(btnCollect, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jLabel4)
                    .addComponent(progBackpackSize, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(progBackpackWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 3, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCollect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progBackpackSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progBackpackWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlIsland.setPreferredSize(new java.awt.Dimension(540, 618));

        javax.swing.GroupLayout pnlIslandLayout = new javax.swing.GroupLayout(pnlIsland);
        pnlIsland.setLayout(pnlIslandLayout);
        pnlIslandLayout.setHorizontalGroup(
            pnlIslandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        pnlIslandLayout.setVerticalGroup(
            pnlIslandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );

        btnMoveNorth.setText("North");
        btnMoveNorth.setActionCommand("");
        btnMoveNorth.setPreferredSize(new java.awt.Dimension(100, 50));
        btnMoveNorth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveNorthActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        kiwisKilled.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        kiwisKilled.setText(" ");

        txtKiwisKilled.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        txtKiwisKilled.setText(" ");

        btnMoveWest.setText("West");
        btnMoveWest.setPreferredSize(new java.awt.Dimension(100, 50));
        btnMoveWest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveWestActionPerformed(evt);
            }
        });

        btnMoveEast.setText("East");
        btnMoveEast.setPreferredSize(new java.awt.Dimension(100, 50));
        btnMoveEast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveEastActionPerformed(evt);
            }
        });

        btnMoveSouth.setText("South");
        btnMoveSouth.setPreferredSize(new java.awt.Dimension(100, 50));
        btnMoveSouth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveSouthActionPerformed(evt);
            }
        });

        jLabel8.setText("Difficulty:");

        txtDifficulty.setText("jLabel10");

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel9.setText("Predators left:");

        txtPredatorsLeft.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        txtPredatorsLeft.setText("0");

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel7.setText("Kiwis caught:");
        jLabel7.setToolTipText("");

        txtKiwisCounted.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        txtKiwisCounted.setText("0");

        labelKiwisKilled.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        textKiwisKilled.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        labelTime.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kiwisKilled)
                        .addGap(77, 77, 77)
                        .addComponent(txtKiwisKilled, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMoveNorth, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMoveSouth, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelTimer)
                                .addGap(105, 105, 105)
                                .addComponent(labeltime))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDifficulty))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlIsland, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtKiwisCounted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(58, 58, 58)
                                        .addComponent(btnMoveWest, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(86, 86, 86)
                                        .addComponent(btnMoveEast, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPredatorsLeft))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(labelKiwisKilled)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textKiwisKilled))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(labelTime)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textTime)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlIsland, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labeltime)
                                    .addComponent(labelTimer))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(kiwisKilled)
                                        .addComponent(txtKiwisKilled)
                                        .addComponent(labelTime)
                                        .addComponent(textTime))))
                            .addComponent(btnMoveNorth, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoveWest, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMoveEast, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(txtPredatorsLeft)
                            .addComponent(jLabel7)
                            .addComponent(txtKiwisCounted))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMoveSouth, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(txtDifficulty)
                            .addComponent(labelKiwisKilled)
                            .addComponent(textKiwisKilled))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnUseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUseActionPerformed
        game.useItem( listInventory.getSelectedValue());
        frame.requestFocus();
    }//GEN-LAST:event_btnUseActionPerformed

    private void btnMoveNorthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveNorthActionPerformed
        playerPromptedToMove(MoveDirection.NORTH);
        frame.requestFocus();
    }//GEN-LAST:event_btnMoveNorthActionPerformed

    private void btnMoveWestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveWestActionPerformed
        playerPromptedToMove(MoveDirection.WEST);
        frame.requestFocus();
    }//GEN-LAST:event_btnMoveWestActionPerformed

    private void btnMoveEastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveEastActionPerformed
        playerPromptedToMove(MoveDirection.EAST);
        frame.requestFocus();
    }//GEN-LAST:event_btnMoveEastActionPerformed

    private void btnMoveSouthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveSouthActionPerformed
        playerPromptedToMove(MoveDirection.SOUTH);
        frame.requestFocus();
    }//GEN-LAST:event_btnMoveSouthActionPerformed

    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        game.dropItem(listInventory.getSelectedValue());
        frame.requestFocus();
    }//GEN-LAST:event_btnDropActionPerformed

    private void btnCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCountActionPerformed
        game.countKiwi();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("kiwiCall.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.requestFocus();
    }//GEN-LAST:event_btnCountActionPerformed

    private void btnCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCollectActionPerformed
        Object obj = listObjects.getSelectedValue();
        game.collectItem(obj);
        frame.requestFocus();
    }//GEN-LAST:event_btnCollectActionPerformed

    private void listObjectsValueChanged(javax.swing.event.ListSelectionEvent evt) {                                         
        Object occ = listObjects.getSelectedValue();
        if ( occ != null )
        {
            btnCollect.setEnabled(game.canCollect(occ));
            btnCount.setEnabled(game.canCount(occ));
            listObjects.setToolTipText(game.getOccupantDescription(occ));
        }
        frame.requestFocus();
    } 
    
    private void listInventoryValueChanged(javax.swing.event.ListSelectionEvent evt) {                                           
        Object item =  listInventory.getSelectedValue();
        btnDrop.setEnabled(true);
        if ( item != null )
        {
            btnUse.setEnabled(game.canUse(item));
            listInventory.setToolTipText(game.getOccupantDescription(item));
        }
        frame.requestFocus();
    }
    
    private void initIslandGrid()
    {
        // Add the grid
        int rows    = game.getNumRows();
        int columns = game.getNumColumns();
        // set up the layout manager for the island grid panel
        pnlIsland.setLayout(new GridLayout(rows, columns));
        // create all the grid square panels and add them to the panel
        // the layout manager of the panel takes care of assigning them to the
        // the right position
        for ( int row = 0 ; row < rows ; row++ )
        {
            for ( int col = 0 ; col < columns ; col++ )
            {
                pnlIsland.add(new GridSquarePanel(game, row, col));
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCollect;
    private javax.swing.JButton btnCount;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnMoveEast;
    private javax.swing.JButton btnMoveNorth;
    private javax.swing.JButton btnMoveSouth;
    private javax.swing.JButton btnMoveWest;
    private javax.swing.JButton btnUse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel kiwisKilled;
    private javax.swing.JLabel labelKiwisKilled;
    private javax.swing.JLabel labelTime;
    private javax.swing.JLabel labelTimer;
    private javax.swing.JLabel labeltime;
    private javax.swing.JList listInventory;
    private javax.swing.JList listObjects;
    private javax.swing.JPanel pnlIsland;
    private javax.swing.JProgressBar progBackpackSize;
    private javax.swing.JProgressBar progBackpackWeight;
    private javax.swing.JProgressBar progPlayerStamina;
    private javax.swing.JLabel textKiwisKilled;
    private javax.swing.JLabel textTime;
    private javax.swing.JLabel txtDifficulty;
    private javax.swing.JLabel txtKiwisCounted;
    private javax.swing.JLabel txtKiwisKilled;
    private javax.swing.JTextField txtPlayerName;
    private javax.swing.JLabel txtPredatorsLeft;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelSwitchTimer) {
            panelSwitchTimer.stop();
            if(game.getState() != GameState.LOST && game.getState() != GameState.WON){
                switchPanel();
                canMove = true;
            }
            
        }
        if (e.getSource() == timer) {
            timeSec++;
            if (timeMin == 5) {
                game.setGameState(GameState.LOST);
                gameStateChanged();
            }
            if (timeSec == 60) {
                timeMin++;
                timeSec = 0;
            }
            if (timeSec < 10) {
                textTime.setText(timeMin + ":0" + timeSec);
            } else {
                textTime.setText(timeMin + ":" + timeSec);
            }
            if (timeMin == 5) {
                game.setGameState(GameState.LOST);
                gameStateChanged();
            }
        }
    }
}
