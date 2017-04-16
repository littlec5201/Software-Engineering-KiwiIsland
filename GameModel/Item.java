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
public  abstract class Item extends Occupant
{
    private double weight;
    private double size;

    /**
     * Construct an item with known attributes.
     * @param pos the position of the item
     * @param name the name of the item
     * @param description a longer description of the item
     */
    public Item(Position pos, String name, String description, double weight, double size) 
    {
        super(pos, name, description);
        this.weight = weight;
        this.size = size;
    }
    
     /**
     * Gets the weight of the item
     * @return the weight of the item
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Gets the size of the item
     * @return the size of the item
     */
    public double getSize() {
        return this.size;
    }    

    
    /**
     * Is it OK to pick up and carry this item?
     * Items with size > 0 can be carried.
     * 
     * @return true if item can be carried, false if not
     */
    public boolean isOkToCarry()
    {
        return size > 0;
    }
    
}
