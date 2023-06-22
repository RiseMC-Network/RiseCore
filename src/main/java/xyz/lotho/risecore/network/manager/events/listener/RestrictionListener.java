package xyz.lotho.risecore.network.manager.events.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

public class RestrictionListener implements Listener {

    private final RiseCore riseCore;

    public RestrictionListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onCommandRestriction(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (player.isOp() && (event.getMessage().contains("/op") || event.getMessage().contains("/deop"))) {
            player.sendMessage(CC.RED + "Command can only be used via console.");
            event.setCancelled(true);
        }

        // TODO: handle other restricted commands
    }

}
