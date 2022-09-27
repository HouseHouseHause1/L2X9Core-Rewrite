package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.l2x9.l2x9corerw.util.ItemUtil;

public class InventoryOpen extends BaseListener {

    public InventoryOpen() {
        super(
                "AntiIllegalInventoryOpen",
                "Remove illegal items when an inventory is opened",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryOpenEvent event) {
        if (plugin.getConfig().getBoolean("AntiIllegal.InventoryOpen-Enabled")) {
            Inventory inv = event.getInventory();
            ItemUtil.deleteIllegals(inv);
        }
    }
}