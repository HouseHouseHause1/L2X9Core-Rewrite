package org.l2x9.l2x9corerw.listener.antilag;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Arrays;
import java.util.List;

public class LiquidFlow extends BaseListener {

    private final double disableTps;
    private final boolean removeBlock;
    private final List<Material> blocks = Arrays.asList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER);

    public LiquidFlow() {
        super("PreventLiquidFlow",
                "Prevent liquid from flowing while the server is below a certain tps",
                Category.ANTILAG);
        disableTps = config.getDouble(getName() + ".DisableTPS");
        removeBlock = config.getBoolean(getName() + ".RemoveSource");
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event) {
        Block block = event.getBlock();
        if (!blocks.contains(block.getType())) return;
        double tps = Utils.getTPS();
        if (tps <= disableTps) {
            event.setCancelled(true);
            if (removeBlock) block.setType(Material.AIR);
        }
    }
}
