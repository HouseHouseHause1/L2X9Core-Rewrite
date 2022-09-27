package org.l2x9.l2x9corerw.listener.antilag;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ElytraFly extends BaseListener {
    private final double maxSpeed;
    private final double disableTPS;
    private final boolean removeElytra;
    private final boolean preventPacket;
    private final boolean sendSpeedMessage;
    private final boolean sendLowTPSMessage;
    private final String speedMessage;
    private final String lowTPSMessage;

    public ElytraFly() {
        super("ElytraFlyFix",
                "Prevent players from going 99% light speed with their elytra",
                Category.ANTILAG
        );
        maxSpeed = config.getDouble(getName() + ".MaxSpeed");
        removeElytra = config.getBoolean(getName() + ".RemoveElytra");
        preventPacket = config.getBoolean(getName() + ".PreventPacketElytra");
        disableTPS = config.getDouble(getName() + ".ElytraDisableTps");
        speedMessage = config.getString(getName() + ".SpeedMessage");
        sendSpeedMessage = !speedMessage.isEmpty();
        lowTPSMessage = config.getString(getName() + ".LowTPSMessage");
        sendLowTPSMessage = !lowTPSMessage.isEmpty();
    }

    @EventHandler
    public void onGlide(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.isGliding()) return;
        double speed = calcSpeed(event);
        if (speed > maxSpeed) {
            event.setTo(event.getFrom());
            player.setGliding(false);
            if (sendSpeedMessage) PlayerUtil.sendMessage(player, speedMessage);
            if (removeElytra) removeElytra(player);
            Utils.log("&3Prevented&r&a " + player.getName() + " &r&3from going too fast with an elytra");
        }
        if (disableTPS <= 0) return;
        double tps = Utils.getTPS();
        if (tps < disableTPS) {
            event.setCancelled(true);
            player.setGliding(false);
            if (sendLowTPSMessage) PlayerUtil.sendMessage(player, lowTPSMessage);
            if (removeElytra) removeElytra(player);
        }
    }


    private double calcSpeed(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        double distX = to.getX() - from.getX();
        double distZ = to.getZ() - from.getZ();
        return Math.hypot(distX, distZ);
    }

    private void removeElytra(Player player) {
        ItemStack chestPlate = player.getInventory().getChestplate();
        if (chestPlate != null && chestPlate.getType() == Material.ELYTRA) {
            PlayerInventory inventory = player.getInventory();
            if (inventory.firstEmpty() == -1) {
                chestPlate.setType(Material.AIR);
                player.getWorld().dropItemNaturally(player.getLocation(), chestPlate);
            } else {
                chestPlate.setType(Material.AIR);
                inventory.setItem(inventory.firstEmpty(), chestPlate);
            }
        }
    }
}
