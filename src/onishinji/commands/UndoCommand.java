package onishinji.commands;

import onishinji.ClicClac;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UndoCommand implements CommandExecutor {

    private ClicClac plugin;

    public UndoCommand(ClicClac clicClac) {
        this.plugin = clicClac;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        
        if (!plugin.hasGuard((Player) sender, "cc.create")) {
            return true;
        }

        
        if (plugin.playerHasBeginStructure(player)) {

            player.sendMessage("Annulation de la dernière zone  Clic Clac");
            plugin.undoRemove(player);
        }
        else
        {
            player.sendMessage("Rien trouvé à annuler");
        }

        return false;
    }

}
