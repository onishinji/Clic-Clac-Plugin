package onishinji.commands;

import onishinji.ClicClac;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCCCommand implements CommandExecutor {

    private ClicClac plugin;

    public RemoveCCCommand(ClicClac cacheCache) {
        plugin = cacheCache;
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            
            if (!plugin.hasGuard((Player) sender, "cc.remove")) {
                return true;
            }

            
            String[] split = args;

            if (split.length < 1) {
                player.sendMessage(plugin.getLocale("cc.error.missingName"));
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);

                if (plugin.structureExist(eventName,groupName)) {
                    player.sendMessage(String.format(plugin.getLocale("cc.remove.done"), eventName, groupName));
                    plugin.removeStructure(eventName,groupName, player);

                } else {
                    player.sendMessage(plugin.getLocale("cc.error.unknowCC"));
                }
            }
        }
        return false;
    }

}
