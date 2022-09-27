package org.l2x9.l2x9corerw.listener.antilag;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class WitherSkullFix extends BaseListener {

    private final boolean chunkLoad;
    private final boolean chunkUnload;

    public WitherSkullFix() {
        super(
                "WitherSkullFix",
                "deletes wither skulls when chunks are loaded / unloaded",
                Category.ANTILAG);
        chunkLoad = config.getBoolean(getName() + ".ChunkLoad");
        chunkUnload = config.getBoolean(getName() + ".ChunkUnload");
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (chunkLoad) removeSkulls(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (chunkUnload) removeSkulls(event.getChunk());
    }

    void removeSkulls(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            if (entity.getType() == EntityType.WITHER_SKULL) entity.remove();
        }
    }
}
