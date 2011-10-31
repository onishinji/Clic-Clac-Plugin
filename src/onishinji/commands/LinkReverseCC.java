package onishinji.commands;

import onishinji.ClicClac;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkReverseCC implements CommandExecutor {

    private ClicClac plugin;

    public LinkReverseCC(ClicClac clicClac) {
        // TODO Auto-generated constructor stub
        plugin = clicClac;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            
            if (!plugin.hasGuard((Player) sender, "cc.link")) {
                return true;
            }

            
            String[] split = args;

            if (split.length < 1) {
                player.sendMessage(plugin.getLocale("cc.error.missingName"));
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
                
                if (plugin.structureExist(eventName,groupName)) {
                    player.sendMessage(plugin.getLocale("cc.link.click"));
                    plugin.activeLink(eventName,groupName, player, false);
                    
                    return true;

                } else {
                    player.sendMessage(plugin.getLocale("cc.error.unknowCC"));
                    return true;
                }
            }
        }
        return false;
    }
}
