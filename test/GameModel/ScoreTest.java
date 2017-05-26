package GameModel;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ethan
 */
public class ScoreTest {
    
    private Score score;
    private String playerName;
    private int playerMoves;
    private Difficulty difficulty;
    
    
    public ScoreTest() {
    }
    
    @Before
    public void setUp() {
        //Creates a new Score for the data file.
        //Player has a name of Test.
        //It took the player 5 moves to complete the game.
        playerName = "Eth";
        playerMoves = 5;
        difficulty = Difficulty.EASY;
        score = new Score(playerName, playerMoves, difficulty);
        score.emptyDBRecords();
    }
    
    @After
    public void tearDown() {
        score = null;
        playerName = null;
        playerMoves = 0;
    }

    /**
     * Test of update method, of class Score.
     */
    @Test
    public void testUpdate() {
        assertEquals("Check update of database", score.update(), true);
        score.emptyDBRecords();
    }

    /**
     * Test of view method, of class Score.
     */
    @Test
    public void testView() {
        score.update();
        ArrayList<String> values = score.view();
        String testValue = "Name: Eth, Score: 5, Difficulty: EASY";
        assertEquals("Check viewing of database", values.get(0), testValue);
        score.emptyDBRecords();
    }
    
    /**
     * Test the emptyDBRecords method, of class Score
     */
    @Test
    public void testEmptyDBRecords(){
        score.emptyDBRecords();
        ArrayList<String> values = score.view();
        assertEquals("Check emptying of database: ", values.size(), 0);
    }
    
}
