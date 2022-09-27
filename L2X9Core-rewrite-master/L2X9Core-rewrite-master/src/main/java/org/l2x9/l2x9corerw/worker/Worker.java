package org.l2x9.l2x9corerw.worker;

import org.l2x9.l2x9corerw.L2X9CoreRWBase;

public abstract class Worker extends L2X9CoreRWBase {
    private final int ms;
    private String name;
    private boolean enabled = true;

    public Worker(int ms, String name) {
        this.ms = ms;
        this.name = name;
        enabled = config.getBoolean(name + ".Worker-Enabled");
    }
    public Worker(int ms) {
        this.ms = ms;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getTime() {
        return ms;
    }

    public abstract void execute();
}
