package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.l2x9.l2x9corerw.util.ItemUtil;

@SuppressWarnings("deprecation")
public class ItemPickup extends BaseListener {
    public ItemPickup() {
        super(
                "AntiIllegalItemPickup",
                "Will remove illegal items when an entity tries to pick them up",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (plugin.getConfig().getBoolean("AntiIllegal.ItemPickup-Enabled")) {
            ItemStack item = event.getItem().getItemStack();
            if (ItemUtil.isEnchantedBlock(item)
                    || ItemUtil.hasIllegalNBT(item) || ItemUtil.hasIllegalEnchants(item)
                    || ItemUtil.isOverStacked(item) || ItemUtil.isIllegal(item)) {
                event.setCancelled(true);
                event.getItem().remove();
            }
            if (ItemUtil.isShulker(item)) {
                BlockStateMeta itemMeta = (BlockStateMeta) item.getItemMeta();
                ShulkerBox box = (ShulkerBox) itemMeta.getBlockState();
                for (ItemStack shulkerItem : box.getInventory().getContents()) {
                    if (shulkerItem != null) {
                        if (ItemUtil.isArmor(item) || ItemUtil.isTool(item)) {
                            if (item.getDurability() > item.getType().getMaxDurability()) {
                                event.getItem().remove();
                                event.setCancelled(true);
                            }
                            if (item.getDurability() < 0) {
                                event.getItem().remove();
                                event.setCancelled(true);
                            }
                        }
                        if (ItemUtil.isIllegal(shulkerItem)) {
                            event.getItem().remove();
                        }
                        if (ItemUtil.hasIllegalNBT(shulkerItem)) {
                            event.getItem().remove();
                            event.setCancelled(true);

                        }
                        if (ItemUtil.isOverStacked(shulkerItem)) {
                            event.getItem().remove();
                            event.setCancelled(true);
                        }
                        if (ItemUtil.hasIllegalEnchants(shulkerItem)) {
                            event.getItem().remove();
                            event.setCancelled(true);
                        }
                    }
                }
                itemMeta.setBlockState(box);
                item.setItemMeta(itemMeta);
            }
        }
    }
}