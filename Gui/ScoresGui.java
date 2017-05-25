package Gui;

import GameModel.Difficulty;
import GameModel.Game;
import GameModel.Multiplayer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 *
 * @author Ethan
 */
public class ScoresGui extends javax.swing.JPanel {

    private JFrame frame;
    private Game game;
    private Difficulty difficulty;
    private Multiplayer multiplayer;
    private WelcomeGui welcome;
    /**
     * Creates new form ScoresGui
     */
    public ScoresGui(JFrame frame, ArrayList<String> scores, Game game, Difficulty difficulty, Multiplayer multiplayer) {
        this.frame = frame;
        this.game = game;
        this.difficulty = difficulty;
        this.multiplayer = multiplayer;
        
        initComponents();
        if(multiplayer == Multiplayer.TWO){
            btnPlayAgain.setVisible(false);
        }
        String [] results = new String[scores.size()];
        for(int i  = 0; i < scores.size(); i++){
            results[i] = scores.get(i);
        }
        listScores.setListData(results);
        listScores.clearSelection();
        listScores.setToolTipText(null);
        listScores.setVisibleRowCount(20);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        btnPlayAgain = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listScores = new javax.swing.JList<>();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setBackground(new java.awt.Color(0, 204, 255));

        jLabel1.setText("Highscores:");

        btnPlayAgain.setText("Play Again");
        btnPlayAgain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayAgainActionPerformed(evt);
            }
        });

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        listScores.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listScores);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 220, Short.MAX_VALUE)
                        .addComponent(btnPlayAgain)
                        .addGap(18, 18, 18)
                        .addComponent(btnMenu))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPlayAgain)
                    .addComponent(btnMenu))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        welcome = new WelcomeGui(frame);
        frame.remove(this);
        frame.setPreferredSize(new Dimension(welcome.getWidth(), welcome.getHeight()+25));
        frame.add(welcome, BorderLayout.CENTER);
        frame.pack();
        frame.revalidate();
        frame.repaint();
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2 - frame.getWidth()/2, screenHeight/2 - frame.getHeight()/2);
        
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnPlayAgainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayAgainActionPerformed
        game.createNewGame();
        MainGui mainGui = new MainGui(frame, game, difficulty, multiplayer);
        frame.remove(this);
        frame.setSize(mainGui.getSize());
        frame.add(mainGui, BorderLayout.CENTER);
        frame.pack();
        frame.revalidate();
        frame.repaint();
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2 - frame.getWidth()/2, screenHeight/2 - frame.getHeight()/2);
    }//GEN-LAST:event_btnPlayAgainActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnPlayAgain;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listScores;
    // End of variables declaration//GEN-END:variables
}
