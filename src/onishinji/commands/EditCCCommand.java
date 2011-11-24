package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EditCCCommand implements CommandExecutor {

    ClicClac plugin;
    String currentMode;
    
    public EditCCCommand(ClicClac clicClac, String mode) {
        plugin = clicClac;
        currentMode = mode;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (!plugin.hasGuard((Player) sender, "cc.edit")) {
            return true;
        }

        if (sender instanceof Player) {

            Player player = (Player) sender;
            String[] split = args;

            if (split.length < 1) {
                player.sendMessage(plugin.getLocale("cc.error.missingName"));
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
                
                if(currentMode.equals("start"))
                {
                    if (plugin.structureExist(eventName, groupName)) {
                        plugin.getStructure(eventName, groupName).isEditable = true;
                        player.sendMessage(String.format(plugin.getLocale("cc.edit.start"), eventName, groupName));
                    } else {
                        player.sendMessage(plugin.getLocale("cc.error.unknowCC"));
                    }
                }
                
                System.out.println("current "  + currentMode);
                if(currentMode.equals("end"))
                { 
                    if (plugin.structureExist(eventName, groupName)) {
                        StructureCC current = plugin.getStructure(eventName,groupName);
                        if(!current.isEditable)
                        {
                            player.sendMessage(plugin.getLocale("cc.edit.errorccWasntEditable"));
                            return true;
                        }
                        current.isEditable = false;
                        current.updateBlock(player);
                        plugin.removeStructure(eventName,groupName, player);
                        plugin.saveStructure(current);
                        
                        player.sendMessage(plugin.getLocale("cc.edit.end"));
                    } else {
                        player.sendMessage(plugin.getLocale("cc.error.unknowCC"));
                    }
                }
                
                
                
            }
        }

        return true;
    }

}
