package org.l2x9.l2x9corerw.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.l2x9.l2x9corerw.command.BaseTabCommand;
import org.l2x9.l2x9corerw.util.PlayerUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorldSwitcher extends BaseTabCommand {
    private final String worldName;

    public WorldSwitcher() {
        super(
                "world",
                "/world <worldName>",
                "odysseus.command.world",
                "Switch worlds",
                new String[]{
                        "overworld::Teleport your self to the overworld",
                        "nether::Teleport your self to the nether",
                        "end::Teleport your self to the end"
                }
        );
        worldName = config.getString("Odysseus.WorldName");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender);
        if (player != null) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "overworld":
                        int x = player.getLocation().getBlockX();
                        int y = player.getLocation().getBlockY();
                        int z = player.getLocation().getBlockZ();
                        World overWorld = Bukkit.getWorld(worldName);
                        player.teleport(new Location(overWorld, x, y, z));
                        PlayerUtil.sendMessage(player, "&6Teleporting to &r&c" + args[0]);
                        break;
                    case "nether":
                        int netherX = player.getLocation().getBlockX();
                        int netherY = player.getLocation().getBlockY();
                        int netherZ = player.getLocation().getBlockZ();
                        World netherWorld = Bukkit.getWorld(worldName.concat("_nether"));
                        if (netherY < 128) {
                            player.teleport(new Location(netherWorld, netherX, 125, netherZ));
                        } else {
                            player.teleport(new Location(netherWorld, netherX, netherY, netherZ));

                        }
                        PlayerUtil.sendMessage(player, "&6Teleporting to &r&c" + args[0]);
                        break;
                    case "end":
                        int endX = player.getLocation().getBlockX();
                        int endY = player.getLocation().getBlockY();
                        int endZ = player.getLocation().getBlockZ();
                        World endWorld = Bukkit.getWorld(worldName.concat("_the_end"));
                        player.teleport(new Location(endWorld, endX, endY, endZ));
                        PlayerUtil.sendMessage(player, "&6Teleporting to &r&c" + args[0]);
                        break;
                    default:
                        PlayerUtil.sendMessage(sender, "&4Error:&r&c Unknown world");
                        break;
                }
            } else {
                sendErrorMessage(sender, "Please include one argument /world <end | overworld | nether>");
            }
        } else {
            sendErrorMessage(sender, PLAYER_ONLY);
        }
    }

    @Override
    public List<String> onTab(String[] args) {
        List<String> worlds = Arrays.asList("overworld", "end", "nether");
        if (args.length > 0) {
            String lastArg = args[args.length - 1];
            for (String world : worlds) {
                if (world.startsWith(lastArg)) {
                    return Collections.singletonList(world);
                }
            }
        } else {
            return worlds;
        }
        return null;
    }
}
