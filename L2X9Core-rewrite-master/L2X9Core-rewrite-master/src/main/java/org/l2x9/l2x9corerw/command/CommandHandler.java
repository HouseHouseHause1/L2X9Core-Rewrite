package org.l2x9.l2x9corerw.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.l2x9.l2x9corerw.L2X9CoreRWBase;
import org.l2x9.l2x9corerw.command.commands.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandHandler extends L2X9CoreRWBase implements TabExecutor {
    private final ArrayList<BaseCommand> commands = new ArrayList<>();

    public void registerCommands() throws NotInPluginYMLException {
        try {
            addCommand(new org.l2x9.l2x9corerw.command.commands.BaseCommand());
            addCommand(new CrashCommand());
            addCommand(new DiscordCommand());
            addCommand(new HelpCommand());
            addCommand(new OpenInv());
            addCommand(new SayCommand());
            addCommand(new SpawnCommand());
            addCommand(new SpeedCommand());
            addCommand(new UptimeCommand());
            addCommand(new UUidCommand());
            addCommand(new WorldSwitcher());
        } catch (Exception e) {
            throw new NotInPluginYMLException("Command not in plugin.yml");
        }
    }

    private void addCommand(BaseCommand command) {
        commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (BaseCommand command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                if (sender.hasPermission(command.getPermission())) {
                    command.execute(sender, args);
                } else {
                    command.sendNoPermission(sender);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        for (BaseCommand command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                if (command instanceof BaseTabCommand) {
                    BaseTabCommand tabCommand = (BaseTabCommand) command;
                    return tabCommand.onTab(args);
                } else {
                    List<String> names = new ArrayList<>();
                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    String lastArg = args[args.length - 1];
                    if (lastArg.length() == 0) {
                        players.forEach(player -> names.add(player.getName()));
                    } else {
                        players.forEach(player -> {
                            if (player.getName().startsWith(lastArg)) {
                                names.add(player.getName());
                            }
                        });
                    }
                    return names;
                }
            }
        }
        return Collections.singletonList("Not a tab command");
    }

    public ArrayList<BaseCommand> getCommands() {
        return commands;
    }
}