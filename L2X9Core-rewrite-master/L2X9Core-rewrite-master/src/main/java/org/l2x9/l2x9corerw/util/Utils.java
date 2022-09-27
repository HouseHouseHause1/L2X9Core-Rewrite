package org.l2x9.l2x9corerw.util;

import org.l2x9.l2x9corerw.L2X9CoreRWBase;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class Utils extends L2X9CoreRWBase {
    public static String PREFIX;
    private static boolean broadcastOp;
    private static boolean logging;

    public static void log(String message) {
        message = ChatColor.translateAlternateColorCodes('&', PREFIX + message);
        if (broadcastOp) {
            sendOpMessage(message);
        }
        if (!logging) {
            plugin.getLog().log(Level.INFO, message);
        }
    }
    public static double getTPS() {
        return plugin.getServer().getTPS()[0];
    }

    public static int countBlockPerChunk(Chunk chunk, Material lookingFor) {
        int count = 0;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    if (chunk.getBlock(x, y, z).getType() == lookingFor)
                        count++;
                }
            }
        }
        return count;
    }

    public static void sendOpMessage(String message) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.isOp()) {
                online.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public static String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    public static String formatLocation(Location location) {
        DecimalFormat format = new DecimalFormat("#.##");
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        return "&3world&r&a " + world.getName()
                + " &r&3X:&r&a " + format.format(x)
                + " &r&3Y:&r&a " + format.format(y)
                + " &r&3Z:&r&a " + format.format(z);
    }

    public static void updateUtilVars() {
        PREFIX = plugin.getConfig().getString("Odysseus.Prefix");
        broadcastOp = plugin.getConfig().getBoolean("Odysseus.BroadcastOp");
        logging = plugin.getConfig().getBoolean("Odysseus.NoConsoleOutput");
    }

    public static void incrementVLS(Object key, ConcurrentHashMap<Object, Integer> vlMap) {
        if (!vlMap.containsKey(key)) {
            vlMap.put(key, 0);
        } else {
            vlMap.replace(key, vlMap.get(key) + 1);
        }
    }
}
