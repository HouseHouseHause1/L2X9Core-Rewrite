package org.l2x9.l2x9corerw.command.commands;


import org.l2x9.l2x9corerw.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenInv extends BaseCommand {
    public OpenInv() {
        super(
                "open",
                "/open <inv | ender> <player>",
                "odysseus.command.openinv",
                "Open peoples inventories",
                new String[]{
                        "inventory::Open the inventory of the specified player",
                        "ender::Open the ender chest of the specified player"
                }
                );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender);
        if (player != null) {
            if (args.length < 2) {
                sendErrorMessage(sender, getUsage());
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (target.isOnline()) {
                    switch (args[0]) {
                        case "ender":
                            player.openInventory(target.getEnderChest());
                            break;
                        case "inv":
                        case "inventory":
                            player.openInventory(target.getInventory());
                            break;
                        default:
                            sendErrorMessage(sender, "Unknown argument " + args[0]);
                    }
                } else {
                    sendErrorMessage(sender, "Player " + args[0] + " not online");
                }
            }
        } else {
            sendErrorMessage(sender, PLAYER_ONLY);
        }
    }
}
