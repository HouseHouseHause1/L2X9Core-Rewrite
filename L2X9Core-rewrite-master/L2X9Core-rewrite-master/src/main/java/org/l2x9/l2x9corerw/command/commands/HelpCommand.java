package org.l2x9.l2x9corerw.command.commands;

import org.l2x9.l2x9corerw.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.l2x9.l2x9corerw.util.PlayerUtil;

import java.util.List;

public class HelpCommand extends BaseCommand {
    List<String> help;
    public HelpCommand() {
        super(
                "help",
                "/help",
                "odysseus.command.help",
                "Displays a custom help menu");
        help = config.getStringList("Command.HelpCommand.HelpList");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String join = String.join("\n", help);
        PlayerUtil.sendMessage(sender, join);
    }
}
