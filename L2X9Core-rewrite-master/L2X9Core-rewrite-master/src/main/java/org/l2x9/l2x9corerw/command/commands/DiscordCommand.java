package org.l2x9.l2x9corerw.command.commands;

import org.l2x9.l2x9corerw.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.l2x9.l2x9corerw.util.PlayerUtil;

public class DiscordCommand extends BaseCommand {
    private final String discord;
    public DiscordCommand() {
        super(
                "discord",
                "/discord",
                "odysseus.command.discord",
                "Shows a discord link");
        discord = config.getString("Command.DiscordCommand.DiscordLink");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlayerUtil.sendMessage(sender, discord);
    }
}
