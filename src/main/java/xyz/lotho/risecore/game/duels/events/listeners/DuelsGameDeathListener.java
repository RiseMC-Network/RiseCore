package xyz.lotho.risecore.game.duels.events.listeners;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.lotho.risecore.game.Game;
import xyz.lotho.risecore.game.GameType;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.duels.player.DuelsPlayer;
import xyz.lotho.risecore.game.duels.team.DuelsTeams;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
public class DuelsGameDeathListener implements Listener {

    private final RiseCore riseCore;

    public DuelsGameDeathListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player killer)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        Game game = getRiseCore().getGameManager().findGameByPlayer(killer);
        if (game == null || game.getGameType() != GameType.DUELS) return;

        DuelGame duelGame = (DuelGame) game;

        DuelsPlayer killerGamePlayer = duelGame.getGamePlayerManager().getPlayer(killer.getUniqueId());
        DuelsPlayer victimGamePlayer  = duelGame.getGamePlayerManager().getPlayer(victim.getUniqueId());

        if (victim.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            victimGamePlayer.kill();

            killerGamePlayer.addKill();

            DuelsTeams killerTeam = killerGamePlayer.getDuelsTeam().getTeam();
            DuelsTeams victimTeam = victimGamePlayer.getDuelsTeam().getTeam();

            game.announce(CC.translate(victimTeam.getColor() + victim.getName() + " &7has been killed by " + killerTeam.getColor() + killer.getName() + "&7!"));
        }
    }

}
