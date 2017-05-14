/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public Score score;
    public String playerName;
    public int playerMoves;
    
    
    public ScoreTest() {
    }
    
    @Before
    public void setUp() {
        //Creates a new Score for the data file.
        //Player has a name of Test.
        //It took the player 5 moves to complete the game.
        playerName = "Test";
        playerMoves = 5;
        score = new Score(playerName, playerMoves);
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
    }

    /**
     * Test of view method, of class Score.
     */
    @Test
    public void testView() {
        ArrayList<String> values = score.view();
        String testValue = "Name: Test, Score: 5";
        assertEquals("Check viewing of database", values.get(0), testValue);
    }
    
}
