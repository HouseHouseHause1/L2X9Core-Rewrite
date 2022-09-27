package org.l2x9.l2x9corerw.listener.patch;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.PlayerUtil;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class EndGatewayCrash extends BaseListener {
    private final boolean crash;
    private final boolean kick;
    private final String kickMessage;

    public EndGatewayCrash() {
        super(
                "AntiEndGatewayCrash",
                "Prevent players from riding an entity through a gateway far out in the end to crash the server",
                Category.PATCHES
        );
        crash = config.getBoolean(getName() + ".Crash");
        kick = config.getBoolean(getName() + ".Kick");
        kickMessage = config.getString(getName() + ".KickMessage");
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (!(vehicle.getLocation().getWorld().getEnvironment() == Environment.THE_END)) return;
        if (isNearGateway(vehicle) && vehicle.getPassengers().isEmpty()) {
            vehicle.remove();
            Utils.log("&3Prevented an entity in " + Utils.formatLocation(vehicle.getLocation()) + "&r&3 from going through a gateway");
        } else if (isNearGateway(vehicle)) {
            vehicle.getPassengers().forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    vehicle.remove();
                    Utils.log("&3Prevented an entity in " + Utils.formatLocation(vehicle.getLocation()) + "&r&3 from going through a gateway. Attempt by&r&a " + player.getName());
                    if (kick) {
                        PlayerUtil.kickPlayer(player, kickMessage);
                    } else if (crash) {
                        PlayerUtil.crashPlayer(player);
                    }
                }
            });
        }
    }

    private boolean isNearGateway(Vehicle vehicle) {
        Location location = vehicle.getLocation();
        for (BlockFace face : BlockFace.values()) {
            if (nearGateWay(face, 1, location) ||
                    nearGateWay(face, 2, location) ||
                    nearGateWay(face, 3, location)) {
                return true;
            }
        }
        return false;
    }

    private boolean nearGateWay(BlockFace face, int dist, Location location) {
        return location.getWorld().getBlockAt(location).getRelative(face, dist).getType() == Material.END_GATEWAY;
    }
}
