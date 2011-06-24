package onishinji.listener;

import java.util.ArrayList;

import onishinji.ClicClac;
import onishinji.StructureCC;
import onishinji.models.MyLocation;

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
                        player.sendMessage("Clique sur le deuxième point");
                    } else if (positions.size() == 1) {
                        //player.sendMessage("Clique sur le deuxième point");
                    }
                }
            }
            

            //////////////////////////////////////////////////////////////////
            // LINK
            //////////////////////////////////////////////////////////////////
            if(plugin.playerHasBeginLink(player))
            {
                StructureCC currentStructure = plugin.playerActiveLink.get(player);

                Location location = player.getTargetBlock(null, 256).getLocation();
                currentStructure.boutons.add(new MyLocation(location));
                player.sendMessage("Bloc interrupteur sauvegardé pour  " + currentStructure.name + ", Have Fun :)");
                
                plugin.playerActiveLink.remove(player);
                
                plugin.modifyStructure(currentStructure);
                
            }

        }
    }

    private boolean checkIfStructureFinish(StructureCC currentStructure, Player player) {
        
        boolean finish = currentStructure.bornes.size() == 2;
        
        if(finish)
        {
            
            player.sendMessage("Merci, la zone Clic Clac a été vidé, tappe /cc-end pour terminer ou /cc-undo");
            player.sendMessage("Veuillez maintenant construire la nouvelle forme à l'intérieur de la zone.");
             
            currentStructure.createStartStateAndRemoveInnerBloc(player);
            
            plugin.playerCreateCommand.remove(player);
            plugin.playerCreateCommand.put(player, currentStructure);
            
        }
        
        return finish;
    }

}
