package onishinji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import onishinji.models.BlockManager;
import onishinji.models.ButtonCC;
import onishinji.models.MyLocation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;

public class StructureCC implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4024893610732056737L;

    public String name;
    public String groupName = "default";

    public ArrayList<MyLocation> bornes;
    public ArrayList<ButtonCC> boutons;
    public ArrayList<BlockManager> blocsInitial;
    public ArrayList<BlockManager> blocsFinal;

    public ArrayList<ArrayList<BlockManager>> steps;

    public Boolean isEditable = false;
    public Boolean isBreakable = false;

    public int currentState = 0;

    public String worldName;
    public String playerName;

    public boolean sens;

    public Date lastUsed;
    public boolean isDateLimited = false;
    public long timeForDateLimited = 0;
    
    public boolean isAnimated = false;

    public StructureCC() {
        bornes = new ArrayList<MyLocation>();
        boutons = new ArrayList<ButtonCC>();
        blocsInitial = new ArrayList<BlockManager>();
        blocsFinal = new ArrayList<BlockManager>();

        steps = new ArrayList<ArrayList<BlockManager>>();

    }

    public StructureCC(String name, ArrayList<ButtonCC> boutons) {
        super();
        this.name = name;
        this.boutons = boutons;
    }

    public StructureCC(String name) {
        super();
        this.name = name;
    }

    public void createStartStateAndRemoveInnerBloc(Player player) {
        blocsInitial = this.getBlocInnerBloc(player);
        this.addStep(player);
        this.remove(blocsInitial, player.getWorld());
    }

    private ArrayList<BlockManager> getBlocInnerBloc(Player player) {

        World world = player.getWorld();

        Block first = player.getWorld().getBlockAt(bornes.get(0).getX(), bornes.get(0).getY(), bornes.get(0).getZ());
        Block second = player.getWorld().getBlockAt(bornes.get(1).getX(), bornes.get(1).getY(), bornes.get(1).getZ());
        Block tmp;
        if (!(first.getLocation().getBlockX() > second.getLocation().getBlockX() && first.getLocation().getBlockZ() > second.getLocation().getBlockZ())) {
            tmp = first;
            first = second;
            second = tmp;
        }

        ArrayList<BlockManager> blocks = new ArrayList<BlockManager>();

        int maxY = 0;
        int minY = 0;

        int minX = 0;
        int maxX = 0;

        int minZ = 0;
        int maxZ = 0;

        if (first.getLocation().getBlockX() <= second.getLocation().getBlockX()) {
            minX = first.getLocation().getBlockX();
            maxX = second.getLocation().getBlockX();
        } else {
            minX = second.getLocation().getBlockX();
            maxX = first.getLocation().getBlockX();
        }

        if (first.getLocation().getBlockZ() <= second.getLocation().getBlockZ()) {
            minZ = first.getLocation().getBlockZ();
            maxZ = second.getLocation().getBlockZ();
        } else {
            minZ = second.getLocation().getBlockZ();
            maxZ = first.getLocation().getBlockZ();
        }

        if (first.getLocation().getBlockY() <= second.getLocation().getBlockY()) {
            minY = first.getLocation().getBlockY();
            maxY = second.getLocation().getBlockY();
        } else {
            minY = second.getLocation().getBlockY();
            maxY = first.getLocation().getBlockY();
        }

        if (maxX == 0)
            maxX = 2;
        if (maxZ == 0)
            maxZ = 2;

        ArrayList<Material> materials = StructureCC.getMaterialsForSecondPass();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {

                for (int y = minY; y <= maxY; y++) {
                    Block currentblock = world.getBlockAt(x, y, z);

                    if (materials.contains(currentblock.getType())) {

                        BlockManager currentBlockManager = new BlockManager();
                        blocks.add(currentBlockManager.convertBlock(currentblock));
                    }

                }
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {

                for (int y = minY; y <= maxY; y++) {
                    Block currentblock = world.getBlockAt(x, y, z);

                    if (!materials.contains(currentblock.getType())) {

                        BlockManager currentBlockManager = new BlockManager();
                        blocks.add(currentBlockManager.convertBlock(currentblock));
                    }

                }
            }
        }

        return blocks;
    }

    public void finishStructure(Player player) {
        blocsFinal = this.getBlocInnerBloc(player);
        this.addStep(player);
    }

    public void switchBlocs(World world) {

        if (isEditable == false) {
            if (!this.isMultiCC()) {
                if (currentState == 0) {
                    this.remove(blocsInitial, world);
                    this.print(blocsFinal, world);
                } else {

                    this.remove(blocsFinal, world);
                    this.print(blocsInitial, world);
                }
            } else {
                if(currentState - 1 >=0)
                this.remove(steps.get(currentState - 1), world);

                if(currentState + 1 < steps.size())
                this.remove(steps.get(currentState + 1), world);
                
                this.print(steps.get(currentState), world);
            }
        }
    }
    
    public void reinitMultiCC(World world)
    {
        if(this.isMultiCC())
        {
            this.remove(steps.get(currentState), world);
            this.print(steps.get(0), world);
            this.currentState = 0;
        }
    }

    public void finishMultiCC(World world) {
        if(this.isMultiCC())
        {
            this.remove(steps.get(currentState), world);
            this.print(steps.get(this.steps.size() - 1), world);
            this.currentState = this.steps.size() - 1;
        }
    }

    public boolean isMultiCC() {
        return steps.size() > 2;
    }

    static ArrayList<Material> getMaterialsForSecondPass() {

        ArrayList<Material> materialForSecondPass = new ArrayList<Material>();
        materialForSecondPass.add(Material.BROWN_MUSHROOM);
        materialForSecondPass.add(Material.BED);
        materialForSecondPass.add(Material.BED_BLOCK);
        materialForSecondPass.add(Material.CAKE);
        materialForSecondPass.add(Material.CAKE_BLOCK);
        materialForSecondPass.add(Material.CACTUS);
        materialForSecondPass.add(Material.DETECTOR_RAIL);
        materialForSecondPass.add(Material.DETECTOR_RAIL);
        materialForSecondPass.add(Material.DIODE);
        materialForSecondPass.add(Material.DIODE_BLOCK_OFF);
        materialForSecondPass.add(Material.DIODE_BLOCK_ON);
        materialForSecondPass.add(Material.GRASS);
        materialForSecondPass.add(Material.IRON_DOOR);
        materialForSecondPass.add(Material.ICE);
        materialForSecondPass.add(Material.IRON_DOOR_BLOCK);
        materialForSecondPass.add(Material.LEVER);
        materialForSecondPass.add(Material.LADDER);
        materialForSecondPass.add(Material.LAVA);
        materialForSecondPass.add(Material.REDSTONE);
        materialForSecondPass.add(Material.REDSTONE_TORCH_OFF);
        materialForSecondPass.add(Material.REDSTONE_TORCH_ON);
        materialForSecondPass.add(Material.REDSTONE_WIRE);
        materialForSecondPass.add(Material.RED_MUSHROOM);
        materialForSecondPass.add(Material.RED_ROSE);
        materialForSecondPass.add(Material.RAILS);
        materialForSecondPass.add(Material.SEEDS);
        materialForSecondPass.add(Material.SIGN);
        materialForSecondPass.add(Material.SIGN_POST);
        materialForSecondPass.add(Material.SUGAR_CANE);
        materialForSecondPass.add(Material.SUGAR_CANE_BLOCK);
        materialForSecondPass.add(Material.STONE_PLATE);
        materialForSecondPass.add(Material.TORCH);
        materialForSecondPass.add(Material.WALL_SIGN);
        materialForSecondPass.add(Material.YELLOW_FLOWER);
        materialForSecondPass.add(Material.WATER);
        materialForSecondPass.add(Material.WOODEN_DOOR);
        materialForSecondPass.add(Material.WOOD_PLATE);

        return materialForSecondPass;
    }

    private void remove(ArrayList<BlockManager> blocs, World world) {

        ArrayList<Material> materials = StructureCC.getMaterialsForSecondPass();

        for (BlockManager blockM : blocs) {

            if (materials.contains(blockM.getType())) {

                world.getBlockAt(blockM.getLocation(world)).setData((byte) 0);
                world.getBlockAt(blockM.getLocation(world)).setType(Material.AIR);
            }
        }

        for (BlockManager blockM : blocs) {

            if (!materials.contains(blockM.getType())) {
                Block block = world.getBlockAt(blockM.getLocation(world));

                if (block.getState() instanceof ContainerBlock) {
                    ContainerBlock chest = (ContainerBlock) block.getState();

                    if (chest != null) {
                        chest.getInventory().clear();
                    }
                }

                world.getBlockAt(blockM.getLocation(world)).setData((byte) 0);
                world.getBlockAt(blockM.getLocation(world)).setType(Material.AIR);
            }
        }
    }

    private void print(ArrayList<BlockManager> blocs, World world) {

        ArrayList<Material> materials = StructureCC.getMaterialsForSecondPass();

        for (BlockManager blockM : blocs) {
            Location newLocation = blockM.getLocation(world);

            if (!materials.contains(blockM.getType())) {

                world.getBlockAt(newLocation).setType(blockM.getType());
                world.getBlockAt(newLocation).setData(blockM.getData());

            }
        }

        for (BlockManager blockM : blocs) {
            Location newLocation = blockM.getLocation(world);

            if (materials.contains(blockM.getType())) {

                world.getBlockAt(newLocation).setType(blockM.getType());
                world.getBlockAt(newLocation).setData(blockM.getData());

            }
        }
    }

    public void undoStart(World world) {
        // TODO Auto-generated method stub
        this.print(blocsInitial, world);

    }

    public void updateBlock(Player player) {

        if(!this.isMultiCC())
        {
            if (currentState == 0) {
                blocsFinal = this.getBlocInnerBloc(player);
            } else {
                blocsInitial = this.getBlocInnerBloc(player);
            }
        }
        else
        {
            steps.set(this.currentState, this.getBlocInnerBloc(player));
        }

    }

    public void addStep(Player player) {
        steps.add(this.getBlocInnerBloc(player));
    }

    public void computeNewState(int newCurrent, ButtonCC structureB) {

        if (this.isMultiCC()) {
            if (structureB.isPositiv()) {
                if (this.currentState < steps.size() - 1)
                    this.currentState++;
                else
                    this.currentState = 0;
            } else {
                if (this.currentState > 0)
                    this.currentState--;
                else
                    this.currentState = steps.size()-1;
            }

        } else {
            this.currentState = newCurrent;
        }
    }

    public void addButton(MyLocation myLocation, boolean sens2) {
        // TODO Auto-generated method stub
        
        ButtonCC b = new ButtonCC(myLocation.getWordLocation());
        b.setPositiv(sens2);
        
        this.boutons.add(b);
        
    }

    public boolean isBreakable() { 
        return isBreakable;
    }

    public boolean canBeAnimated() {
       
        boolean animated = this.isAnimated;
        
        if(this.isDateLimited)
        { 
            Calendar cal = Calendar.getInstance(); 
            
            long currentTime = cal.getTime().getTime();
            
            if(this.lastUsed == null)
            {
                System.out.println("set used");
               cal.add(Calendar.SECOND, (int) -this.timeForDateLimited);
               this.lastUsed = cal.getTime();
            }
            long res = currentTime - this.lastUsed.getTime(); 
            
            long test = currentTime - this.lastUsed.getTime();
            long borne = this.timeForDateLimited * 1000;
            
            if(currentTime - this.lastUsed.getTime() <= this.timeForDateLimited * 1000)
            {
                return false;
            }
            else
            {
                return !animated && true;
            }             
        }
        
        return animated;
    }


}
