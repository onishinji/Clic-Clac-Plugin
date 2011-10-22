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
                player.sendMessage("Euh, je n'ai pas bien saisie le nom du Clic Clac ...");
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
                
                if(currentMode.equals("start"))
                {
                    if (plugin.structureExist(eventName, groupName)) {
                        plugin.getStructure(eventName, groupName).isEditable = true;
                        player.sendMessage("La zone clic clac est éditable, tappe '/cc-end-edit "+ eventName +" " + groupName+ "' pour terminer ");
                    } else {
                        player.sendMessage(ChatColor.RED + "Zone clic clac inexistante.");
                    }
                }
                
                if(currentMode.equals("end"))
                {
                    if (plugin.structureExist(eventName, groupName)) {
                        StructureCC current = plugin.getStructure(eventName,groupName);
                        if(!current.isEditable)
                        {
                            player.sendMessage("Cette zone clic clac n'était pas éditable ...");
                            return true;
                        }
                        current.isEditable = false;
                        current.updateBlock(player);
                        plugin.removeStructure(eventName,groupName, player);
                        plugin.saveStructure(current);
                        
                        player.sendMessage("La zone clic clac n'est plus éditable et a été sauvegardé");
                    } else {
                        player.sendMessage(ChatColor.RED + "Zone clic clac inexistante.");
                    }
                }
                
                
                
            }
        }

        return true;
    }

}
