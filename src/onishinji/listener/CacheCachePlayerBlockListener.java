package onishinji.listener;

import onishinji.ClicClac;
import onishinji.models.MyLocation;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class CacheCachePlayerBlockListener extends BlockListener {

    private ClicClac plugin;

    public CacheCachePlayerBlockListener(ClicClac giveItemOnEvent) {
        // TODO Auto-generated constructor stub
        plugin = giveItemOnEvent;
    }

    
    public void onBlockRedstoneChange(BlockRedstoneEvent event)
    {
            MyLocation buttonLocation = new MyLocation(event.getBlock().getLocation());
            plugin.onRedStoneChange(buttonLocation, event);
    }
    
    public void onBlockBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        this.checkAndRemoveLevier(block, event.getPlayer());
    }
    
    public void onBlockBurn(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        this.checkAndRemoveLevier(block, event.getPlayer()); 
    }
    
    private void checkAndRemoveLevier(Block block, Player player)
    {  
       
    }

}
