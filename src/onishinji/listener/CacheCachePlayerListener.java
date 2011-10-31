package onishinji.listener;

import java.awt.Color;
import java.util.ArrayList;

import onishinji.ClicClac;
import onishinji.StructureCC;
import onishinji.models.MyLocation;
import org.bukkit.block.Sign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener; 

public class CacheCachePlayerListener extends PlayerListener {

    private ClicClac plugin;

    public CacheCachePlayerListener(ClicClac giveItemOnEvent) {
        // TODO Auto-generated constructor stub
        plugin = giveItemOnEvent;
    }
    
    public void onPlayerInteract(PlayerInteractEvent event) {
        
        // click droit pancarte
        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            
            if(event.getClickedBlock().getState() instanceof Sign)
            {
                Block block = event.getClickedBlock();
               plugin.checkSign(block, event.getPlayer().getWorld(), event.getPlayer());
            }
        }
        
        if (event.getClickedBlock() != null && event.getAction() == Action.LEFT_CLICK_BLOCK) {

            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            //////////////////////////////////////////////////////////////////
            // CREATE
            //////////////////////////////////////////////////////////////////
            if (plugin.playerHasBeginStructure(player)) {
                StructureCC currentStructure = plugin.playerCreateCommand.get(player);
                ArrayList<MyLocation> positions = currentStructure.bornes;

                Location location = player.getTargetBlock(null, 256).getLocation();
                
                positions.add(new MyLocation(location));
                currentStructure.bornes = positions; 
                
                boolean finish = this.checkIfStructureFinish(currentStructure, player);
                
                if(!finish)
                {
                    if (positions.size() == 1) {
                        player.sendMessage(plugin.getLocale("cc.create.secondInstruction"));
                    }
                }            }
            

            //////////////////////////////////////////////////////////////////
            // LINK
            //////////////////////////////////////////////////////////////////
            if(plugin.playerHasBeginLink(player))
            {
                StructureCC currentStructure = plugin.playerActiveLink.get(player);

                Location location = player.getTargetBlock(null, 256).getLocation();
                currentStructure.addButton(new MyLocation(location), currentStructure.sens);
                player.sendMessage(String.format(plugin.getLocale("cc.link.save"), currentStructure.name, currentStructure.groupName));
                
                plugin.playerActiveLink.remove(player);
                
                plugin.modifyStructure(currentStructure);
                
            }

        }
    }

    private boolean checkIfStructureFinish(StructureCC currentStructure, Player player) {
        
        boolean finish = currentStructure.bornes.size() == 2;
        
        if(finish)
        {
            
            // Che
            
            player.sendMessage(plugin.getLocale("cc.create.cuboidHasBeenCreated.text1"));
            player.sendMessage(plugin.getLocale("cc.create.cuboidHasBeenCreated.text2"));
            player.sendMessage(plugin.getLocale("cc.create.cuboidHasBeenCreated.text3")); 
             
            currentStructure.createStartStateAndRemoveInnerBloc(player);
            
            plugin.playerCreateCommand.remove(player);
            plugin.playerCreateCommand.put(player, currentStructure);
            
        }
        
        return finish;
    }

}
