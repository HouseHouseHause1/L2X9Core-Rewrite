package org.l2x9.l2x9corerw.command.commands;

import org.l2x9.l2x9corerw.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends BaseCommand {

    public SpeedCommand() {
        super(
                "speed",
                "/speed <number>",
                "odysseus.command.speed",
                "Turn up your fly speed");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender);
        if (player != null) {
            try {
                if (args.length > 0) {
                    float speed = Float.parseFloat(args[0]);
                    if (!(speed > 1)) {
                        player.setFlySpeed(speed);
                        sendMessage(player, "&6Fly speed set to&r&c " + speed);
                    } else {
                        sendErrorMessage(player, "Flying speed must not be above 1");
                    }
                } else {
                    sendMessage(sender, "&6Please note that the default flight speed is&r&c 0.1");
                    sendErrorMessage(sender, getUsage());

                }
            } catch (NumberFormatException e) {
                sendErrorMessage(player, getUsage());
            }
        } else {
            sendErrorMessage(sender, PLAYER_ONLY);
        }
    }
}
