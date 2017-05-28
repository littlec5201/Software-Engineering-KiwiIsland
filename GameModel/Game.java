package GameModel;

/**
 *
 * @author Ethan
 */
import Gui.MainGui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

/**
 * This is the class that knows the Kiwi Island game rules and state and
 * enforces those rules.
 *
 * @author AS
 * @version 1.0 - created Maintenance History August 2011 Extended for stage 2.
 * AS
 */
public class Game {

    //Constants shared with UI to provide player data
    public static final int STAMINA_INDEX = 0;
    public static final int MAXSTAMINA_INDEX = 1;
    public static final int MAXWEIGHT_INDEX = 2;
    public static final int WEIGHT_INDEX = 3;
    public static final int MAXSIZE_INDEX = 4;
    public static final int SIZE_INDEX = 5;
    private Difficulty difficulty;
    private int moveOccupants = 0;

    /**
     * A new instance of Kiwi island that reads data from "IslandData.txt".
     */
    public Game(String name, Difficulty difficulty) {
        this.difficulty = difficulty;
        eventListeners = new HashSet<GameEventListener>();
        this.name = name;
        createNewGame();
    }

    /**
     * Starts a new game. At this stage data is being read from a text file
     */
    public void createNewGame() {
        this.kiwiList = new ArrayList<Occupant>();
        this.predatorList = new ArrayList<Occupant>();
        this.positionList = new ArrayList<Position>();
        totalPredators = 0;
        totalKiwis = 0;
        predatorsTrapped = 0;
        kiwiCount = 0;
        initialiseIslandFromFile("IslandData.txt");
        drawIsland();
        state = GameState.PLAYING;
        winMessage = "";
        loseMessage = "";
        playerMessage = "";
        notifyGameEventListeners();
        totalTurns = 0;
    }

    /**
     * *********************************************************************************************************************
     * Accessor methods for game data
     * **********************************************************************************************************************
     */
    /**
     * Get number of rows on island
     *
     * @return number of rows.
     */
    public int getNumRows() {
        return island.getNumRows();
    }

    /**
     * Get number of columns on island
     *
     * @return number of columns.
     */
    public int getNumColumns() {
        return island.getNumColumns();
    }

    /**
     * Gets the current state of the game.
     *
     * @return the current state of the game
     */
    public GameState getState() {
        return state;
    }

    /**
     * Provide a description of occupant
     *
     * @param whichOccupant
     * @return description if whichOccuoant is an instance of occupant, empty
     * string otherwise
     */
    public String getOccupantDescription(Object whichOccupant) {
        String description = "";
        if (whichOccupant != null && whichOccupant instanceof Occupant) {
            Occupant occupant = (Occupant) whichOccupant;
            description = occupant.getDescription();
        }
        return description;
    }

    /**
     * Gets the player object.
     *
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if possible to move the player in the specified direction.
     *
     * @param direction the direction to move
     * @return true if the move was successful, false if it was an invalid move
     */
    public boolean isPlayerMovePossible(MoveDirection direction) {
        boolean isMovePossible = false;
        // what position is the player moving to?
        Position newPosition = player.getPosition().getNewPosition(direction);
        // is that a valid position?
        if ((newPosition != null) && newPosition.isOnIsland()) {
            // what is the terrain at that new position?
            Terrain newTerrain = island.getTerrain(newPosition);
            // can the playuer do it?
            isMovePossible = player.hasStaminaToMove(newTerrain)
                    && player.isAlive();
        }
        return isMovePossible;
    }

    /**
     * Get terrain for position
     *
     * @param row
     * @param column
     * @return Terrain at position row, column
     */
    public Terrain getTerrain(int row, int column) {
        return island.getTerrain(new Position(island, row, column));
    }

    /**
     * Is this position visible?
     *
     * @param row
     * @param column
     * @return true if position row, column is visible
     */
    public boolean isVisible(int row, int column) {
        return island.isVisible(new Position(island, row, column));

    }

    /**
     * Is this position explored?
     *
     * @param row
     * @param column
     * @return true if position row, column is explored.
     */
    public boolean isExplored(int row, int column) {
        return island.isExplored(new Position(island, row, column));
    }

    //sukim
    public boolean isFauna(int row, int column) {
        boolean isFauna = false;
        if (island.getOccupantStringRepresentation(new Position(island, row, column)).contains("F")) {
            isFauna = true;
        }
        return isFauna;
    }

    //sukim
    public String checkFauna(int row, int column) {
        String faunaName = island.getOccupantName(new Position(island, row, column));
        
        return faunaName;

    }
    //sukim

