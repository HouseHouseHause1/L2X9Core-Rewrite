package org.l2x9.l2x9corerw.listener.patch;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.concurrent.ConcurrentHashMap;

public class OffhandCrash extends BaseListener {
    ConcurrentHashMap<Object, Integer> vlMap;
    private final boolean kick;
    private final boolean crash;
    private final int max;
    private final String kickMessage;

    public OffhandCrash() {
        super(
                "OffhandCrashFix",
                "Prevent players from crashing the server using offhand crash",
                Category.PATCHES
        );
        vlMap = new ConcurrentHashMap<>();
        plugin.getViolationMaps().add(vlMap);
        kick = config.getBoolean(getName() + ".Kick");
        crash = config.getBoolean(getName() + ".Crash");
        kickMessage = config.getString(getName() + ".KickMessage");
        max = config.getInt(getName() + ".MaxOffhandSpam");
    }

    @EventHandler
    public void onOffhand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        Utils.incrementVLS(player, vlMap);
        if (vlMap.get(player) > max) {
            if (crash) {
                PlayerUtil.crashPlayer(player);
            } else if (kick) {
                PlayerUtil.kickPlayer(player, kickMessage);
            }
        }
    }
}
