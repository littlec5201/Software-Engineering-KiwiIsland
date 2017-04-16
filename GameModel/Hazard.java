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
public class Hazard extends Occupant {
    
    private final double impact;
    private final double FATAL_IMPACT = 1.0;

    /**
     * Create a hazard on the island
     * @param position where hazard is.
     * @param name of hazard.
     * @param description a more detailed description of hazard
     * @param impact on player
     */
    public Hazard(Position position, String name, String description, double impact) {
        
        super(position, name, description);
        this.impact = impact;
        
    }
    
    /**
     * What is impact of hazard
     * @return impact
     */
    public double getImpact() {
        return this.impact;
    }
    
    /**
     * Checks if fatal impact
     * @return true if fatal
     */
    public boolean isFatal()
    {
        return impact == FATAL_IMPACT;
    }
    
    /**
     * Checks if this is broken trap hazard
     * @return true if this is broken trap hazard
     */
    public boolean isBreakTrap() 
    {
        String name = this.getName();
        return name.equalsIgnoreCase("Broken trap");
    }
    
    @Override
    public String getStringRepresentation() {
        return "H";
    }   


    
}
