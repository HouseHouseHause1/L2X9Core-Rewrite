package org.l2x9.l2x9corerw.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemUtil extends Utils {
    private static final List<Material> armor = Arrays.asList(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS,
            Material.DIAMOND_BOOTS, Material.GOLD_BOOTS, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS,
            Material.IRON_LEGGINGS, Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS,
            Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.LEATHER_HELMET,
            Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLD_HELMET,
            Material.ELYTRA);
    private static final List<Material> tools = Arrays.asList(Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD,
            Material.DIAMOND_HOE, Material.DIAMOND_SPADE, Material.IRON_AXE, Material.IRON_HOE,
            Material.IRON_PICKAXE, Material.IRON_SPADE, Material.IRON_SWORD, Material.GOLD_AXE, Material.GOLD_HOE,
            Material.GOLD_PICKAXE, Material.GOLD_SWORD, Material.WOOD_AXE, Material.WOOD_HOE, Material.WOOD_PICKAXE,
            Material.WOOD_SWORD, Material.CARROT_STICK, Material.SHEARS, Material.FISHING_ROD, Material.BOW,
            Material.FLINT_AND_STEEL);

    public static boolean isArmor(ItemStack item) {
        return armor.contains(item.getType());
    }

    public static boolean isTool(ItemStack item) {
        return tools.contains(item.getType());
    }

    public static boolean isIllegal(ItemStack item) {
        return plugin.getIllegalItems().contains(item.getType());
    }

    public static boolean hasIllegalNBT(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES) || meta.hasItemFlag(ItemFlag.HIDE_DESTROYS)
                    || meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) || meta.hasItemFlag(ItemFlag.HIDE_PLACED_ON)
                    || meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS) || meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                    || meta.isUnbreakable() || meta.hasLore();
        }
        return false;
    }

    public static boolean isOverStacked(ItemStack item) {
        if (plugin.getConfig().getBoolean("AntiIllegal.Delete-Stacked-Items")) {
            return item.getAmount() > item.getMaxStackSize();
        } else {
            return false;
        }
    }

    public static boolean hasIllegalEnchants(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasEnchants()) return false;
        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            if (!entry.getKey().canEnchantItem(item)) return true;
            if (entry.getValue() > entry.getKey().getMaxLevel()) return true;
            break;
        }
        return false;
    }

    public static boolean isEnchantedBlock(ItemStack item) {
        if (item.getType().isBlock()) {
            if (item.hasItemMeta()) {
                return item.getItemMeta().hasEnchants();
            }
        }
        return false;
    }

    public static void deleteIllegals(Inventory inventory) {
        if (inventory.getContents() != null) {
            for (ItemStack item : inventory.getStorageContents()) {
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    revertDurability(item);
                    if (ItemUtil.isIllegal(item)) {
                        inventory.remove(item);
                    }
                    if (ItemUtil.hasIllegalNBT(item)) {
                        if (meta.isUnbreakable()) meta.setUnbreakable(false);
                        if (meta.hasLore()) meta.setLore(null);
                        meta.removeItemFlags(ItemFlag.values());
                    }
                    if (ItemUtil.isOverStacked(item)) {
                        item.setAmount(item.getMaxStackSize());
                    }
                    if (ItemUtil.hasIllegalEnchants(item)) {
                        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : item.getEnchantments().entrySet()) {
                            item.removeEnchantment(enchantmentIntegerEntry.getKey());
                        }
                    }
                    if (item.hasItemMeta()) {
                        if (meta.getDisplayName() != null) {
                            ItemUtil.removeColours(item);
                        }
                        if (ItemUtil.isEnchantedBlock(item)) {
                            item.getEnchantments().forEach((key, value) -> item.removeEnchantment(key));
                        }
                        if (isShulker(item)) {
                            BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                            ShulkerBox shulker = (ShulkerBox) blockStateMeta.getBlockState();
                            for (ItemStack shulkerItem : shulker.getInventory().getContents()) {
                                if (shulkerItem != null) {
                                    ItemMeta shulkerItemMeta = shulkerItem.getItemMeta();
                                    revertDurability(shulkerItem);
                                    removeColours(shulkerItem);
                                    if (ItemUtil.isIllegal(shulkerItem)) {
                                        shulker.getInventory().remove(shulkerItem);
                                    }
                                    if (ItemUtil.hasIllegalNBT(shulkerItem)) {
                                        if (shulkerItemMeta.isUnbreakable()) shulkerItemMeta.setUnbreakable(false);
                                        if (shulkerItemMeta.hasLore()) meta.setLore(null);
                                        shulkerItem.removeItemFlags(ItemFlag.values());
                                    }
                                    if (ItemUtil.isOverStacked(shulkerItem)) {
                                        shulkerItem.setAmount(shulkerItem.getType().getMaxStackSize());
                                    }
                                    if (ItemUtil.hasIllegalEnchants(shulkerItem)) {
                                        for (Map.Entry<Enchantment, Integer> enchantmentIntEntry : shulkerItem.getEnchantments().entrySet()) {
                                            if (enchantmentIntEntry.getValue() > enchantmentIntEntry.getKey().getMaxLevel()) {
                                                if (enchantmentIntEntry.getKey().canEnchantItem(shulkerItem)) {
                                                    shulkerItem.addEnchantment(enchantmentIntEntry.getKey(), enchantmentIntEntry.getKey().getMaxLevel());
                                                } else {
                                                    shulkerItem.removeEnchantment(enchantmentIntEntry.getKey());
                                                }
                                            }
                                        }
                                    }
                                    if (ItemUtil.isEnchantedBlock(shulkerItem)) {
                                        shulkerItem.getEnchantments().forEach((key, value) -> shulkerItem.removeEnchantment(key));
                                    }
                                }
                            }
                            blockStateMeta.setBlockState(shulker);
                            item.setItemMeta(blockStateMeta);
                        }
                    }
                }
            }
        }
    }

    private static void revertDurability(ItemStack item) {
        if (ItemUtil.isArmor(item) || ItemUtil.isTool(item)) {
            if (item.getDurability() > item.getType().getMaxDurability()) {
                item.setDurability(item.getType().getMaxDurability());
            }
            if (item.getDurability() < 0) {
                item.setDurability((short) 1);
            }
        }
    }

    public static boolean isShulker(ItemStack item) {
        return item.hasItemMeta()
                && item.getItemMeta() instanceof BlockStateMeta
                && ((BlockStateMeta) item.getItemMeta()).getBlockState() instanceof ShulkerBox;
    }

    public static void removeColours(ItemStack item) {
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return;
        String display = meta.getDisplayName();
        display = ChatColor.stripColor(display);
        if (display.length() > 35) display = display.substring(0, 35);
        meta.setDisplayName(display);
        item.setItemMeta(meta);
    }
}
