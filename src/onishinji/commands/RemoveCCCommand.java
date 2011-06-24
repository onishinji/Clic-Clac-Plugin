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

            if (split.length < 1 || split.length > 1) {
                player.sendMessage("Euh, je n'ai pas bien saisie le nom de la zone Clic Clac ...");
            } else {
                String eventName = split[0];

                if (plugin.structureExist(eventName)) {
                    player.sendMessage("Suppression de la zone Clic Clac " + eventName + " terminé." );
                    plugin.removeStructure(eventName, player);

                } else {
                    player.sendMessage("Je ne connais pas cette zone Clic Clac");
                }
            }
        }
        return false;
    }

}
