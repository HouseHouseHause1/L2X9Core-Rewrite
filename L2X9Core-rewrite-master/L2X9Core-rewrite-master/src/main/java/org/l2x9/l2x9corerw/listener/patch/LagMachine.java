package org.l2x9.l2x9corerw.listener.patch;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LagMachine extends BaseListener {

    private final double disableTPS;
    private final boolean removeWire;
    private final int maxUpdates;
    private final boolean strikeLightning;
    private final boolean logToFile;
    private final ConcurrentHashMap<Object, Integer> chunkMap;
    private File log;
    private final List<Material> ignored = Arrays.asList(
            Material.WOOD_PLATE,
            Material.GOLD_PLATE,
            Material.IRON_PLATE,
            Material.STONE_PLATE,
            Material.TRAPPED_CHEST);
    private final ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE);

    public LagMachine() {
        super("AntiLagMachine",
                "Prevent most LagMachines",
                Category.PATCHES);
        chunkMap = new ConcurrentHashMap<>();
        plugin.getViolationMaps().add(chunkMap);
        disableTPS = config.getDouble(getName() + ".DisableTPS");
        removeWire = config.getBoolean(getName() + ".BreakMachine");
        maxUpdates = config.getInt(getName() + ".MaxRedstoneUpdates");
        strikeLightning = config.getBoolean(getName() + ".StrikeLightning");
        logToFile = config.getBoolean(getName() + ".LogToFile");
        if (logToFile) {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-d-uuuu");
            String formattedDate = localDate.format(formatter);
            File logFolder = new File(plugin.getDataFolder() + "/LagMachineLogs");
            if (!logFolder.exists()) logFolder.mkdir();
            log = new File(logFolder, formattedDate + ".log");
            if (!log.exists()) {
                try {
                    log.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        double tps = Utils.getTPS();
        if (tps > disableTPS) return;
        if (ignored.contains(block.getType())) return;
        Chunk chunk = block.getChunk();
        Utils.incrementVLS(chunk, chunkMap);
        if (chunkMap.get(chunk) > maxUpdates) {
            event.setNewCurrent(0);
            if (removeWire) block.breakNaturally(tool);
            if (strikeLightning) block.getWorld().strikeLightning(block.getLocation());
            if (logToFile) logFile("Possible LagMachine in " + Utils.formatLocation(block.getLocation()));
            Utils.log("&3Potential lag machine in " + Utils.formatLocation(block.getLocation()));
        }
    }

    private void logFile(String input) {
        try {
            input = ChatColor.translateAlternateColorCodes('&', input);
            input = ChatColor.stripColor(input);
            FileInputStream inputStream = new FileInputStream(log);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(line -> builder.append(line).append("\n"));
            builder.append(input).append("\n");
            FileWriter writer = new FileWriter(log);
            writer.write(builder.toString());
            inputStream.close();
            reader.close();
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
