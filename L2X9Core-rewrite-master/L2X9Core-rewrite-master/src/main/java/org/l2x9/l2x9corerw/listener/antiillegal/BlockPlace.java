package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.l2x9.l2x9corerw.util.ItemUtil;

import java.util.Map.Entry;

public class BlockPlace extends BaseListener {

    public BlockPlace() {
        super(
                "AntiIllegalBlockPlace",
                "Prevent players from placing illegal blocks",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (plugin.getConfig().getBoolean("AntiIllegal.Block-Place-Enabled")) {
            if (ItemUtil.isIllegal(event.getItemInHand())) {
                event.setCancelled(true);
                event.getPlayer().getInventory().remove(event.getItemInHand());
            }
            ItemStack itemStack = event.getItemInHand();
            BlockStateMeta blockStateMeta;
            ShulkerBox shulkerBox = null;
            if (itemStack.getItemMeta() instanceof BlockStateMeta) {
                blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();
                if (blockStateMeta.getBlockState() instanceof ShulkerBox) {
                    shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
                    Inventory boxInventory = shulkerBox.getInventory();
                    for (ItemStack item : boxInventory.getContents()) {
                        if (item != null) {
                            if (ItemUtil.isArmor(item) || ItemUtil.isTool(item)) {
                                if (item.getDurability() > item.getType().getMaxDurability()) {
                                    item.setDurability(item.getType().getMaxDurability());
                                    event.setCancelled(true);
                                }
                                if (item.getDurability() < 0) {
                                    item.setDurability((short) 1);
                                    event.setCancelled(true);
                                }
                            }
                            if (ItemUtil.isIllegal(item)) {
                                boxInventory.remove(item);
                                event.setCancelled(true);
                            }
                            if (ItemUtil.hasIllegalNBT(item)) {
                                boxInventory.remove(item);
                                event.setCancelled(true);
                            }
                            if (ItemUtil.isOverStacked(item)) {
                                item.setAmount(item.getMaxStackSize());
                                event.setCancelled(true);
                            }
                            if (ItemUtil.hasIllegalEnchants(item)) {
                                for (Entry<Enchantment, Integer> enchantmentIntegerEntry : item.getEnchantments().entrySet()) {
                                    item.removeEnchantment(enchantmentIntegerEntry.getKey());
                                }
                                event.setCancelled(true);
                            }
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();
                                if (meta.getDisplayName() != null) {
                                    ItemUtil.removeColours(item);
                                }
                                if (ItemUtil.isEnchantedBlock(item)) {
                                    for (Entry<Enchantment, Integer> enchantmentIntegerEntry : item.getEnchantments().entrySet()) {
                                        item.removeEnchantment(enchantmentIntegerEntry.getKey());
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
                if (shulkerBox != null) {
                    blockStateMeta.setBlockState(shulkerBox);
                    itemStack.setItemMeta(blockStateMeta);
                }
            }
        }
    }
}