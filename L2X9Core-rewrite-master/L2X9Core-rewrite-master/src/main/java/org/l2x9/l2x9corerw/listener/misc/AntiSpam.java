package org.l2x9.l2x9corerw.listener.misc;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.l2x9.l2x9corerw.util.CoolDown;
import org.l2x9.l2x9corerw.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiSpam extends BaseListener {

    private final boolean allowLinks;
    private final boolean sendFakeMessage;
    private final double ms;
    private final List<String> blackListedWords;
    private final boolean checkCommands;
    private final CoolDown chatCooldown;
    private final CoolDown commandCooldown;

    public AntiSpam() {
        super(
                "AntiChatSpam",
                "Prevent players from spamming chat",
                Category.MISC
        );
        allowLinks = config.getBoolean(getName() + ".AllowLinks");
        sendFakeMessage = config.getBoolean(getName() + ".SendFakeMessage");
        ms = config.getDouble(getName() + ".ChatCooldown");
        blackListedWords = config.getStringList(getName() + ".BlacklistedWords");
        checkCommands = config.getBoolean(getName() + ".CheckCommands");
        chatCooldown = new CoolDown();
        commandCooldown = new CoolDown();

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        doCheck(event);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!checkCommands) return;
        Player player = event.getPlayer();
        if (commandCooldown.checkCooldown(player)) {
            event.setCancelled(true);
        } else {
            commandCooldown.setCooldown(player, ms);
        }

    }

    private void doCheck(Object event) {
        if (event instanceof AsyncPlayerChatEvent) {
            AsyncPlayerChatEvent chatEvent = (AsyncPlayerChatEvent) event;
            Player player = chatEvent.getPlayer();
            String[] split = chatEvent.getMessage().split(" ");
            if (player.isOp()) return;
            for (String word : split) {
                if (!allowLinks && isUrl(word)) {
                    chatEvent.setCancelled(true);
                    Utils.log("Prevented&r&a " + player.getName() + "&r&3 from sending a url&r&a " + word);
                    break;
                }
                if (blackListedWords.isEmpty()) continue;
                    if (blackListedWords.contains(word.toLowerCase())) {
                        chatEvent.setCancelled(true);
                        Utils.log("Prevented&r&a " + player.getName() + "&r&3 from sending&r&a " + word);
                        break;
                }
            }
            if (chatCooldown.checkCooldown(player)) {
                chatCooldown.setCooldown(player, ms);
            } else {
                chatEvent.setCancelled(true);
            }
            if (sendFakeMessage && chatEvent.isCancelled()) {
                player.sendMessage("<" + player.getName() + "> " + chatEvent.getMessage());
            }
        }
    }

    private boolean isUrl(String url) {
        String LINK_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(LINK_REGEX);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }
}
