package xyz.lotho.risecore.game.murdermystery.events.listener;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.lotho.risecore.game.util.Game;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.network.RiseCore;

@Getter
public class MurderMysteryDamageEventListener implements Listener {

    private final RiseCore riseCore;

    public MurderMysteryDamageEventListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Game game = getRiseCore().getGameManager().findGameByPlayer(player);
        if (game == null || game.getGameType() != GameType.MURDER_MYSTERY) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

}
