package org.l2x9.l2x9corerw.listener.patch;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.concurrent.ConcurrentHashMap;

public class ChestLagFix extends BaseListener {
    private final ConcurrentHashMap<Object, Integer> vlMap;
    private final boolean crash;
    private final boolean kick;
    private final String kickMessage;
    private final int maxOpens;

    public ChestLagFix() {
        super(
                "AntiChestLag",
                "Prevent players from crashing the server by spam opening chests",
                Category.PATCHES
        );
        vlMap = new ConcurrentHashMap<>();
        plugin.getViolationMaps().add(vlMap);
        kick = config.getBoolean(getName() + ".Kick");
        kickMessage = config.getString(getName() + ".KickMessage");
        crash = config.getBoolean(getName() + ".Crash");
        maxOpens = config.getInt(getName() + ".MaxOpensPerSecond");
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Utils.incrementVLS(player, vlMap);
        if (vlMap.get(player) > maxOpens) {
            if (crash) {
                PlayerUtil.crashPlayer(player);
            } else if (kick) {
                PlayerUtil.kickPlayer(player, kickMessage);
            }
            vlMap.remove(player);
        }
    }
}
