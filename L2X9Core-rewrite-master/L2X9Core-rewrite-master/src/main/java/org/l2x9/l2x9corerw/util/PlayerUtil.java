package org.l2x9.l2x9corerw.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class PlayerUtil extends Utils {

    public static void crashPlayer(Player player) {
        for (int i = 0; i < 100; i++) {
            player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), Integer.MAX_VALUE, 1, 1, 1);
        }
    }

    public static void sendMessage(Object player, String message) {
        if (player instanceof Player) {
            Player casted = (Player) player;
            casted.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + message));
        } else {
            if (player instanceof CommandSender) {
                CommandSender casted = (CommandSender) player;
                casted.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + message));
            }
        }
    }

    public static void kickPlayer(Player player, String string) {
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', PREFIX + string));
    }

    public static void teleportPlayer(Player player, int x, int y, int z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    public static void teleportPlayer(Player player, double x, double y, double z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    public static void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArray);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(plugin, "BungeeCord", byteArray.toByteArray());
            byteArray.close();
            out.close();
        } catch (Exception | Error e) {
            player.sendMessage(ChatColor.RED + "Error when trying to connect to " + server);
        }
    }
}
