package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCCCommand implements CommandExecutor {

    private ClicClac plugin;

    public ListCCCommand(ClicClac cacheCache) {
        plugin = cacheCache;
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub

        if (sender instanceof Player) {

            
            if (!plugin.hasGuard((Player) sender, "cc.list")) {
                return true;
            }

            
            String liste = "Liste des zones Clic Clac: ";
            for (StructureCC structure : plugin.structuresLoaded) {
                liste = liste + structure.name + ", ";
            }
            if(plugin.structuresLoaded.size() > 0)
            {
                liste = liste.substring(0, liste.length() - 2);
            }
            else
            {
                liste = liste + ChatColor.RED + " Aucun Clic Clac chargé.";
            }
            Player player = (Player) sender;
            player.sendMessage(liste);

        }

        return true;
    }

}
