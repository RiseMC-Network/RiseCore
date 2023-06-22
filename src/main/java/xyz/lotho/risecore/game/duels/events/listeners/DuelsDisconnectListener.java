package xyz.lotho.risecore.game.duels.events.listeners;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lotho.risecore.game.Game;
import xyz.lotho.risecore.game.GameType;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.duels.player.DuelsPlayer;
import xyz.lotho.risecore.game.duels.team.DuelsTeam;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
public class DuelsDisconnectListener implements Listener {

    private final RiseCore riseCore;

    public DuelsDisconnectListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onLeaveGame(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = getRiseCore().getGameManager().findGameByPlayer(player);

        if (game == null || game.getGameType() != GameType.DUELS) return;

        DuelGame duelGame = (DuelGame) game;

        DuelsPlayer gamePlayer = duelGame.getGamePlayerManager().getPlayer(player.getUniqueId());
        DuelsTeam playerDuelsTeam = gamePlayer.getDuelsTeam();

        playerDuelsTeam.getTeamPlayers().remove(gamePlayer);
        duelGame.getGamePlayerManager().getGamePlayers().remove(gamePlayer.getUuid());

        game.announce(playerDuelsTeam.getTeam().getColor() + player.getName() + CC.RED + " has quit the game!");
    }

}
