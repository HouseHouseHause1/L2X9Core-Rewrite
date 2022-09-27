package org.l2x9.l2x9corerw.listener.patch;

import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;

public class AntiNetherRoof extends BaseListener {

    private final boolean teleport;
    private final boolean killPlayer;
    private final boolean sendMessage;
    private final String message;
    private final int topLayer;
    private final int bottomLayer;
    private final boolean preventInteract;

    public AntiNetherRoof() {
        super(
                "AntiNetherRoof",
                "block players from getting onto the nether roof",
                Category.PATCHES
        );
        teleport = config.getBoolean(getName() + ".TeleportPlayer");
        killPlayer = config.getBoolean(getName() + ".KillPlayer");
        message = config.getString(getName() + ".Message");
        sendMessage = !message.isEmpty();
        topLayer = config.getInt(getName() + ".NetherTop");
        bottomLayer = config.getInt(getName() + ".NetherBottom");
        preventInteract = config.getBoolean(getName() + ".PreventInteractions");
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getEnvironment().equals(Environment.NETHER)) return;
        double height = player.getLocation().getY();
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        if (teleport) {
            if (height >= topLayer) {
                PlayerUtil.teleportPlayer(player, x, 125, z);
                Utils.log("&3Removed&r&a " + player.getName() + " &r&3from the nether roof");
                if (sendMessage) PlayerUtil.sendMessage(player, message);
            } else if (height <= bottomLayer) {
                PlayerUtil.teleportPlayer(player, x, 6, z);
                Utils.log("&3Removed&r&a " + player.getName() + " &r&3from the nether roof");
                if (sendMessage) PlayerUtil.sendMessage(player, message);
            }
        } else if (killPlayer) player.setHealth(0);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!preventInteract) return;
        if (!block.getWorld().getEnvironment().equals(Environment.NETHER)) return;
        if (!(block.getY() >= topLayer)) return;
        event.setCancelled(true);
        if (sendMessage) PlayerUtil.sendMessage(event.getPlayer(), message);
    }
}
