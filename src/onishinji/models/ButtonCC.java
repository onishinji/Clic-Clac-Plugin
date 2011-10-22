package onishinji.models;

import org.bukkit.Location;

public class ButtonCC extends MyLocation {
 
    boolean positiv = true; 

    public ButtonCC(Location ref) {
        super(ref);
        // TODO Auto-generated constructor stub
    }

    public boolean isPositiv() {
        return positiv;
    }

    public void setPositiv(boolean positiv) {
        this.positiv = positiv;
    }
 
    
    
}
