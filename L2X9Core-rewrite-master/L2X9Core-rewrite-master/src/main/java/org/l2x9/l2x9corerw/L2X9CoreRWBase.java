package org.l2x9.l2x9corerw;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class L2X9CoreRWBase {
    protected static L2X9CoreRW plugin;
    protected static FileConfiguration config;

    protected static void setPlugin(L2X9CoreRW plugin) {
        L2X9CoreRWBase.plugin = plugin;
        config = plugin.getConfig();
    }
}
