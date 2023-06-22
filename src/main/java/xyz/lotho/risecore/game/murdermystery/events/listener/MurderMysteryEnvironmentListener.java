package xyz.lotho.risecore.game.murdermystery.events.listener;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lotho.risecore.game.Game;
import xyz.lotho.risecore.game.GameType;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.network.RiseCore;

@Getter
public class MurderMysteryEnvironmentListener implements Listener {

    private final RiseCore riseCore;

    public MurderMysteryEnvironmentListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Game game = getRiseCore().getGameManager().findGameByPlayer(player);

        if (game != null && game.getGameType() == GameType.MURDER_MYSTERY) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Game game = getRiseCore().getGameManager().findGameByPlayer(player);

        if (game != null && game.getGameType() == GameType.MURDER_MYSTERY) event.setCancelled(true);
    }

}
