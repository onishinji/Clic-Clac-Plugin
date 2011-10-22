package onishinji.commands;

import java.util.ArrayList;

import onishinji.ClicClac;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
 
public class CreateCCCommand implements CommandExecutor {

    private ClicClac plugin;

    public CreateCCCommand(ClicClac cacheCache) {
        plugin = cacheCache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (sender instanceof Player) {
            
            
            if (!plugin.hasGuard((Player) sender, "cc.create")) {
                return true;
            }

            
            Player player = (Player) sender;
            String[] split = args;

            if (split.length < 1) {
                player.sendMessage("Euh, je n'ai pas bien saisie le nom du Clic Clac ...");
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
               
                if (!plugin.structureExist(eventName, groupName)) {
                    if (!plugin.playerHasBeginStructure(player)) {
                        player.sendMessage("clique sur le premier point de la zone Clic Clac, pour annuler retappe la même commande ou /cc-undo");
                    } else {
                        
                        plugin.undoRemove(player);
                        player.sendMessage("Tu as annulé cette zone Clic Clac en rettapant la commande, ne te trompe pas cette fois ci :)");
                        player.sendMessage("clique sur le premier point de la zone de cache cache");
                    }

                    plugin.startStructure(player, eventName, groupName);
                } else {
                    player.sendMessage("Désolé, une zone Clic Clac cache porte déjà ce nom dans le groupe '" + groupName+"', recommence");
                }
            }

        }
        return true;
    }
    
   
}
