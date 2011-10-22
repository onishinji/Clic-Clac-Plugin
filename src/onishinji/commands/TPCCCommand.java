package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCCCommand implements CommandExecutor {

    ClicClac plugin;
    public TPCCCommand(ClicClac clicClac) {
        // TODO Auto-generated constructor stub
        plugin = clicClac;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (sender instanceof Player) {

            Player player = (Player) sender;
            
            if (!plugin.hasGuard((Player) sender, "cc.tp")) {
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
              
                    StructureCC cc = plugin.getStructure(eventName, groupName);
                    player.teleport(cc.blocsFinal.get(0).getLocation(plugin.getServer().getWorld(cc.worldName)));                    
                    
                    
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
