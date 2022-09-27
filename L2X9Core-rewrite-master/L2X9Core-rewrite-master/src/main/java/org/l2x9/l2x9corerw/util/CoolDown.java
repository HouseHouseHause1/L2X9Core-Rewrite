package org.l2x9.l2x9corerw.util;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class CoolDown {

    private final HashMap<Player, Double> cooldowns = new HashMap<>();

    public void setCooldown(Player player, double ms) {
        double delay = System.currentTimeMillis() + ms;
        cooldowns.put(player, delay);
    }

    public double getCooldown(Player player) {
        return cooldowns.get(player) - System.currentTimeMillis();
    }

    public boolean checkCooldown(Player player) {
        return !cooldowns.containsKey(player) || cooldowns.get(player) <= System.currentTimeMillis();
    }
}