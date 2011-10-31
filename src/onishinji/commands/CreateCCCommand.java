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
                player.sendMessage(plugin.getLocale("cc.error.missingName"));
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
               
                if (!plugin.structureExist(eventName, groupName)) {
                    if (!plugin.playerHasBeginStructure(player)) {
                        player.sendMessage(plugin.getLocale("cc.create.firstInstruction"));
                    } else {
                        
                        plugin.undoRemove(player);
                        player.sendMessage(plugin.getLocale("cc.create.cancelByRepeatCreate"));
                        player.sendMessage(plugin.getLocale("cc.create.firstInstruction"));
                    }

                    plugin.startStructure(player, eventName, groupName);
                } else {
                    player.sendMessage(String.format(plugin.getLocale("cc.create.error.nameIsTakenInGroup"),groupName));
                }
            }

        }
        return true;
    }
    
   
}
