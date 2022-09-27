package org.l2x9.l2x9corerw.listener.patch;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;

import java.util.Arrays;
import java.util.List;

public class EndPortalGrief extends BaseListener {
    List<Material> portal = Arrays.asList(Material.ENDER_PORTAL, Material.ENDER_PORTAL_FRAME);
    List<Material> buckets = Arrays.asList(Material.LAVA_BUCKET, Material.WATER_BUCKET);
    private final boolean sendMessage;
    private final String message;
    private final boolean crash;

    public EndPortalGrief() {
        super(
                "AntiEndPortalGrief",
                "Prevent players from destroying end portals",
                Category.PATCHES
        );
        message = config.getString(getName() + ".Message");
        sendMessage = !message.isEmpty();
        crash = config.getBoolean(getName() + ".Crash");
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked();
        for (BlockFace face : BlockFace.values()) {
            if (portal.contains(block.getRelative(face).getType())) {
                event.setCancelled(true);
                Utils.log("&3Prevented&r&a " + event.getPlayer().getName() + " &r&3in " + Utils.formatLocation(block.getLocation()) + " &r&3from destroying an end portal");
                if (sendMessage) {
                    PlayerUtil.sendMessage(event.getPlayer(), message);
                } else if (crash) {
                    PlayerUtil.crashPlayer(event.getPlayer());
                }
                break;
            }
        }
    }
    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        Material type = event.getItem().getType();
        if (buckets.contains(type)) {
            for (BlockFace face : BlockFace.values()) {
                if (portal.contains(block.getRelative(face).getType())) {
                    Utils.log("&3Prevented an EndPortal from being destroyed in " + Utils.formatLocation(block.getLocation()));
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}