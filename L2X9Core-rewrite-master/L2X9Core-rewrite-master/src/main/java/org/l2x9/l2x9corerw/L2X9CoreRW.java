package org.l2x9.l2x9corerw;

import org.l2x9.l2x9corerw.command.CommandHandler;
import org.l2x9.l2x9corerw.listener.ListenerHandler;
import org.l2x9.l2x9corerw.util.PluginUtil;
import org.l2x9.l2x9corerw.util.Utils;
import org.l2x9.l2x9corerw.worker.WorkerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class L2X9CoreRW extends JavaPlugin {
    private final ArrayList<ConcurrentHashMap<Object, Integer>> violationMaps = new ArrayList<>();
    private final Logger log = Bukkit.getLogger();
    private final PluginManager pluginManager = getServer().getPluginManager();
    private final Messenger messenger = getServer().getMessenger();
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private WorkerHandler workerHandler;
    private HashMap<EntityType, Integer> entityPairMap;
    private List<Material> illegalItems;
    public static long startTime;

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            L2X9CoreRWBase.setPlugin(this);
            commandHandler = new CommandHandler();
            listenerHandler = new ListenerHandler();
            workerHandler = new WorkerHandler();
            Utils.updateUtilVars();
            init();
        } catch (Exception e) {
            e.printStackTrace();
            pluginManager.disablePlugin(this, true);
        }
    }

    public Logger getLog() {
        return log;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public HashMap<EntityType, Integer> getEntityPairMap() {
        return entityPairMap;
    }

    public List<Material> getIllegalItems() {
        return illegalItems;
    }

    public void init() {
        try {
            messenger.registerOutgoingPluginChannel(this, "BungeeCord");
            startTime = System.currentTimeMillis();
            Utils.log("&7Registering commands...");
            commandHandler.registerCommands();
            Utils.log("&7Registering listeners...");
            listenerHandler.registerListeners();
            Utils.log("&7Setting up AntiIllegal...");
            illegalItems = PluginUtil.setupAntiIllegal();
            Utils.log("&7Setting up entity per chunk limit...");
            entityPairMap = PluginUtil.setupEntityLimit();
            Utils.log("&7Starting workers...");
            workerHandler.startWorkers();
            Utils.log("&7Finished enabling!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            reloadConfig();
            getViolationMaps().clear();
            entityPairMap.clear();
            commandHandler.getCommands().clear();
            workerHandler.stopAllWorkers();
            workerHandler.getWorkers().clear();
            listenerHandler.getListeners().clear();

            HandlerList.unregisterAll(this);

            commandHandler.registerCommands();
            workerHandler.startWorkers();
            listenerHandler.registerListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ConcurrentHashMap<Object, Integer>> getViolationMaps() {
        return violationMaps;
    }
}
