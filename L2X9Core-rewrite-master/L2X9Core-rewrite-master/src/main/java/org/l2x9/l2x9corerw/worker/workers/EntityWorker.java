package org.l2x9.l2x9corerw.worker.workers;

import org.l2x9.l2x9corerw.worker.Worker;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class EntityWorker extends Worker {
    HashMap<EntityType, Integer> map;

    public EntityWorker() {
        super(20000, "EntityWorker");
        map = plugin.getEntityPairMap();
    }

    @Override
    public void execute() {
        for (Chunk chunk : getChunks()) {
            for (Map.Entry<EntityType, Integer> entry : map.entrySet()) {
                removeAmount(entry.getKey(), entry.getValue(), chunk);
            }
        }
    }

    private ArrayList<Chunk> getChunks() {
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (World world : plugin.getServer().getWorlds()) {
            chunks.addAll(Arrays.asList(world.getLoadedChunks()));
        }
        return chunks;
    }

    private void removeAmount(EntityType type, int maxAmount, Chunk chunk) {
        List<Entity> correctType = new ArrayList<>();
        Arrays.stream(chunk.getEntities()).forEach(entity -> {
            if (entity.getType() == type) correctType.add(entity);
        });
        int entityAmount = correctType.size();
        if (entityAmount <= maxAmount) return;
        List<Entity> sized = correctType.subList(0, entityAmount - maxAmount);
        sized.forEach(Entity::remove);
    }
}
