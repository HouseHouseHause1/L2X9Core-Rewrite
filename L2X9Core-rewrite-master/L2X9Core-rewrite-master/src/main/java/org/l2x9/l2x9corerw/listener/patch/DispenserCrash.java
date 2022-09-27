package org.l2x9.l2x9corerw.listener.patch;

import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.ItemUtil;
import org.l2x9.l2x9corerw.util.Utils;

public class DispenserCrash extends BaseListener {
    public DispenserCrash() {
        super(
                "AntiDispenserCrash",
                "Prevent asshats from crashing the server again",
                Category.PATCHES
        );
    }
    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        int height = event.getBlock().getY();
        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        if (!hasShulker(dispenser)) return;
        if (height == 255 || height <= 1) {
            event.setCancelled(true);
            Utils.log("&3Prevented a dispenser crash in " + Utils.formatLocation(dispenser.getLocation()));
        }
    }
    private boolean hasShulker(Dispenser dispenser) {
        for (ItemStack item : dispenser.getInventory()) {
            if (item != null && ItemUtil.isShulker(item)) {
                return true;
            }
        }
        return false;
    }
}
