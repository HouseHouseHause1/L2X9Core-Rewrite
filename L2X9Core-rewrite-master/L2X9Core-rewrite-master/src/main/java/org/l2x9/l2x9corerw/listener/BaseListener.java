package org.l2x9.l2x9corerw.listener;

import org.bukkit.event.Listener;
import org.l2x9.l2x9corerw.L2X9CoreRWBase;

public abstract class BaseListener extends L2X9CoreRWBase implements Listener {
    private final String name;
    private final String description;
    private final Category category;
    private final boolean enabled;

    public BaseListener(String name, String description, Category category) {
        this.category = category;
        this.description = description;
        this.name = name;
        enabled = config.getBoolean(getName() + ".Module-Enabled");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public enum Category {
        ANTIILLEGAL,
        ANTILAG,
        PATCHES,
        MISC,
        PROTOCOLLIB
    }
}
