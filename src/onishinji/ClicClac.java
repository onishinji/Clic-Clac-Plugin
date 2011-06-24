package onishinji;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import onishinji.commands.CreateCCCommand;
import onishinji.commands.EndCCCommand;
import onishinji.commands.LinkCCCommand;
import onishinji.commands.ListCCCommand;
import onishinji.commands.RemoveCCCommand;
import onishinji.commands.UndoCommand;
import onishinji.listener.CacheCachePlayerBlockListener;
import onishinji.listener.CacheCachePlayerListener;
import onishinji.models.MyLocation;
import onishinji.models.SLAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class ClicClac extends JavaPlugin {

    private final CacheCachePlayerListener playerListener = new CacheCachePlayerListener(this);
    private final CacheCachePlayerBlockListener blockListiner = new CacheCachePlayerBlockListener(this);

    public HashMap<Player, StructureCC> playerCreateCommand = new HashMap<Player, StructureCC>();
    public HashMap<Player, StructureCC> playerActiveLink = new HashMap<Player, StructureCC>();
    public ArrayList<StructureCC> structuresLoaded = new ArrayList<StructureCC>();
    public PermissionHandler _permissions;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        
        _permissions = null;

        playerCreateCommand = new HashMap<Player, StructureCC>();
        playerActiveLink = new HashMap<Player, StructureCC>();
        structuresLoaded = new ArrayList<StructureCC>();

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BURN, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListiner, Priority.Normal, this);

        // Register our commands
        getCommand("cc-start").setExecutor(new CreateCCCommand(this));
        getCommand("cc-end").setExecutor(new EndCCCommand(this));
        getCommand("cc-undo").setExecutor(new UndoCommand(this));
        getCommand("cc-link").setExecutor(new LinkCCCommand(this));
        getCommand("cc-list").setExecutor(new ListCCCommand(this));
        getCommand("cc-remove").setExecutor(new RemoveCCCommand(this));

        // Creation répertoire
        File testInstall = new File("plugins/ClicClac");
        if (!testInstall.exists()) {
            testInstall.mkdir();
        }

        // Lecture du répertoire
        try {
            File[] files = SLAPI.listFiles("plugins", "ClicClac");
            for (File currentFile : files) {
                if (currentFile.getName().contains(".bin")) {
                    ArrayList<StructureCC> test;
                    test = (ArrayList<StructureCC>) SLAPI.load(currentFile.getPath());

                    if (test.size() > 0) {
                        structuresLoaded = test;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        init();

    }

    public boolean has(Player player, String permissionsNode) {
        return _permissions.has(player, permissionsNode);
    }
    
    public boolean hasGuard(Player player, String permissionsNode) {
        if (_permissions != null && !this.has(player, permissionsNode)) {
            player.sendMessage(ChatColor.RED
                    + "[ClicClac] You do not have permission to perform that action!");
            return false;
        } else {
            return true;
        }
    }
    
    
    public boolean init() {
        Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

        if (permissions == null) {
            return false;
        }
        
        _permissions = ((Permissions) permissions).getHandler();

        return true;
    }

    public boolean structureExist(String name) {
        for (StructureCC struc : structuresLoaded) {
            if (struc.name.equals(name)) {
                return true;
            }
        }

        return false;
    }

    public StructureCC getStructure(String name) {
        for (StructureCC struc : structuresLoaded) {
            if (struc.name.equals(name)) {
                return struc;
            }
        }

        return null;
    }

    // ////////////////////////////////////////////////////////
    // CreateCC
    // ////////////////////////////////////////////////////////

    public void startStructure(Player player, String name) {

        if (!this.structureExist(name)) {
            StructureCC cc = new StructureCC();
            cc.name = name;
            cc.currentState = 0;

            playerCreateCommand.put(player, cc);
        }
    }

    public boolean playerHasBeginStructure(Player player) {
        return playerCreateCommand.containsKey(player);
    }

    public void undoRemove(Player player) {
        playerCreateCommand.get(player).undoStart(player.getWorld());
    }

    public boolean playerHasBeginLink(Player player) {
        return playerActiveLink.containsKey(player);
    }

    public void endStructure(Player player) {
        // TODO Auto-generated method stub
        playerCreateCommand.remove(player);
    }

    public void saveStructure(StructureCC currentStructure) {
        this.structuresLoaded.add(currentStructure);

        try {
            SLAPI.save(this.structuresLoaded, "plugins/ClicClac/structuresLoaded.bin");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void activeLink(String eventName, Player player) {

        StructureCC test = this.getStructure(eventName);
        if (test != null) {
            playerActiveLink.put(player, test);
        }

    }

    public void modifyStructure(StructureCC currentStructure) {

        int pos = 0;
        for (int i = 0; i < structuresLoaded.size(); i++) {
            if (structuresLoaded.get(i).name.equals(currentStructure.name)) {
                pos = i;
            }
        }

        structuresLoaded.remove(pos);
        this.saveStructure(currentStructure);

    }

    public void onRedStoneChange(MyLocation buttonLocation, BlockRedstoneEvent event) {

        for (StructureCC structure : structuresLoaded) {
            for (MyLocation structureB : structure.boutons) {
                if (structureB.getX() == buttonLocation.getX() && structureB.getY() == buttonLocation.getY() && structureB.getZ() == buttonLocation.getZ()) {

                    structure.currentState = event.getNewCurrent();
                    structure.switchBlocs(event.getBlock().getWorld());

                }
            }
        }

    }

    public void removeStructure(String eventName, Player player) {

        StructureCC toRemove = null;
        for (StructureCC struc : structuresLoaded) {
            if (struc.name.equals(eventName)) {
                toRemove = struc;
            }
        }

        if (toRemove != null) {
            this.structuresLoaded.remove(toRemove);

            try {
                SLAPI.save(this.structuresLoaded, "plugins/ClicClac/structuresLoaded.bin");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}
