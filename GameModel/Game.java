package nz.ac.aut.ense701.gameModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
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

    /**
     * A new instance of Kiwi island that reads data from "IslandData.txt".
     */
    public Game() {
        eventListeners = new HashSet<GameEventListener>();
        createNewGame();
    }

    /**
     * Starts a new game.
     * At this stage data is being read from a text file
     */
	public void createNewGame() {
//      Callum update
        kiwiList = new ArrayList<Occupant>();
        predatorList = new ArrayList<Occupant>();
        positionList = new ArrayList<Position>();

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
    }

    /***********************************************************************************************************************
     * Accessor methods for game data
    ************************************************************************************************************************/
    
    /**
     * Get number of rows on island
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
    public boolean isPlayerMovePossible(MoveDirection direction)
    {
        boolean isMovePossible = false;
        // what position is the player moving to?
        Position newPosition = player.getPosition().getNewPosition(direction);
        // is that a valid position?
        if ( (newPosition != null) && newPosition.isOnIsland() )
        {
               // isMovePossible = false;
            
          //  }
            // what is the terrain at that new position?
            Terrain newTerrain = island.getTerrain(newPosition);
            // can the playuer do it?
            isMovePossible = player.hasStaminaToMove(newTerrain) && 
                             player.isAlive();
        }
        return isMovePossible;
    }
    
      /**
     * Get terrain for position
     * @param row
     * @param column
     * @return Terrain at position row, column
     */
    public Terrain getTerrain(int row, int column) {
        return island.getTerrain(new Position(island, row, column));
    }

    /**
     * Is this position visible?
     * @param row
     * @param column
     * @return true if position row, column is visible
     */
    public boolean isVisible(int row, int column) {
        return island.isVisible(new Position(island, row, column));

    }
   
    /**
    * Is this position explored?
    * @param row
    * @param column
    * @return true if position row, column is explored.
    */
    public boolean isExplored(int row, int column) {
        return island.isExplored(new Position(island, row, column));
    }
    
    /**
     * Get occupants for player's position
     * @return occupants at player's position
     */
    public Occupant[] getOccupantsPlayerPosition()
    {
        return island.getOccupants(player.getPosition());
    }
    
    /**
     * Get string for occupants of this position
     * @param row
     * @param column
     * @return occupant string for this position row, column
     */
    public String getOccupantStringRepresentation(int row, int column) {
        return island.getOccupantStringRepresentation(new Position(island, row, column));
    }
    
    /**
     * Get values from player for GUI display
     * @return player values related to stamina and backpack.
     */
    public int[] getPlayerValues()
    {
        int[] playerValues = new int[6];
        playerValues[STAMINA_INDEX ]= (int) player.getStaminaLevel();
        playerValues[MAXSTAMINA_INDEX]= (int) player.getMaximumStaminaLevel();
        playerValues[MAXWEIGHT_INDEX ]= (int) player.getMaximumBackpackWeight();
        playerValues[WEIGHT_INDEX]= (int) player.getCurrentBackpackWeight();
        playerValues[MAXSIZE_INDEX ]= (int) player.getMaximumBackpackSize();
        playerValues[SIZE_INDEX]= (int) player.getCurrentBackpackSize();
            
        return playerValues;
        
    }
    
    /**
     * How many kiwis have been counted?
     * @return count
     */
    public int getKiwiCount()
    {
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
                } //Supertrap can be used anytime if player has one
                else if(tool.isSuperTrap() && player.hasSuperTrap())
                {
                }//Screwdriver can only be used if player has a broken trap
                else if (tool.isScrewdriver() && player.hasTrap()) {
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

    
