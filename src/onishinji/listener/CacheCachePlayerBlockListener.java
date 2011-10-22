package onishinji.listener;

import onishinji.ClicClac;
import onishinji.models.MyLocation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.material.Sign;

public class CacheCachePlayerBlockListener extends BlockListener {

    private ClicClac plugin;

    public CacheCachePlayerBlockListener(ClicClac giveItemOnEvent) {
        // TODO Auto-generated constructor stub
        plugin = giveItemOnEvent;
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        if (plugin.hasBlockOnStructure(new MyLocation(event.getBlock().getLocation()))) {
            event.setCancelled(true);
        }
    }

    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        MyLocation buttonLocation = new MyLocation(event.getBlock().getLocation());
  
                Block block = event.getBlock().getRelative(BlockFace.SELF);

                Material mat = block.getType();
                if (mat.equals(Material.SIGN) || mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN)) {//mat Material.SIGN) {

                    plugin.checkSign(block, event.getBlock().getWorld(), null);
                }
        plugin.onRedStoneChange(buttonLocation, event);
    }

    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        this.checkAndRemoveLevier(block, event.getPlayer());
    }

    public void onBlockBurn(BlockBreakEvent event) {
        Block block = event.getBlock();
        this.checkAndRemoveLevier(block, event.getPlayer());
    }

    private void checkAndRemoveLevier(Block block, Player player) {

    }

    public void onBlockDamage(BlockDamageEvent event) {

        if (plugin.hasBlockOnStructure(new MyLocation(event.getBlock().getLocation()))) {
            event.setCancelled(true);
        }
    }

}
