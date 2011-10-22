package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BreakCCCommand implements CommandExecutor {

    private ClicClac plugin;

    public BreakCCCommand(ClicClac clicClac) {
        plugin = clicClac;
        // TODO Auto-generated constructor stub
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            
            if (!plugin.hasGuard((Player) sender, "cc.break")) {
                return true;
            }

            
            String[] split = args;

            if (split.length < 1) {
                player.sendMessage("Euh, je n'ai pas bien saisie le nom de la zone Clic Clac ...");
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
                
                if (plugin.structureExist(eventName,groupName)) {
                 
                    StructureCC cc = plugin.getStructure(eventName,groupName);
                    cc.isBreakable = !cc.isBreakable;
                    plugin.modifyStructure(cc);
                    
                    if(cc.isBreakable())
                    {
                        player.sendMessage(ChatColor.YELLOW +" Cette zone Clic Clac est cassable maintenant");
                    }
                    else
                    {
                        player.sendMessage(ChatColor.YELLOW +" Cette zone Clic Clac n'est plus cassable maintenant");
                    }
                    
                    return true;

                } else {
                    player.sendMessage("Je ne connais pas cette zone Clic Clac");
                    return true;
                }
            }
        }
        return false;
    }
}
