package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.l2x9.l2x9corerw.util.ItemUtil;

public class HopperTransfer extends BaseListener {
    public HopperTransfer() {
        super(
                "AntiIllegalHopperTransfer",
                "Remove illegals when a hopper moves an item",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryMoveItemEvent event) {
        if (plugin.getConfig().getBoolean("AntiIllegal.HopperTransfer-Enabled")) {
            Inventory inv = event.getSource();
            if (inv.getContents() != null) {
                for (ItemStack item : inv.getStorageContents()) {
                    if (item != null) {
                        if (ItemUtil.isArmor(item) || ItemUtil.isTool(item)) {
                            if (item.getDurability() > item.getType().getMaxDurability()) {
                                item.setDurability(item.getType().getMaxDurability());
                            }
                            if (item.getDurability() < 0) {
                                item.setDurability((short) 1);
                            }

                        }
                        if (ItemUtil.isIllegal(item)) {
                            inv.remove(item);
                            event.setCancelled(true);
                        }
                        if (ItemUtil.hasIllegalNBT(item)) {
                            inv.remove(item);
                            event.setCancelled(true);

                        }
                        if (ItemUtil.isOverStacked(item)) {
                            item.setAmount(item.getMaxStackSize());
                            event.setCancelled(true);
                        }
                        if (ItemUtil.hasIllegalEnchants(item)) {
                            inv.remove(item);
                            event.setCancelled(true);
                        }
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta.hasDisplayName()) {
                                ItemUtil.removeColours(item);
                                item.setItemMeta(meta);
                            }
                            if (ItemUtil.isEnchantedBlock(item)) {
                                event.setCancelled(true);
                            }
                            if (ItemUtil.isShulker(item)) {
                                BlockStateMeta itemMeta = (BlockStateMeta) item.getItemMeta();
                                ShulkerBox shulker = (ShulkerBox) itemMeta.getBlockState();
                                for (ItemStack shulkerItem : shulker.getInventory().getContents()) {
                                    if (shulkerItem != null) {
                                        if (ItemUtil.isArmor(item) || ItemUtil.isTool(item)) {
                                            if (item.getDurability() > item.getType().getMaxDurability()) {
                                                inv.remove(item);
                                                event.setCancelled(true);
                                            }
                                            if (item.getDurability() < 0) {
                                                inv.remove(item);
                                                event.setCancelled(true);
                                            }
                                        }
                                        if (ItemUtil.isIllegal(shulkerItem)) {
                                            inv.remove(item);
                                        }
                                        if (ItemUtil.hasIllegalNBT(shulkerItem)) {
                                            inv.remove(item);
                                            event.setCancelled(true);
                                        }
                                        if (ItemUtil.isOverStacked(shulkerItem)) {
                                            inv.remove(item);
                                            event.setCancelled(true);
                                        }
                                        if (ItemUtil.hasIllegalEnchants(shulkerItem)) {
                                            inv.remove(item);
                                            event.setCancelled(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}