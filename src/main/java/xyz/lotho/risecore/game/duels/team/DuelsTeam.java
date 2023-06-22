package xyz.lotho.risecore.game.duels.team;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.duels.kit.util.KitType;
import xyz.lotho.risecore.game.duels.player.DuelsPlayer;

import java.util.ArrayList;

@Getter
@Setter
public class DuelsTeam {

    private final DuelsTeams team;
    private final DuelGame duelGame;

    private ArrayList<DuelsPlayer> teamPlayers = new ArrayList<>();

    private Location spawnLocation;

    public DuelsTeam(DuelsTeams team, DuelGame duelGame) {
        this.team = team;
        this.duelGame = duelGame;
    }

    public void spawn() {
        getTeamPlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();

            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(getSpawnLocation());

            getDuelGame().getKitManager().getKit(KitType.DEFAULT).apply(player);
        });
    }

    public ArrayList<DuelsPlayer> getAliveMembers() {
        ArrayList<DuelsPlayer> aliveMembers = new ArrayList<>();

        getTeamPlayers().forEach(gamePlayer -> {
            if (!gamePlayer.isKilled()) {
                aliveMembers.add(gamePlayer);
            }
        });

        return aliveMembers;
    }

}
