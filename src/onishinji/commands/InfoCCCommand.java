package onishinji.commands;

import java.util.ArrayList;

import onishinji.ClicClac;
import onishinji.StructureCC;
import onishinji.models.BlockManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCCCommand implements CommandExecutor {

    ClicClac plugin;

    public InfoCCCommand(ClicClac clicClac) {
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

            if (!plugin.hasGuard((Player) sender, "cc.info")) {
                return true;
            }

            String[] split = args;

            if (split.length < 1) {
                player.sendMessage("Euh, je n'ai pas bien saisie le nom de la zone Clic Clac ...");
                return true;
            } else {
                String eventName = split[0];
                String groupName = plugin.getGroupNameFromArgs(args);

                if (plugin.structureExist(eventName, groupName)) {

                    StructureCC cc = plugin.getStructure(eventName, groupName);

                    player.sendMessage(ChatColor.YELLOW + "Information de la Zone clic clac " + cc.name + " du groupe '" + cc.groupName + "'");
                    player.sendMessage(ChatColor.YELLOW + " > crée par " + cc.playerName + " dans le monde '" + cc.worldName+"'");
                    player.sendMessage(ChatColor.YELLOW + " > elle contient " + cc.blocsInitial.toArray().length + " blocs");
                    if(cc.isBreakable()) player.sendMessage(ChatColor.YELLOW + " > Elle est cassable !");

                   this.checkImportantBloc(player, cc, Material.IRON_BLOCK);
                   this.checkImportantBloc(player, cc, Material.IRON_ORE);
                   this.checkImportantBloc(player, cc, Material.GOLD_BLOCK);
                   this.checkImportantBloc(player, cc, Material.GOLD_ORE);
                   this.checkImportantBloc(player, cc, Material.DIAMOND_BLOCK);
                   this.checkImportantBloc(player, cc, Material.LAVA); 
                   this.checkImportantBloc(player, cc, Material.LAPIS_BLOCK); 
                   this.checkImportantBloc(player, cc, Material.OBSIDIAN); 
                   this.checkImportantBloc(player, cc, Material.TNT); 
                   this.checkImportantBloc(player, cc, Material.WATER); 

                    return true;

                } else {
                    player.sendMessage("Je ne connais pas cette zone Clic Clac");
                    return true;
                }
            }
        }
        return false;

    }
    
    private int countBlocOf(ArrayList<BlockManager> blocArray, Material matRef) {

        int nb = 0;
        for (BlockManager block : blocArray) {
            if (block.getType().equals(matRef)) {
                nb++;
            }
        }
        return nb;

    }
    
    private void checkImportantBloc(Player player, StructureCC cc, Material matRef)
    {
        if(!cc.isMultiCC())
        {
            int nbImportantBlock = 0;
            int nbImportantBlockStep1 = this.countBlocOf(cc.blocsInitial, matRef);
            int nbImportantBlockStep2 = this.countBlocOf(cc.blocsFinal, matRef);
            
            nbImportantBlock = nbImportantBlockStep1 + nbImportantBlockStep2;
            
            if(nbImportantBlock > 0)
            { 
                player.sendMessage(ChatColor.YELLOW + "!!! il y a " + nbImportantBlock +" blocs de " +matRef +" (" + nbImportantBlockStep1 + " dans l'état 1, " + nbImportantBlockStep2 +" dans l'état 2)");
            }
        }
        else
        {
            int i=0;
            for(ArrayList<BlockManager> blocs : cc.steps)
            { 
                i++;
                int nbImportantBlock = this.countBlocOf(blocs, matRef);
                if(nbImportantBlock > 0)
                {
                    player.sendMessage(ChatColor.YELLOW + "!!! il y a " + nbImportantBlock +" blocs de " +matRef + " dans l'étape " + i);
                }
            }
        }
       
    }

}
