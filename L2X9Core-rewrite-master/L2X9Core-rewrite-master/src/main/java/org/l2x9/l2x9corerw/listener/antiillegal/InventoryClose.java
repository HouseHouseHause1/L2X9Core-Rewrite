package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.l2x9.l2x9corerw.util.ItemUtil;

public class InventoryClose extends BaseListener {

    public InventoryClose() {
        super(
                "AntiIllegalInventoryClose",
                "Remove all illegal items when a player closes an inventory",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (plugin.getConfig().getBoolean("AntiIllegal.InventoryClose-Enabled")) {
            Inventory inv = event.getInventory();
            ItemUtil.deleteIllegals(inv);
            Inventory playerInv = event.getPlayer().getInventory();
            ItemUtil.deleteIllegals(playerInv);
            if (inv.getType() == InventoryType.SHULKER_BOX) {
                for (ItemStack item : inv) {
                    if (item != null) {
                        if (ItemUtil.isShulker(item)) {
                            inv.remove(item);
                        }
                    }
                }
            }
        }
    }
}