package org.l2x9.l2x9corerw.listener.patch;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.l2x9.l2x9corerw.util.ItemUtil;
import org.l2x9.l2x9corerw.util.PlayerUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class BookBan extends BaseListener {
    private final List<Material> books = Arrays.asList(Material.WRITTEN_BOOK, Material.BOOK_AND_QUILL);
    private final boolean sendMessage;
    private final String message;

    public BookBan() {
        super(
                "AntiBookBan",
                "Fix the bookban exploit",
                Category.PATCHES
        );
        sendMessage = config.getBoolean(getName() + ".SendMessage");
        message = config.getString(getName() + ".Message");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean unbanned = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (ItemUtil.isShulker(item)) {
                    BlockStateMeta stateMeta = (BlockStateMeta) item.getItemMeta();
                    ShulkerBox box = (ShulkerBox) stateMeta.getBlockState();
                    for (ItemStack boxItem : box.getInventory().getContents()) {
                        if (boxItem != null && books.contains(boxItem.getType())) {
                            BookMeta book = (BookMeta) boxItem.getItemMeta();
                            if (isBanBook(book)) {
                                World world = player.getWorld();
                                unbanned = true;
                                world.dropItemNaturally(player.getLocation(), boxItem);
                                box.getInventory().remove(boxItem);
                            }
                        }
                    }
                    stateMeta.setBlockState(box);
                    item.setItemMeta(stateMeta);
                }
            }
        }
        if (unbanned && sendMessage) {
            PlayerUtil.sendMessage(player, message);
        }
    }

    public boolean isBanBook(BookMeta book) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        for (String page : book.getPages()) {
            return !pattern.matcher(page).matches();
        }
        return false;
    }
}
