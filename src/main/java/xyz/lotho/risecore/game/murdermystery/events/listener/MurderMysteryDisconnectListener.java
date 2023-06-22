package xyz.lotho.risecore.game.murdermystery.events.listener;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.game.murdermystery.player.MurderMysteryPlayer;
import xyz.lotho.risecore.game.util.Game;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
public class MurderMysteryDisconnectListener implements Listener {

    private final RiseCore riseCore;

    public MurderMysteryDisconnectListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onLeaveGame(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = getRiseCore().getGameManager().findGameByPlayer(player);

        if (game == null || game.getGameType() != GameType.DUELS) return;

        MurderMysteryGame murderMysteryGame = (MurderMysteryGame) game;

        MurderMysteryPlayer gamePlayer = murderMysteryGame.getPlayerManager().getPlayer(player.getUniqueId());
        murderMysteryGame.getPlayerManager().removePlayer((gamePlayer.getUuid()));

        game.announce("&2&lDISCONNECT &8> " + player.getName() + CC.RED + " has quit the game!");
    }

}
