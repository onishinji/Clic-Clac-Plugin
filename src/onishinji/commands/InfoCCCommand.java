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

    String prefixInfo = " > ";
    
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
                player.sendMessage(plugin.getLocale("cc.error.missingName"));
                return true;
            } else {
                String eventName = split[0];
                String groupName = plugin.getGroupNameFromArgs(args);

                if (plugin.structureExist(eventName, groupName)) {

                    StructureCC cc = plugin.getStructure(eventName, groupName);

                    player.sendMessage(String.format(plugin.getLocale("cc.info.name"), cc.name, cc.groupName));
                    player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.createdBy"), cc.playerName, cc.worldName));
                    player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.stats.default"), cc.blocsInitial.toArray().length));
                   
                    if(cc.isMultiCC())
                    {
                        player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.animated.steps"), cc.steps.size(), cc.currentState+1));
                    }
                    
                    if(cc.isBreakable()) {
                        player.sendMessage(prefixInfo + plugin.getLocale("cc.break.isNowActive"));
                    }
                    else
                    {
                        player.sendMessage(prefixInfo + plugin.getLocale("cc.break.isNowProtected"));
                    }
                    
                    if(cc.isDateLimited) {
                        player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.stats.dateLimited"), cc.timeForDateLimited)); 
                    }
                    
                    if(cc.canBeAnimated())
                    {
                        player.sendMessage(prefixInfo + plugin.getLocale("cc.info.animated.ready"));
                    }
                    else 
                    {
                        player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.animated.notReady"), cc.lastUsed));
 
                    }

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
                    player.sendMessage(prefixInfo + plugin.getLocale("cc.error.unknowCC"));
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
                player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.warning.simple"), nbImportantBlock, matRef, nbImportantBlockStep1, nbImportantBlockStep2));
             
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
                    player.sendMessage(prefixInfo + String.format(plugin.getLocale("cc.info.warning.multi"), nbImportantBlock, matRef, i)); 
                }
            }
        }
       
    }

}
