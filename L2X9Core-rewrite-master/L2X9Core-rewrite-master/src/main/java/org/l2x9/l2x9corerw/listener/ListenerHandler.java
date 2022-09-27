package org.l2x9.l2x9corerw.listener;

import org.bukkit.plugin.PluginManager;
import org.l2x9.l2x9corerw.L2X9CoreRWBase;
import org.l2x9.l2x9corerw.listener.antiillegal.*;
import org.l2x9.l2x9corerw.listener.antilag.ElytraFly;
import org.l2x9.l2x9corerw.listener.antilag.LiquidFlow;
import org.l2x9.l2x9corerw.listener.antilag.WitherSkullFix;
import org.l2x9.l2x9corerw.listener.misc.AntiSpam;
import org.l2x9.l2x9corerw.listener.patch.*;

import java.util.ArrayList;

public class ListenerHandler extends L2X9CoreRWBase {
    private final ArrayList<BaseListener> listeners = new ArrayList<>();
    private final PluginManager pluginManager = plugin.getServer().getPluginManager();

    public void registerListeners() {
        //Patches --
        addListener(new BookBan());
        addListener(new ChestLagFix());
        addListener(new DispenserCrash());
        addListener(new EndGatewayCrash());
        addListener(new EndPortalGrief());
        addListener(new SpunkBan());
        addListener(new AntiNetherRoof());
        addListener(new OffhandCrash());
        addListener(new LagMachine());
        addListener(new LightLag());
        //--
        //AntiIllegal --
        addListener(new BlockPlace());
        addListener(new ChunkLoad());
        addListener(new HopperTransfer());
        addListener(new InventoryClose());
        addListener(new InventoryOpen());
        addListener(new ItemPickup());
        addListener(new PlayerScroll());
        //--
        //AntiLag --
        addListener(new WitherSkullFix());
        addListener(new ElytraFly());
        addListener(new LiquidFlow());
        //--
        //Misc --
        addListener(new AntiSpam());
        //--
    }

    private void addListener(BaseListener listener) {
        if (listener.isEnabled() || listener.getCategory() == BaseListener.Category.ANTIILLEGAL) {
            listeners.add(listener);
            pluginManager.registerEvents(listener, plugin);
        }
    }

    public ArrayList<BaseListener> getListeners() {
        return listeners;
    }
}