package org.l2x9.l2x9corerw.listener.patch;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LightLag extends BaseListener {
    private final ConcurrentHashMap<Object, Integer> vlMap;
    private final HashMap<Player, Location> lastPlacedMap;
    private final HashMap<Player, Boolean> switchMap;
    private final int maxSpam;
    private final boolean crash;
    private final String kick;

    public LightLag() {
        super(
                "AntiLightLag",
                "Prevent players from doing skylight lag",
                Category.PATCHES
        );
        vlMap = new ConcurrentHashMap<>();
        plugin.getViolationMaps().add(vlMap);
        lastPlacedMap = new HashMap<>();
        switchMap = new HashMap<>();
        maxSpam = plugin.getConfig().getInt(getName() + ".MaxBlockPlaceSpam");
        crash = plugin.getConfig().getBoolean(getName() + ".Crash");
        kick = plugin.getConfig().getString(getName() + ".KickMessage");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        lastPlacedMap.putIfAbsent(player, null);
        vlMap.putIfAbsent(player, null);
        switchMap.putIfAbsent(player, false);
        switchMap.replace(player, !switchMap.get(player));
        if (switchMap.get(player)) {
            lastPlacedMap.replace(player, event.getBlock().getLocation());
        } else {
            if (lastPlacedMap.get(player).equals(event.getBlock().getLocation())) {
                Utils.incrementVLS(player, vlMap);
                if (vlMap.get(player) >= maxSpam / 2) {
                    if (crash) {
                        PlayerUtil.crashPlayer(player);
                    } else {
                        PlayerUtil.kickPlayer(player, kick);
                    }
                    lastPlacedMap.remove(player);
                    vlMap.remove(player);
                    switchMap.remove(player);
                }
            }
        }
    }
}
