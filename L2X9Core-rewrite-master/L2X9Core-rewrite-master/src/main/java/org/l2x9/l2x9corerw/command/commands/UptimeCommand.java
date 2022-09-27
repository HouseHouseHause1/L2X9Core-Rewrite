package org.l2x9.l2x9corerw.command.commands;


import org.l2x9.l2x9corerw.L2X9CoreRW;
import org.l2x9.l2x9corerw.command.BaseCommand;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.command.CommandSender;

public class UptimeCommand extends BaseCommand {

    public UptimeCommand() {
        super(
                "uptime",
                "/uptime",
                "odysseus.command.uptime",
                "Show the uptime of the server");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendMessage(sender, "&6The server has had &r&c" + Utils.getFormattedInterval(System.currentTimeMillis() - L2X9CoreRW.startTime) + "&r&6 uptime");
    }
}
