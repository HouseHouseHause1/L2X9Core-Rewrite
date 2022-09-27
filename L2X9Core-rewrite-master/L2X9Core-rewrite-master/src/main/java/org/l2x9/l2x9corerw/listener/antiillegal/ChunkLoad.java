package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.l2x9.l2x9corerw.L2X9CoreRWBase;
import org.l2x9.l2x9corerw.util.ItemUtil;

public class ChunkLoad extends BaseListener {

    public ChunkLoad() {
        super(
                "AntiIllegalChunkLoad",
                "Remove illegals in all containers when a chunk gets loaded",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onLoad(ChunkLoadEvent event) {
        if (L2X9CoreRWBase.plugin.getConfig().getBoolean("AntiIllegal.ChunkLoad-Enabled")) {
            for (BlockState state : event.getChunk().getTileEntities()) {
                if (state instanceof Container) {
                    Container container = (Container) state;
                    ItemUtil.deleteIllegals(container.getInventory());
                }
            }
        }
    }
}