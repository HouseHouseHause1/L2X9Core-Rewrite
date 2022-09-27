package org.l2x9.l2x9corerw.listener.antiillegal;

import org.l2x9.l2x9corerw.listener.BaseListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.l2x9.l2x9corerw.util.ItemUtil;

public class PlayerScroll extends BaseListener {
    public PlayerScroll() {
        super(
                "AntiIllegalPlayerScroll",
                "Removes illegal items when a player moves hotbar slots",
                Category.ANTIILLEGAL
        );
    }

    @EventHandler
    public void onItemMove(PlayerItemHeldEvent event) {
        if (plugin.getConfig().getBoolean("AntiIllegal.PlayerHotbarMove-Enabled")) {
            Player player = event.getPlayer();
            ItemUtil.deleteIllegals(player.getInventory());
        }
    }
}