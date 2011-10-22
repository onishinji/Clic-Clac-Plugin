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
                player.sendMessage("Euh, je n'ai pas bien saisie le nom de la zone Clic Clac ...");
                return true;
            } else {
                String eventName = split[0];
                String groupName =  plugin.getGroupNameFromArgs(args);
                
                if (plugin.structureExist(eventName,groupName)) {
                    player.sendMessage("Clique sur le bloc qui te servira d'interrupteur.");
                    plugin.activeLink(eventName,groupName, player, false);
                    
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
