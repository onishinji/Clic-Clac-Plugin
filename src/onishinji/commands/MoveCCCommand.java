package onishinji.commands;

import onishinji.ClicClac;
import onishinji.StructureCC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoveCCCommand implements CommandExecutor {

    ClicClac plugin;

    public MoveCCCommand(ClicClac clicClac) {
        plugin = clicClac;
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!plugin.hasGuard((Player) sender, "cc.move")) {
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

                String oldGroupName = "";
                String newGroupName = "";
                int pos = 0;
                if (args.length > 1) {
                    for (int i = 1; i < args.length; i++) {

                        boolean ignore;
                        if (args[i].equalsIgnoreCase(">")) {
                            pos = i;
                            ignore = true;
                        }
                        else
                        {
                            ignore = false;
                        }
                        if (pos == 0 && ignore == false)
                            oldGroupName = oldGroupName + " " + args[i];
                        else if(ignore == false)
                        {
                            newGroupName = newGroupName + " " + args[i];
                        }
                    }
                    if(!oldGroupName.equals(""))
                    oldGroupName = oldGroupName.substring(1, oldGroupName.length());

                    if(!newGroupName.equals("")) 
                    newGroupName = newGroupName.substring(1, newGroupName.length());
                }

                if (oldGroupName.equals("") || newGroupName.equals("")) {
                    player.sendMessage(ChatColor.RED + "Il faut tapper /cc-move nomZoneCC nomAncienGroupe > nomNouveauGroupe");
                    return true;
                }

                if (plugin.structureExist(eventName, oldGroupName)) {

                    if (plugin.structureExist(eventName, newGroupName)) {
                        player.sendMessage(ChatColor.RED + "On ne peut déplacer un clic clac dans un groupe ayant un clic clac du même nom");
                        return true;
                    }
                    StructureCC current = plugin.getStructure(eventName, oldGroupName);

                    plugin.removeStructure(eventName, oldGroupName, player);
                                    
                    current.groupName = newGroupName;
                    plugin.saveStructure(current);

                    player.sendMessage("La zone clic clac a été déplace de '" + oldGroupName + "' à '" + newGroupName + "' ");
                } else {
                    player.sendMessage(ChatColor.RED + "Zone clic clac inexistante.");
                }
            }

        }
        return true;
    }

}