    public String getFaunaDesc(String fauna) {
        String faunaDesc = "";

        if (fauna.contains("Oystercatcher")) {
            faunaDesc += "<html>" + "<b><u>OysterCatcher:</u></b>" + "<br>" + " a bird that is commonly found along shorelines " + "<br>" + "that wade to look for food in mud and sand."
                    + "" + "<br>" + "Found on coasts around the world. Ranges of " + "<br>" + "39-50cm in length and 72-91 cm in wingspan. " + "<br>" +"<html>";
        }
        if (fauna.contains("Crab")) {
            faunaDesc += "<html>" + "<b><u>Crab:</u> </b>" + "<br>" + "a crustacean that live in the world's ocean," + "<br>" + " fresh water and land."
                    + " Covered in exoskeleton with" + "<br>" + " a pair of claws. Different species range in " + "<br>" + "sizes"
                    + " and can grow up to 4 metres." + "<br>" +"<html>";
        }
        if (fauna.contains("Fernbird")) {
            faunaDesc += "<html>" + "<b><u>Fernbird:</u> </b>" + "<br>" + "rich brown on top and white below with" + "<br>" + " brown spots."
                    + " Mainly travel on feet and occasional" + "<br>" + "short flights of 15 metres. A rare species." + "<br>" + "<html>";
        }
        if (fauna.contains("Heron")) {
            faunaDesc += "<html>" + "<b><u>Heron:</u> </b>" + "<br>" + "Long legged fresh water and coastal bird. Largest " + "<br>" + "species is the Goliath Heron which "
                    + "stands up " + "<br>" + "to 152cm tall. The neck can form the " + "<br>" + "shape of an S." + "<br>" + "<html>";
        }
        if (fauna.contains("Robin")) {
            faunaDesc += "<html>" + "<b><u>NZ Robin:</u></b>" + "<br>" + "also known as toutouwai and can only" + "<br>" + "be found in New Zealand."
                    + " Can survive up to " + "<br>" + "14 years where few predators exists. Small in " + "<br>" + "size like a sparrow. Enjoys"
                    + " forests with dense," + "<br>" + "even, canopies and ground covered with leaf litter." + "<br>" +"<html>";
        }
        if (fauna.contains("Tui")) {
            faunaDesc += "<html>" + "<b><u>Tui:</u></b>" + "<br>" + "are medium in size and are usually very" + "<br>" + " vocal. From a distance looks black in colour "
                    + "<br>" + "but in good light have a blue, green " + "<br>" + "and bronze iridescent sbeen and a white tuff" + "<br>" + " at the throat. Usually nest"
                    + " in native forest " + "<br>" + "and shrub. Notoriously aggressive and will defend a " + "<br>" + "flowering tree." + "<br>" +"<html>";
        }
        if(fauna.contains("Fern") && !fauna.contains("Fernbird")) {
            faunaDesc += "<html>" + "<b><u>Silver Fern:</u></b>" + "<br>" + "known to grow 10 metres or more. The" + "<br>" + " crown is dense, and the fronds tend to" + "<br>" + " be about 4 metres long and have a "
                    + "<br>" + "silver-white colouration on the undersides. It" + "<br>" + " has been the symbol of New Zealand's national" + "<br>" + " rugby team since the 1880s." + "<br>" +"<html>";
        }
        if (fauna.contains("Manuka")) {
            faunaDesc += "<html>" + "<b><u>Manuka:</u></b>" + "<br>" + "typically a shrurb of 2-5 metres but can " + "<br>" + "grow up to 15 metres"
                    + " The flowers are " + "<br>" + "white occasionally pink. It is cultivated in NZ " + "<br>" + "for Manuka honey produced"
                    + " when honeybees gather the " + "<br>" + "nectar from its flowers." + "<br>" + "<html>";
        }
        if (fauna.contains("Kauri")) {
            faunaDesc += "<html>" + "<b><u>Kauri:</u></b>" + "<br>" + "it is the largest by volume species of" + "<br>" + " trees in New Zealand. Standing up to"
                    + "50" + "<br>" + " metres tall. Relies on depriving its competitors " + "<br>" + " nutrition in order to survive. " + "<br>" + "<html>";
        }
        if (fauna.contains("Flax")) {
            faunaDesc += "<html>" + "<b><u>Common Flax:</u></b>" + "<br>" + "grows up to 3 metres with its flower" + "<br>" + " stalks reaching up to 4 metres."
                    + " Support large " + "<br>" + "communities of animals providing shelter and food resource." + "<br>" + " The name given to this by Maori "
                    + "is " + "<br>" + "Harakeke. Harakeke is a actually a lily even " + "<br>" + "though it is called flax." + "<br>" + "<html>";
        }
        return faunaDesc;

    }

    /**
     * Get occupants for player's position
     *
     * @return occupants at player's position
     */
    public Occupant[] getOccupantsPlayerPosition() {
        return island.getOccupants(player.getPosition());
    }

