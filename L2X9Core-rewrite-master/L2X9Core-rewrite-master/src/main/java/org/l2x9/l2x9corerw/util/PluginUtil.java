package org.l2x9.l2x9corerw.util;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PluginUtil extends Utils {
    public static HashMap<EntityType, Integer> setupEntityLimit() {
        try {
            List<String> entityPairs = plugin.getConfig().getStringList("EntityWorker.TypePairs");
            List<String> validEntities = new ArrayList<>();
            HashMap<EntityType, Integer> map = new HashMap<>();
            for (EntityType type : EntityType.values()) validEntities.add(type.toString());
            for (String pair : entityPairs) {
                String[] split = pair.split(":");
                String entityType = split[0].toUpperCase();
                int amount = Integer.parseInt(split[1]);
                if (validEntities.contains(entityType)) {
                    map.put(EntityType.valueOf(entityType), amount);
                } else {
                    log("&3Unknown EntityType&r&a " + entityType + "&r&3 in the EntityAmounts section of the config");
                    break;
                }
            }
            return map;
        } catch (Error | Exception throwable) {
            log("&3Error in the EntityAmounts section of the config missing \":\"");
            return null;
        }
    }

    public static List<Material> setupAntiIllegal() {
        List<String> items = plugin.getConfig().getStringList("AntiIllegal.Illegal-Items-List");
        List<Material> materials = new ArrayList<>();
        List<Material> knownMaterials = Arrays.asList(Material.values());
        for (String material : items) {
            String upperCaseMat = material.toUpperCase();
            if (knownMaterials.contains(Material.getMaterial(upperCaseMat))) {
                materials.add(Material.getMaterial(upperCaseMat));
            } else {
                log("&3Invalid configuration option AntiIllegal.Illegal-Items-List&r&a " + material);
            }
        }
        return materials;
    }
}
