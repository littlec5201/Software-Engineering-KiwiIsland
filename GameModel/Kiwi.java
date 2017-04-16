/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

/**
 *
 * @author Ethan
 */
public class Kiwi  extends Fauna
{
    private boolean counted;
    /**
     * Constructor for objects of class Kiwi
     * @param pos the position of the kiwi object
     * @param name the name of the kiwi object
     * @param description a longer description of the kiwi
     */
    public Kiwi(Position pos, String name, String description) 
    {
        super(pos, name, description);
        counted = false;
    } 
    
    /**
    * Count this kiwi
    */
    public void count() {
        counted = true;
    }
 
   /**
    * Has this kiwi been counted
    * @return true if counted.
    */
    public boolean counted() {
        return counted;
    }


    @Override
    public String getStringRepresentation() 
    {
        return "K";
    }     
}

