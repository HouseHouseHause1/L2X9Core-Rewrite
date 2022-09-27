package org.l2x9.l2x9corerw.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.l2x9.l2x9corerw.command.BaseCommand;
import org.l2x9.l2x9corerw.util.PlayerUtil;

public class CrashCommand extends BaseCommand {

    public CrashCommand() {
        super(
                "crash",
                "/crash <player> | nearby <radius> | everyone | elytra | taco",
                "odysseus.command.crash",
                "Crash players games",
                new String[] {
                        "elytra::Crash the games of everyone who is using an elytra",
                        "everyone::Crash the game of everyone on the server",
                        "nearby::Crash everyone within a radius",
                        "taco::Crash the game of everyone with their game set to a spanish language"
                });
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(sender, getUsage());
        } else {
            switch (args[0]) {
                case "elytra":
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.isOp()) {
                            if (online.isGliding()) {
                                PlayerUtil.crashPlayer(online);
                                sendMessage(sender, "&6You have just crashed&r&c " + online.getName());

                            }
                        }
                    }
                    break;
                case "everyone":
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.isOp()) {
                            PlayerUtil.crashPlayer(online);
                            sendMessage(sender, "&6You have just crashed&r&c " + online.getName());
                        }
                    }
                    break;
                case "nearby":
                    try {
                        Player player = getSenderAsPlayer(sender);
                        if (player != null) {
                            for (Player nearby : player.getLocation().getNearbyPlayers(Integer.parseInt(args[1]))) {
                                if (!nearby.hasPermission(getPermission())) {
                                    PlayerUtil.crashPlayer(nearby);
                                    sendMessage(player, "&6You have just crashed&r&c " + nearby.getName());
                                }
                            }
                        } else {
                            sendErrorMessage(sender, PLAYER_ONLY);
                            break;
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        sendMessage(sender, "The second argument must be a number");
                    }
                    break;
                case "taco":
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getLocale().toLowerCase().contains("es")) {
                            PlayerUtil.crashPlayer(online);
                            sendMessage(sender, "&6You have just crashed&r&c " + online.getName());
                        }
                    }
                    break;
                default:
                    Player target = Bukkit.getPlayer(args[0]);
                    if (Bukkit.getOnlinePlayers().contains(target)) {
                        if (!target.hasPermission(getPermission())) {
                            PlayerUtil.crashPlayer(target);
                            PlayerUtil.sendMessage(sender, "&6You have just crashed&r&c " + target.getName());
                        } else {
                            sendErrorMessage(sender, "You cannot crash that player");
                        }
                    } else {
                        sendErrorMessage(sender, "Target not online");
                    }
                    break;
            }
        }
    }
}