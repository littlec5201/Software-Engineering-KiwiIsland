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
public class Fauna extends Occupant
{
    

    /**
     * Constructor for objects of class Endangered
     * @param pos the position of the kiwi
     * @param name the name of the kiwi
     * @param description a longer description of the kiwi
     */
    public Fauna(Position pos, String name, String description) 
    {
        super(pos, name, description);
    } 
    
 


    @Override
    public String getStringRepresentation() 
    {
          return "F";
    }    
}
