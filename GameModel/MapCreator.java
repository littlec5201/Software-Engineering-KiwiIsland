/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ethan
 */
public class MapCreator {
    
    public MapCreator(){
        
    }
    
    public String[] initialiseTerrain(){
        String[] terrain = new String[10];
        String toAdd = "";
        for(int i = 0; i < 10; i++){
            for(int j  = 0; j < 10; j++){
                Random rand = new Random();
                int randy = rand.nextInt(5);
                if(randy == 0){
                    toAdd += '~';
                }else if(randy == 1){
                    toAdd += '.';
                }else if(randy == 2){
                    toAdd += '^';
                }else if(randy == 3){
                    toAdd += '#';
                }else if(randy == 4){
                    toAdd += '*';
                }
            }
            terrain[i] = toAdd;
            toAdd = "";
        }
        return terrain;
    }
    
    public void createMap(){
        Random rand = new Random();
        String[] terrain = initialiseTerrain();
        
        try {
            PrintWriter out = new PrintWriter(new FileWriter("IslandData.txt"));
            out.println("10, 10,");
            for(int i = 0; i < 10; i++){
                out.println(terrain[i]+",");
            }
            out.println("River Song, 0, 2, 100.0, 10.0, 5.0,");
            out.println("38,");
            out.println("E,Sandwich,A nice and healthy sandwich, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 2.0, 1.0, 50.0,");
            out.println("E,Muesli Bar,A juicy and nutricious muesli bar, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 1.0, 1.0, 50.0,");
            out.println("E,Apple,A juicy apple, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 2.0, 3.0, 50.0,");
            out.println("E,Orange Juice,A bottle of juice, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 2.0, 3.0, 50.0,");
            out.println("T,Trap,A trap for predators, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 1.0, 1.0,");
            out.println("T,Trap,A trap for predators, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 1.0, 1.0,");
            out.println("ST,SuperTrap,A SuperTrap for predators, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 1.0, 1.0,");
            out.println("T,Screwdriver,A screwdriver that is useful for fixing traps, "+rand.nextInt(10)+", "+rand.nextInt(10)+", 0.5, 0.75,");
            out.println("F,Oystercatcher,A variable oystercatcher sitting on sand, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Crab,A scuttling crab,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Fernbird,A shy fernbird, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Heron,A white-faced heron, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Robin,A black robin, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Tui,A singing tui, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Fern,A silver fern, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Manuka,A flowering manukau, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Kauri, A tall kauri, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("F,Flax, A common flax, "+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A little spotted kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A little spotted kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("K,Kiwi,A large brown kiwi,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Rat,A Norwegian rat,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Cat,A wild cat,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Rat,A Norwegian rat,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Kiore,A pacific rat,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Stoat,A brown and white stoat,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Possum,A bushy tailed possum,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("P,Possum,A bushy tailed possum,"+rand.nextInt(10)+", "+rand.nextInt(10)+",");
            out.println("H,Cliff,A fall down a steep rocky cliff,"+rand.nextInt(10)+", "+rand.nextInt(10)+",1.0,");
            out.println("H,Pond,A fall into a deep pond,"+rand.nextInt(10)+", "+rand.nextInt(10)+",1.0,");
            out.println("H,Rock,A large falling rock,"+rand.nextInt(10)+", "+rand.nextInt(10)+",0.5,");
            out.println("H,Sunburn,Too much sun has given you bad sunburn and,"+rand.nextInt(10)+", "+rand.nextInt(10)+",0.3,");
            out.println("H,Fall,Tripping on roots hurt your ankle and,"+rand.nextInt(10)+", "+rand.nextInt(10)+",0.5,");
            out.println("H,Cliff,A fall down a small cliff,"+rand.nextInt(10)+", "+rand.nextInt(10)+",0.3,");
            out.println("H,Broken trap,Your predator trap has broken,"+rand.nextInt(10)+", "+rand.nextInt(10)+",0.0,");
            
            out.close();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(MapCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void createTestMap(){
        try {
            PrintWriter out = new PrintWriter(new FileWriter("IslandData.txt"));
            out.println("10, 10,");
            out.println("~~...~^^^^,");
            out.println("~#^^^^^^^^,");
            out.println("####^^^^^~,");
            out.println(".###**^.~~,");
            out.println(".^^^**^.~~,");
            out.println(".*****###~,");
            out.println(".******###,");
            out.println("*****~**~~,");
            out.println("..**~~~**.,");
            out.println("~.***~***.,");
            out.println("River Song, 0, 2, 100.0, 10.0, 5.0,");
            out.println("38,");
            out.println("E,Sandwich,A nice and healthy sandwich, 2, 4, 2.0, 1.0, 50.0,");
            out.println("E,Muesli Bar,A juicy and nutricious muesli bar, 5, 4, 1.0, 1.0, 50.0,");
            out.println("E,Apple,A juicy apple, 6, 7, 2.0, 3.0, 50.0,");
            out.println("E,Orange Juice,A bottle of juice, 8, 2, 2.0, 3.0, 50.0,");
            out.println("T,Trap,A trap for predators, 0, 4, 1.0, 1.0,");
            out.println("T,Trap,A trap for predators, 3, 0, 1.0, 1.0,");
            out.println("ST,SuperTrap,A SuperTrap for predators, 5, 7, 1.0, 1.0,");
            out.println("T,Screwdriver,A screwdriver that is useful for fixing traps, 5, 3, 0.5, 0.75,");
            out.println("F,Oystercatcher,A variable oystercatcher sitting on sand, 0, 3,");
            out.println("F,Crab,A scuttling crab, 4, 0,");
            out.println("F,Fernbird,A shy fernbird, 4, 6,");
            out.println("F,Heron,A white-faced heron, 6, 8,");
            out.println("F,Robin,A black robin, 7, 3,");
            out.println("F,Tui,A singing tui, 8, 8, ");
            out.println("F,Fern,A silver fern, 1, 2,");
            out.println("F,Manuka,A flowering manukau, 3, 2,");
            out.println("F,Kauri, A tall kauri, 8, 6,");
            out.println("F,Flax, A common flax, 9, 1,");
            out.println("K,Kiwi,A little spotted kiwi,0,7,");
            out.println("K,Kiwi,A little spotted kiwi,2,8,");
            out.println("K,Kiwi,A large brown kiwi,6,2,");
            out.println("K,Kiwi,A large brown kiwi,6,6,");
            out.println("K,Kiwi,A large brown kiwi,7,0,");
            out.println("K,Kiwi,A large brown kiwi,7,7,");
            out.println("K,Kiwi,A large brown kiwi,8,3,");
            out.println("K,Kiwi,A large brown kiwi,9,3,");
            out.println("K,Kiwi,A large brown kiwi,9,4,");
            out.println("K,Kiwi,A large brown kiwi,9,6,");
            out.println("P,Rat,A Norwegian rat,6,4,");
            out.println("P,Cat,A wild cat,0,7,");
            out.println("P,Rat,A Norwegian rat,2,6,");
            out.println("P,Kiore,A pacific rat,3,4,");
            out.println("P,Stoat,A brown and white stoat,4,1,");
            out.println("P,Possum,A bushy tailed possum,5,2,");
            out.println("P,Possum,A bushy tailed possum,9,7,");
            out.println("H,Cliff,A fall down a steep rocky cliff,7,4,1.0,");
            out.println("H,Pond,A fall into a deep pond,2,2,1.0,");
            out.println("H,Rock,A large falling rock,3,5,1.0,");
            out.println("H,Sunburn,Too much sun has given you bad sunburn and,5,0,0.3,");
            out.println("H,Fall,Tripping on roots hurt your ankle and,1,4,0.5,");
            out.println("H,Cliff,A fall down a small cliff,6,5,0.3,");
            out.println("H,Broken trap,Your predator trap has broken,7,6,0.0,");
            
            out.close();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(MapCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