    /**
     * Get string for occupants of this position
     *
     * @param row
     * @param column
     * @return occupant string for this position row, column
     */
    public String getOccupantStringRepresentation(int row, int column) {
        return island.getOccupantStringRepresentation(new Position(island, row, column));
    }

    /**
     * Get values from player for GUI display
     *
     * @return player values related to stamina and backpack.
     */
    public int[] getPlayerValues() {
        int[] playerValues = new int[6];
        playerValues[STAMINA_INDEX] = (int) player.getStaminaLevel();
        playerValues[MAXSTAMINA_INDEX] = (int) player.getMaximumStaminaLevel();
        playerValues[MAXWEIGHT_INDEX] = (int) player.getMaximumBackpackWeight();
        playerValues[WEIGHT_INDEX] = (int) player.getCurrentBackpackWeight();
        playerValues[MAXSIZE_INDEX] = (int) player.getMaximumBackpackSize();
        playerValues[SIZE_INDEX] = (int) player.getCurrentBackpackSize();

        return playerValues;

    }

    /**
     * How many kiwis have been counted?
     *
     * @return count
     */
    public int getKiwiCount() {
        return kiwiCount;
    }

    /**
     * How many predators are left?
     *
     * @return number remaining
     */
    public int getPredatorsRemaining() {
        return totalPredators - predatorsTrapped;
    }

    /**
     * Get contents of player backpack
     *
     * @return objects in backpack
     */
    public Object[] getPlayerInventory() {
        return player.getInventory().toArray();
    }

    /**
     * Get player name
     *
     * @return player name
     */
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Is player in this position?
     *
     * @param row
     * @param column
     * @return true if player is at row, column
     */
    public boolean hasPlayer(int row, int column) {
        return island.hasPlayer(new Position(island, row, column));
    }

    /**
     * Only exists for use of unit tests
     *
     * @return island
     */
    public Island getIsland() {
        return island;
    }

    /**
     * Draws the island grid to standard output.
     */
    public void drawIsland() {
        island.draw();
    }

    /**
     * Is this object collectable
     *
     * @param itemToCollect
     * @return true if is an item that can be collected.
     */
    public boolean canCollect(Object itemToCollect) {
        boolean result = (itemToCollect != null) && (itemToCollect instanceof Item);
        if (result) {
            Item item = (Item) itemToCollect;
            result = item.isOkToCarry();
        }
        return result;
    }

    /**
     * Is this object a countable kiwi
     *
     * @param itemToCount
     * @return true if is an item is a kiwi.
     */
    public boolean canCount(Object itemToCount) {
        boolean result = (itemToCount != null) && (itemToCount instanceof Kiwi);
        if (result) {
            Kiwi kiwi = (Kiwi) itemToCount;
            result = !kiwi.counted();
        }
        return result;
    }

