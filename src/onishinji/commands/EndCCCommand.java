package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndCCCommand implements CommandExecutor {

    
    private ClicClac plugin;

    public EndCCCommand(ClicClac cacheCache) { 
        plugin = cacheCache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            
            if (!plugin.hasGuard((Player) sender, "cc.create")) {
                return true;
            }

            
            if(plugin.playerHasBeginStructure(player))
            {
                StructureCC currentStructure = plugin.playerCreateCommand.get(player);
                currentStructure.finishStructure(player);
                plugin.saveStructure(currentStructure);
                player.sendMessage(String.format(plugin.getLocale("cc.create.end"), currentStructure.name, currentStructure.groupName, currentStructure.name, currentStructure.groupName));

                plugin.playerCreateCommand.remove(player);
                return true;   
            }
        }
        
        return true;
    }

}
