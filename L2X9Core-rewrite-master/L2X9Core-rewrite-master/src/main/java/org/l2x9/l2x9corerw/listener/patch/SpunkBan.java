package org.l2x9.l2x9corerw.listener.patch;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.l2x9.l2x9corerw.util.PlayerUtil;

import java.util.Arrays;
import java.util.List;

public class SpunkBan extends BaseListener {
    private final int maxTile;
    private final boolean sendMessage;
    private final String message;
    private final boolean crash;
    private final List<Material> checked = Arrays.asList(
      Material.STONE_PLATE,
      Material.FURNACE,
      Material.ENCHANTMENT_TABLE,
      Material.CHEST,
      Material.TRAPPED_CHEST,
      Material.SKULL
    );
    public SpunkBan() {
        super(
                "AntiChunkBan",
                "Prevent players from making a ChunkBan",
                Category.PATCHES
        );
        maxTile = config.getInt(getName() + ".MaxTileEntities");
        message = config.getString(getName() + ".Message");
        sendMessage = !message.isEmpty();
        crash = config.getBoolean(getName() + ".Crash");
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Chunk chunk = block.getChunk();
        Player player = event.getPlayer();
        if (!isChecked(block)) return;
        if (chunk.getTileEntities().length > maxTile) {
            event.setCancelled(true);
            if (sendMessage) {
                PlayerUtil.sendMessage(player, message);
            } else if (crash) {
                PlayerUtil.crashPlayer(player);
            }
        }
    }
    private boolean isChecked(Block block) {
        return checked.contains(block.getType());
    }
}
