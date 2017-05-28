package GameModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class MapCreatorTest {
    
    private MapCreator map;
    
    public MapCreatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        map = new MapCreator();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initialiseTerrain method, of class MapCreator.
     */
    @Test
    public void testInitialiseTerrain() {
        int count = 0;
        String[] terrainToTest = map.initialiseTerrain();
        if(terrainToTest.length == 10){
            for(int i = 0; i < 10; i++){
                char[] terrainRow = terrainToTest[i].toCharArray();
                for(int j = 0; j < 10; j++){
                    if(terrainRow[i] == '~' || terrainRow[i] == '.' || terrainRow[i] == '^' || terrainRow[i] == '#' || terrainRow[i] == '*'){
                        count++;
                    }
                }
            }
            assertEquals(100, count);
        }else{
            fail("The amount of terrain generated is incorrect");
        }
    }

    /**
     * Test of createMap method, of class MapCreator.
     */
    @Test
    public void testCreateMap() {
        int count = 0;
        map.createMap();
        try {
            BufferedReader in = new BufferedReader(new FileReader("IslandData.txt"));
            
            String line = "";
            
            line = in.readLine();
            if(line.equals("10, 10,")){
                    count++;
            }
            for(int i = 0; i < 10; i ++){
                line = in.readLine();
                if(line.matches("[~,#,^,*.]{10},")){
                    count++;
                }
            }
            
            line = in.readLine();
            if(line.equals("River Song, 0, 2, 100.0, 10.0, 5.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.equals("38,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("E,Sandwich,A nice and healthy sandwich, [0-9]{1}, [0-9]{1}, 2.0, 1.0, 50.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("E,Muesli Bar,A juicy and nutricious muesli bar, [0-9]{1}, [0-9]{1}, 1.0, 1.0, 50.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("E,Apple,A juicy apple, [0-9]{1}, [0-9]{1}, 2.0, 3.0, 50.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("E,Orange Juice,A bottle of juice, [0-9]{1}, [0-9]{1}, 2.0, 3.0, 50.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("T,Trap,A trap for predators, [0-9]{1}, [0-9]{1}, 1.0, 1.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("T,Trap,A trap for predators, [0-9]{1}, [0-9]{1}, 1.0, 1.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("ST,SuperTrap,A SuperTrap for predators, [0-9]{1}, [0-9]{1}, 1.0, 1.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("T,Screwdriver,A screwdriver that is useful for fixing traps, [0-9]{1}, [0-9]{1}, 0.5, 0.75,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Oystercatcher,A variable oystercatcher sitting on sand, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Crab,A scuttling crab,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Fernbird,A shy fernbird, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Heron,A white-faced heron, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Robin,A black robin, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Tui,A singing tui, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Fern,A silver fern, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Manuka,A flowering manukau, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Kauri, A tall kauri, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("F,Flax, A common flax, [0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A little spotted kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A little spotted kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("K,Kiwi,A large brown kiwi,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Rat,A Norwegian rat,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Cat,A wild cat,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Rat,A Norwegian rat,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Kiore,A pacific rat,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Stoat,A brown and white stoat,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Possum,A bushy tailed possum,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("P,Possum,A bushy tailed possum,[0-9]{1}, [0-9]{1},")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Cliff,A fall down a steep rocky cliff,[0-9]{1}, [0-9]{1},1.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Pond,A fall into a deep pond,[0-9]{1}, [0-9]{1},1.0,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Rock,A large falling rock,[0-9]{1}, [0-9]{1},0.5,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Sunburn,Too much sun has given you bad sunburn and,[0-9]{1}, [0-9]{1},0.3,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Fall,Tripping on roots hurt your ankle and,[0-9]{1}, [0-9]{1},0.5,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Cliff,A fall down a small cliff,[0-9]{1}, [0-9]{1},0.3,")){
                    count++;
            }
            
            line = in.readLine();
            if(line.matches("H,Broken trap,Your predator trap has broken,[0-9]{1}, [0-9]{1},0.0,")){
                    count++;
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MapCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(55, count);
    }
    
}
