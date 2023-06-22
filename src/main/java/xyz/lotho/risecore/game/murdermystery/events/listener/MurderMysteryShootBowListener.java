package xyz.lotho.risecore.game.murdermystery.events.listener;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import xyz.lotho.risecore.game.util.Game;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.ItemBuilder;
import xyz.lotho.risecore.network.util.Tasks;

@Getter
public class MurderMysteryShootBowListener implements Listener {

    private final RiseCore riseCore;

    public MurderMysteryShootBowListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player shooter)) return;

        Game game = getRiseCore().getGameManager().findGameByPlayer(shooter);
        if (game == null || game.getGameType() != GameType.MURDER_MYSTERY) return;

        shooter.getInventory().remove(Material.ARROW);
        Tasks.runLater(() -> shooter.getInventory().setItem(17, new ItemBuilder(Material.ARROW).amount(1).build()), 200L);
    }

}
