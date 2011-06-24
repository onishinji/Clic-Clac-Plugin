package onishinji.models;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;

public class MyLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    public MyLocation(Location ref) {
        this.x = ref.getBlockX();
        this.y = ref.getBlockY();
        this.z = ref.getBlockZ();
    }

    public Location getWordLocation() {
        return new Location(null, x, y, z);

    }

    private int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    private int y;
    private int z;
}
