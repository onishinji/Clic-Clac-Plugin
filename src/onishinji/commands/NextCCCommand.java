package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NextCCCommand implements CommandExecutor {

    private ClicClac plugin;

    public NextCCCommand(ClicClac clicClac) {
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
            
            
            if (!plugin.hasGuard((Player) sender, "cc.next")) {
                return true;
            }

            
            if(plugin.playerHasBeginStructure(player))
            {
                StructureCC currentStructure = plugin.playerCreateCommand.get(player);
                currentStructure.addStep(player);
                player.sendMessage(plugin.getLocale("cc.create.nextSaved"));
 
                return true;
            }
        }
        
        return true;
    }

}
