package xyz.lotho.risecore.game.murdermystery.events.listener;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import xyz.lotho.risecore.game.util.Game;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.network.RiseCore;

@Getter
public class MurderMysteryInventoryMoveListener implements Listener {

    private final RiseCore riseCore;

    public MurderMysteryInventoryMoveListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        System.out.println("InventoryMoveEvent");
        System.out.println(event.getInventory().getType());

        Game game = getRiseCore().getGameManager().findGameByPlayer(player);
        if (game == null || game.getGameType() != GameType.MURDER_MYSTERY) return;

        System.out.println("got past game check");

        if (event.getInventory().getType() == InventoryType.CRAFTING) {
            event.setCancelled(true);
        }
    }
}
