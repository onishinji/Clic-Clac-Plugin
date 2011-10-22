package onishinji.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO Auto-generated method stub

        if (sender instanceof Player) {

            if (!plugin.hasGuard((Player) sender, "cc.list")) {
                return true;
            }

            Player player = (Player) sender;

            /* Tous les groupes*/
            if (args.length == 0) {

                String liste = "Liste des groupes Clic Clac: ";

                HashMap<String, Integer> groupNames = new HashMap<String, Integer>();

                for (StructureCC structure : plugin.structuresLoaded) {
                    groupNames.put(structure.groupName, 1);
                }

                liste = liste + this.createListeFromArray(groupNames);

                player.sendMessage(liste);
            } else {
                String groupName = "";
                if (args.length > 1) {
                    for (int i = 0; i < args.length; i++) {
                        groupName = groupName + " " + args[i];
                    }
                    groupName = groupName.substring(1, groupName.length());
                } else {
                    groupName = args[0];
                }

                String liste = "Liste des zones Clic Clac de '" + groupName + "': ";

                HashMap<String, Integer> listeStructure = new HashMap<String, Integer>();

                for (StructureCC structure : plugin.getStructureByGroupName(groupName)) {
                    listeStructure.put(structure.name, 1);
                }
                liste = liste + this.createListeFromArray(listeStructure);

                player.sendMessage(liste);
            }
        }

        return true;
    }

    private String createListeFromArray(HashMap<String, Integer> groupNames) {
        String liste = "";
        for (String entry : groupNames.keySet()) {
            liste = liste + entry + ", ";
        }

        if (plugin.structuresLoaded.size() > 0 && liste.length() > 0) {
            liste = liste.substring(0, liste.length() - 2);
        } else {
            liste = liste + ChatColor.RED + " Aucun Clic Clac chargé.";
        }

        return liste;
    }

}
