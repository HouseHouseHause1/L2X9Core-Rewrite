package org.l2x9.l2x9corerw.command.commands;

import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.l2x9.l2x9corerw.command.BaseCommand;

public class SayCommand extends BaseCommand {
    private final String format;

    public SayCommand() {
        super(
                "say",
                "/say <message>",
                "odysseus.command.say",
                "Configurable say command");
        format = config.getString("Command.SayCommand.Format");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String arg : args) {
                builder.append(arg.concat(" "));
            }
            broadcast(format.replace("{prefix}", Utils.PREFIX).replace("{message}", builder.substring(0, builder.length() -1)));
        } else {
            sendErrorMessage(sender, "Message cannot be blank");
        }
    }
    private void broadcast(String message) {
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}