    /**
     * Is this object usable
     *
     * @param itemToUse
     * @return true if is an item that can be collected.
     */
    public boolean canUse(Object itemToUse) {
        boolean result = (itemToUse != null) && (itemToUse instanceof Item);
        if (result) {
            //Food can always be used (though may be wasted)
            // so no need to change result

            if (itemToUse instanceof Tool) {
                Tool tool = (Tool) itemToUse;
                //Traps can only be used if there is a predator to catch
                if (tool.isTrap()) {
                    result = island.hasPredator(player.getPosition());
                } //Screwdriver can only be used if player has a broken trap
                else if (tool.isSuperTrap() && player.hasSuperTrap()) {
                } else if (tool.isScrewdriver() && player.hasTrap()) {
                    result = player.getTrap().isBroken();
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Details of why player won
     *
     * @return winMessage
     */
    public String getWinMessage() {
        return winMessage;
    }

    /**
     * Details of why player lost
     *
     * @return loseMessage
     */
    public String getLoseMessage() {
        return loseMessage;
    }

    /**
     * Details of information for player
     *
     * @return playerMessage
     */
    public String getPlayerMessage() {
        String message = playerMessage;
        playerMessage = ""; // Already told player.
        return message;
    }

    /**
     * Is there a message for player?
     *
     * @return true if player message available
     */
    public boolean messageForPlayer() {
        return !("".equals(playerMessage));
    }

    /**
     * *************************************************************************************************************
     * Mutator Methods
     * **************************************************************************************************************
     */
    /**
     * Picks up an item at the current position of the player Ignores any
     * objects that are not items as they cannot be picked up
     *
     * @param item the item to pick up
     * @return true if item was picked up, false if not
     */
    public boolean collectItem(Object item) {
        boolean success = (item instanceof Item) && (player.collect((Item) item));
        if (success) {
            // player has picked up an item: remove from grid square
            island.removeOccupant(player.getPosition(), (Item) item);

            // everybody has to know about the change
            notifyGameEventListeners();
        }
        return success;
    }

    /**
     * Drops what from the player's backpack.
     *
     * @param what to drop
     * @return true if what was dropped, false if not
     */
    public boolean dropItem(Object what) {
        boolean success = player.drop((Item) what);
        if (success) {
            // player has dropped an what: try to add to grid square
            Item item = (Item) what;
            success = island.addOccupant(player.getPosition(), item);
            if (success) {
                // drop successful: everybody has to know that
                notifyGameEventListeners();
            } else {
                // grid square is full: player has to take what back
                player.collect(item);
            }
        }
        return success;
    }

    /**
     * Uses an item in the player's inventory. This can be food or tool items.
     *
     * @param item to use
     * @return true if the item has been used, false if not
     */
    public boolean useItem(Object item) {
        boolean success = false;
        Position currentPosition = player.getPosition();

        if (item instanceof Food && player.hasItem((Food) item)) //Player east food to increase stamina
        {
            Food food = (Food) item;
            // player gets energy boost from food
            player.increaseStamina(food.getEnergy());
            try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("eatingFood.wav").getAbsoluteFile());
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
            // player has consumed the food: remove from inventory
            player.drop(food);
            // use successful: everybody has to know that
            notifyGameEventListeners();
        } else if (item instanceof Tool) {
            Tool tool = (Tool) item;
            if (tool.isTrap() && !tool.isBroken()) {
                success = trapPredator(currentPosition);
            } else if (tool.isSuperTrap()) {
                //gets player's current position's row and column 
                int row = currentPosition.getRow();
                int col = currentPosition.getColumn();
                Position topCurrent = currentPosition.checkPosition(row - 1, col);
                Position bottCurrent = currentPosition.checkPosition(row + 1, col);
                Position leftCurrent = currentPosition.checkPosition(row, col - 1);
                Position rightCurrent = currentPosition.checkPosition(row, col + 1);
                Position leftTopCurrent = currentPosition.checkPosition(row - 1, col - 1);
                Position rightBottCurrent = currentPosition.checkPosition(row + 1, col + 1);
                Position rightTopCurrent = currentPosition.checkPosition(row - 1, col + 1);
                Position leftBottCurrent = currentPosition.checkPosition(row + 1, col - 1);

                //sets up dialog for selecting direction of where the supertrap covers
                String trapDirection = "";
                Object[] trapDirections = {"| down ", "- across", "\\ left diagonal", "/ right diagonal"};
                trapDirection = (String) JOptionPane.showInputDialog(
                        null,
                        "Select which direction the super trap covers: ",
                        "Set Super Trap",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        trapDirections,
                        trapDirections[0]);
                if ((trapDirection != null) && (trapDirection.length() > 0)) {
                    //if player selects down option then checks for current, also above and below current position for predator
                    if (trapDirection == trapDirections[0]) {
                        if (trapPredator(currentPosition)) {
                            success = trapPredator(currentPosition);
                            this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                        if (trapPredator(topCurrent)) {
                                success = trapPredator(topCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                             } 
                        if (trapPredator(bottCurrent)) {
                                success = trapPredator(bottCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                    } //player selects across then checks for left and right of current position for predator
                    else if (trapDirection == trapDirections[1]) {
                        if (trapPredator(currentPosition)) {
                            success = trapPredator(currentPosition);
                            this.setPlayerMessage("Success! Predator was trapped :)");
                            
                        }
                        if (trapPredator(leftCurrent)) {
                                success = trapPredator(leftCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                        if (trapPredator(rightCurrent)) {
                                success = trapPredator(rightCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                    } //checks for leftdiagonal positions for predators
                    else if (trapDirection == trapDirections[2]) {
                        if (trapPredator(currentPosition)) {
                            success = trapPredator(currentPosition);
                            this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                         if (trapPredator(leftTopCurrent)) {
                                success = trapPredator(leftTopCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                         }
                         if (trapPredator(rightBottCurrent)) {
                                success = trapPredator(rightBottCurrent);
                         }
                    } //checks for right diagonal positions for predators
                    else if (trapDirection == trapDirections[3]) {
                        if (trapPredator(currentPosition)) {
                            this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                        if (trapPredator(rightTopCurrent)) {
                                success = trapPredator(rightTopCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                        }
                        if (trapPredator(leftBottCurrent)) {
                                success = trapPredator(leftBottCurrent);
                                this.setPlayerMessage("Success! Predator was trapped :)");
                         } 

                    }
                    player.drop(tool);
                }
            } else if (tool.isScrewdriver())// Use screwdriver (to fix trap)
            {
                if (player.hasTrap()) {
                    Tool trap = player.getTrap();
                    trap.fix();
                }
            }
        }
        updateGameState();
        return success;
    }

    /**
     * Count any kiwis in this position
     */
    public void countKiwi() {
        //check if there are any kiwis here
        for (Occupant occupant : island.getOccupants(player.getPosition())) {
            if (occupant instanceof Kiwi) {
                Kiwi kiwi = (Kiwi) occupant;
                if (!kiwi.counted()) {
                    kiwi.count();
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
                    //                    Remove kiwi from island
                    if (island.removeOccupant(occupant.getPosition(), occupant)) {
                        System.out.println("Removed kiwi from island");
                    }
                    kiwiCount++;
                }
            }
        }
        updateGameState();
    }

    /**
     * Attempts to move the player in the specified direction.
     *
     * @param direction the direction to move
     * @return true if the move was successful, false if it was an invalid move
     */
    public boolean playerMove(MoveDirection direction) {
        // what terrain is the player moving on currently
        boolean successfulMove = false;
        if (isPlayerMovePossible(direction)) {
            Position newPosition = player.getPosition().getNewPosition(direction);
            Terrain terrain = island.getTerrain(newPosition);

            // move the player to new position
            player.moveToPosition(newPosition, terrain);
            island.updatePlayerPosition(player);
            successfulMove = true;

            totalTurns++;
            
            // Is there a hazard?
            checkForHazard();

            if (difficulty == Difficulty.EASY) {
                difficultyEasy();
            }
            if (difficulty == Difficulty.MEDIUM) {
                difficultyMedium();
            }
            if (difficulty == Difficulty.HARD) {
                difficultyHard();
            }

            updateGameState();
        }
        return successfulMove;
    }

    /**
     * Easy difficulty is standard at the moment Features may be added later on
     */
    public void difficultyEasy() {

    }

    /**
     * Medium difficulty will move the kiwis and predators around the board,
     * making it harder to kill predators. Predators will die if they step on
     * hazards.
     */
    public void difficultyMedium() {
        difficultyEasy();
        if (moveOccupants == 1) {
            occupantMove(kiwiList);
            occupantMove(predatorList);
            moveOccupants = 0;
        } else {
            moveOccupants++;
        }
    }

    /**
     * Sets a timed session
     */
    public void difficultyHard() {
        difficultyMedium();
    }

    /**
     * Callum
     *
     * @param min
     * @param max
     * @return
     */
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * Callum Attempts to move occupants in a list to a new position
     *
     * @param occupantList
     */
    public void occupantMove(ArrayList<Occupant> occupantList) {
        System.out.println("New move----------------------------");
        int count = 0;
        boolean isKiwiList = false;
        if (!occupantList.isEmpty()) {
            if (occupantList.get(0).getStringRepresentation().equals("K")) {
                isKiwiList = true;
                System.out.println("List of kiwis ------------------------------------");
            } else {
                System.out.println("List of predators --------------------------------");
                isKiwiList = false;
            }
        }
        for (Iterator<Occupant> iterator = occupantList.iterator(); iterator.hasNext();) {
            Occupant occ = iterator.next();
            System.out.println("Attempting to move occupant " + (count + 1) + " out of " + (occupantList.size() + 1));
            System.out.println("Current position is (" + occ.getPosition().getRow() + ", " + occ.getPosition().getColumn() + ")");
            String occString = occ.getStringRepresentation();
            int occRow = occ.getPosition().getRow();
            int occColumn = occ.getPosition().getColumn();
            Position northNeighbour = null, southNeighbour = null, eastNeighbour = null, westNeighbour = null;
            ArrayList<Integer> numsAvailable = new ArrayList<Integer>();

            for (Position pos : positionList) {
                String posString = "";
                if (pos.getRow() == occRow - 1 && pos.getColumn() == occColumn) {
                    System.out.println("Found occ north square");
                    posString = island.getOccupantStringRepresentation(pos.getNewPosition(MoveDirection.NORTH));
                    if (!posString.contains("H") || occString.equals("P")) {
                        northNeighbour = pos;
                        northNeighbour.getNewPosition(MoveDirection.NORTH);
                        numsAvailable.add(1);
                        System.out.println("Added north as possible move");
                    }
                }
                if (pos.getRow() == occRow && pos.getColumn() == occColumn + 1) {
                    System.out.println("Found occ east square");
                    posString = island.getOccupantStringRepresentation(pos.getNewPosition(MoveDirection.EAST));
                    if (!posString.contains("H") || occString.equals("P")) {
                        eastNeighbour = pos;
                        eastNeighbour.getNewPosition(MoveDirection.EAST);
                        numsAvailable.add(2);
                        System.out.println("Added east as possible move");
                    }
                }
                if (pos.getRow() == occRow + 1 && pos.getColumn() == occColumn) {
                    System.out.println("Found occ south square");
                    posString = island.getOccupantStringRepresentation(pos.getNewPosition(MoveDirection.SOUTH));
                    if (!posString.contains("H") || occString.equals("P")) {
                        southNeighbour = pos;
                        southNeighbour.getNewPosition(MoveDirection.SOUTH);
                        numsAvailable.add(3);
                        System.out.println("Added south as possible move");
                    }
                }
                if (pos.getRow() == occRow && pos.getColumn() == occColumn - 1) {
                    System.out.println("Found occ west square");
                    posString = island.getOccupantStringRepresentation(pos.getNewPosition(MoveDirection.WEST));
                    if (!posString.contains("H") || occString.equals("P")) {
                        westNeighbour = pos;
                        westNeighbour.getNewPosition(MoveDirection.WEST);
                        numsAvailable.add(4);
                        System.out.println("Added west as possible move");
                    }
                }
                if (numsAvailable.size() == 4) {
                    break;
                }
            }

            while (true) {
                boolean moved = false;
                if (numsAvailable.isEmpty()) {
                    break;
                }
                ArrayList<Occupant> copyList = null;

                if (numsAvailable.isEmpty()) {
                    break;
                }

                int index = randInt(0, numsAvailable.size() - 1);
                int directionNumber = numsAvailable.get(index);
                numsAvailable.remove(index);

                switch (directionNumber) {
                    case 1:
                        if (island.removeOccupant(occ.getPosition(), occ)) {
                            island.addOccupant(northNeighbour, occ);
                            System.out.println(occ.getName() + " moved north");
                            occ.setPosition(northNeighbour);
                            if (difficulty == Difficulty.MEDIUM || difficulty == Difficulty.HARD) {
                                if (island.getOccupantStringRepresentation(northNeighbour).contains("P") && island.getOccupantStringRepresentation(northNeighbour).contains("H")) {
                                    trapPredator(northNeighbour);
                                }
                            }
                            moved = true;
                        }
                        break;
                    case 2:
                        if (island.removeOccupant(occ.getPosition(), occ)) {
                            island.addOccupant(eastNeighbour, occ);
                            System.out.println(occ.getName() + " moved east");
                            if (difficulty == Difficulty.MEDIUM || difficulty == Difficulty.HARD) {
                                if (island.getOccupantStringRepresentation(eastNeighbour).contains("P") && island.getOccupantStringRepresentation(eastNeighbour).contains("H")) {
                                    trapPredator(eastNeighbour);
                                }
                            }
                            moved = true;
                        }
                        break;
                    case 3:
                        if (island.removeOccupant(occ.getPosition(), occ)) {
                            island.addOccupant(southNeighbour, occ);
                            occ.setPosition(southNeighbour);
                            System.out.println(occ.getName() + " moved south");
                            if (difficulty == Difficulty.MEDIUM || difficulty == Difficulty.HARD) {
                                if (island.getOccupantStringRepresentation(southNeighbour).contains("P") && island.getOccupantStringRepresentation(southNeighbour).contains("H")) {
                                    trapPredator(southNeighbour);
                                }
                            }
                            moved = true;
                        }
                        break;
                    case 4:
                        if (island.removeOccupant(occ.getPosition(), occ)) {
                            island.addOccupant(westNeighbour, occ);
                            occ.setPosition(westNeighbour);
                            System.out.println(occ.getName() + " moved west");
                            if (difficulty == Difficulty.MEDIUM || difficulty == Difficulty.HARD) {
                                if (island.getOccupantStringRepresentation(westNeighbour).contains("P") && island.getOccupantStringRepresentation(westNeighbour).contains("H")) {
                                    trapPredator(westNeighbour);
                                }
                            }
                            moved = true;
                        }
                        break;
                }
                if (moved == false) {
                    System.out.println("Unable to move " + occ.getName());
                }

                if (difficulty == Difficulty.HARD) {
                    Position currentPos = occ.getPosition();
                    if (island.getOccupantStringRepresentation(currentPos).contains("P") && island.getOccupantStringRepresentation(currentPos).contains("K")) {
                        for (Occupant o : island.getOccupants(currentPos)) {
                            if (o.getStringRepresentation().equals("K") && kiwisKilled < ((totalKiwis / 2) - 1)) {
                                island.removeOccupant(currentPos, o);
                                System.out.println("Kiwi was killed by predator");
                                kiwisKilled++;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            count++;
        }
        kiwiList.clear();
        predatorList.clear();
        for (Position p : positionList) {
            for (Occupant o : island.getOccupants(p)) {
                if (o.getStringRepresentation().equals("K")) {
                    kiwiList.add(o);
                } else if (o.getStringRepresentation().equals("P")) {
                    predatorList.add(o);
                }
            }
        }
        System.out.println(kiwiList);
        System.out.println(predatorList);
    }

    /**
     * Adds a game event listener.
     *
     * @param listener the listener to add
     */
    public void addGameEventListener(GameEventListener listener) {
        eventListeners.add(listener);
    }

    /**
     * Removes a game event listener.
     *
     * @param listener the listener to remove
     */
    public void removeGameEventListener(GameEventListener listener) {
        eventListeners.remove(listener);
    }

    /**
     * *******************************************************************************************************************************
     * Private methods
     * *******************************************************************************************************************************
     */
    /**
     * Used after player actions to update game state. Applies the Win/Lose
     * rules.
     */
    private void updateGameState() {
        String message = "";
        if (!player.isAlive()) {
            state = GameState.LOST;
            message = "Sorry, you have lost the game. " + this.getLoseMessage();
            this.setLoseMessage(message);
        } else if (!playerCanMove()) {
            state = GameState.LOST;
            message = "Sorry, you have lost the game. You do not have sufficient stamina to move.";
            this.setLoseMessage(message);
        } else if (predatorsTrapped == totalPredators) {
            state = GameState.WON;
            message = "You win! You have done an excellent job and trapped all the predators. Would you like to save your score?";
            this.setWinMessage(message);
        } else if (kiwiCount == totalKiwis) {
            if (predatorsTrapped >= totalPredators * MIN_REQUIRED_CATCH) {
                state = GameState.WON;
                message = "You win! You have counted all the kiwi and trapped at least 80% of the predators. Would you like to save your score?";
                this.setWinMessage(message);
            }
        }
        // notify listeners about changes
        notifyGameEventListeners();
    }

    public void setGameState(GameState state) {
        this.state = state;
        String message = "Sorry, you have lost the game. You ran out of time.";
        this.setLoseMessage(message);
    }

    /**
     * Sets details about players win
     *
     * @param message
     */
    private void setWinMessage(String message) {
        winMessage = message;
    }

    /**
     * Sets details of why player lost
     *
     * @param message
     */
    private void setLoseMessage(String message) {
        loseMessage = message;
    }

    /**
     * Set a message for the player
     *
     * @param message
     */
    private void setPlayerMessage(String message) {
        playerMessage = message;

    }

    /**
     * Check if player able to move
     *
     * @return true if player can move
     */
    private boolean playerCanMove() {
        return (isPlayerMovePossible(MoveDirection.NORTH) || isPlayerMovePossible(MoveDirection.SOUTH)
                || isPlayerMovePossible(MoveDirection.EAST) || isPlayerMovePossible(MoveDirection.WEST));

    }

    /**
     * Trap a predator in this position
     *
     * @return true if predator trapped
     */
    private boolean trapPredator(Position current) {
        boolean hadPredator = island.hasPredator(current);
        if (hadPredator) //can trap it
        {
            Occupant occupant = island.getPredator(current);
            //Predator has been trapped so remove
            island.removeOccupant(current, occupant);
            try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("predatorKill.wav").getAbsoluteFile());
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
            predatorsTrapped++;
        }
        return hadPredator;
    }

    /**
     * Checks if the player has met a hazard and applies hazard impact. Fatal
     * hazards kill player and end game.
     */
    private void checkForHazard() {
        //check if there are hazards
        for (Occupant occupant : island.getOccupants(player.getPosition())) {
            if (occupant instanceof Hazard) {
                handleHazard((Hazard) occupant);
            }
        }
    }

    /**
     * Apply impact of hazard
     *
     * @param hazard to handle
     */
    private void handleHazard(Hazard hazard) {
        if (hazard.isFatal()) {
            player.kill();
            this.setLoseMessage(hazard.getDescription() + " has killed you.");
        } else if (hazard.isBreakTrap()) {
            Tool trap = player.getTrap();
            if (trap != null) {
                trap.setBroken();
                this.setPlayerMessage("Sorry your predator trap is broken. You will need to find tools to fix it before you can use it again.");
            }
        } else // hazard reduces player's stamina
        {
            double impact = hazard.getImpact();
            // Impact is a reduction in players energy by this % of Max Stamina
            double reduction = player.getMaximumStaminaLevel() * impact;
            player.reduceStamina(reduction);
            // if stamina drops to zero: player is dead
            if (player.getStaminaLevel() <= 0.0) {
                player.kill();
                this.setLoseMessage(" You have run out of stamina");
            } else // Let player know what happened
            {
                this.setPlayerMessage(hazard.getDescription() + " has reduced your stamina.");
            }
        }
    }

    /**
     * Notifies all game event listeners about a change.
     */
    private void notifyGameEventListeners() {
        for (GameEventListener listener : eventListeners) {
            listener.gameStateChanged();
        }
    }

    /**
     * Loads terrain and occupant data from a file. At this stage this method
     * assumes that the data file is correct and just throws an exception or
     * ignores it if it is not.
     *
     * @param fileName file name of the data file
     */
    private void initialiseIslandFromFile(String fileName) {
        try {
            Scanner input = new Scanner(new File(fileName));
            // make sure decimal numbers are read in the form "123.23"
            input.useLocale(Locale.US);
            input.useDelimiter("\\s*,\\s*");

            // create the island
            int numRows = input.nextInt();
            int numColumns = input.nextInt();
            island = new Island(numRows, numColumns);

            // read and setup the terrain
            setUpTerrain(input);

            // read and setup the player
            setUpPlayer(input);

            // read and setup the occupants
            setUpOccupants(input);

            input.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find data file '" + fileName + "'");
        } //catch (IOException e) {
//            System.err.println("Problem encountered processing file.");
//        }
    }

    /**
     * Reads terrain data and creates the terrain.
     *
     * @param input data from the level file
     */
    private void setUpTerrain(Scanner input) {
        for (int row = 0; row < island.getNumRows(); row++) {
            String terrainRow = input.next();
            for (int col = 0; col < terrainRow.length(); col++) {
                Position pos = new Position(island, row, col);
                String terrainString = terrainRow.substring(col, col + 1);
                Terrain terrain = Terrain.getTerrainFromStringRepresentation(terrainString);
                island.setTerrain(pos, terrain);
            }
        }
    }

    /**
     * Reads player data and creates the player.
     *
     * @param input data from the level file
     */
    private void setUpPlayer(Scanner input) {
        String playerName = name;
        String waste = input.next();
        int playerPosRow = input.nextInt();
        int playerPosCol = input.nextInt();
        double playerMaxStamina = input.nextDouble();
        double playerMaxBackpackWeight = input.nextDouble();
        double playerMaxBackpackSize = input.nextDouble();

        Position pos = new Position(island, playerPosRow, playerPosCol);
        player = new Player(pos, playerName,
                playerMaxStamina,
                playerMaxBackpackWeight, playerMaxBackpackSize);
        island.updatePlayerPosition(player);
    }

    /**
     * Callum Creates occupants listed in the file and adds them to the island.
     *
     * @param input data from the level file
     */
    private void setUpOccupants(Scanner input) {
        int numItems = input.nextInt();
        for (int i = 0; i < numItems; i++) {
            String occType = input.next();
            String occName = input.next();
            String occDesc = input.next();
            int occRow = input.nextInt();
            int occCol = input.nextInt();

            Position occPos = null;
            boolean positionExists = false;
            for (Position p : positionList) {
                if (p.getColumn() == occCol && p.getRow() == occRow) {
                    positionExists = true;
                    occPos = p;
                    break;
                }
            }
            if (!positionExists) {
                occPos = new Position(island, occRow, occCol);
                positionList.add(occPos);
            }

            Occupant occupant = null;

            if (occType.equals("T")) {
                double weight = input.nextDouble();
                double size = input.nextDouble();
                occupant = new Tool(occPos, occName, occDesc, weight, size);
            } else if (occType.equals("ST")) {
                double weight = input.nextDouble();
                double size = input.nextDouble();
                occupant = new Tool(occPos, occName, occDesc, weight, size);
            } else if (occType.equals("E")) {
                double weight = input.nextDouble();
                double size = input.nextDouble();
                double energy = input.nextDouble();
                occupant = new Food(occPos, occName, occDesc, weight, size, energy);
            } else if (occType.equals("H")) {
                double impact = input.nextDouble();
                occupant = new Hazard(occPos, occName, occDesc, impact);
            } else if (occType.equals("K")) {
                occupant = new Kiwi(occPos, occName, occDesc);
                kiwiList.add(occupant);
                totalKiwis++;
            } else if (occType.equals("P")) {
                occupant = new Predator(occPos, occName, occDesc);
                predatorList.add(occupant);
                totalPredators++;
            } else if (occType.equals("F")) {
                occupant = new Fauna(occPos, occName, occDesc);
            }
            if (occupant != null) {
                island.addOccupant(occPos, occupant);
            }
        }
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public boolean saveScores() {
        Score score = new Score(getPlayerName(), getTotalTurns(), difficulty);
        return score.update();
    }

    public ArrayList<String> viewScores() {
        Score score = new Score(getPlayerName(), getTotalTurns(), difficulty);
        ArrayList<String> results = score.view();
        return results;
    }
    
    public int getKiwisKilled() {
        return this.kiwisKilled;
    }

    private Island island;
    private Player player;

    //  Callum update
    private ArrayList<Occupant> kiwiList;
    private ArrayList<Occupant> predatorList;
    private ArrayList<Position> positionList;
//  End of callum update

    private GameState state;
    private int kiwiCount;
    private int totalKiwis;
    private int kiwisKilled;
    private int totalPredators;
    private int predatorsTrapped;
    private int totalTurns;
    private Set<GameEventListener> eventListeners;

    private final double MIN_REQUIRED_CATCH = 0.8;

    private String winMessage = "";
    private String loseMessage = "";
    private String playerMessage = "";
    private String name;

}
