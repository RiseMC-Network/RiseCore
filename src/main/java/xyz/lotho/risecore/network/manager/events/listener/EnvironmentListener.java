package xyz.lotho.risecore.network.manager.events.listener;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lotho.risecore.game.Game;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.server.ServerType;

@Getter
public class EnvironmentListener implements Listener {

    private final RiseCore riseCore;

    public EnvironmentListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (getRiseCore().getServerType() == ServerType.GAME) {
            Game game = getRiseCore().getGameManager().findGameByPlayer(player);
            if (game == null) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (getRiseCore().getServerType() == ServerType.GAME) {
            Game game = getRiseCore().getGameManager().findGameByPlayer(player);
            if (game == null) event.setCancelled(true);
        }
    }

}
