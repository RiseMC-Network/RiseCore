package xyz.lotho.risecore.game.duels.player;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.duels.team.DuelsTeam;
import xyz.lotho.risecore.network.RiseCore;

import java.util.UUID;

@Getter
public class DuelsPlayer {

    private final RiseCore riseCore;
    private final DuelGame duelGame;

    private final UUID uuid;

    private int kills;

    private DuelsTeam duelsTeam;
    private boolean killed;

    public DuelsPlayer(RiseCore riseCore, DuelGame duelGame, UUID uuid) {
        this.riseCore = riseCore;
        this.duelGame = duelGame;
        this.uuid = uuid;
    }

    public void addKill() {
        this.kills++;
    }

    public void kill() {
        this.killed = true;
        getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    public Player getPlayer() {
        return getRiseCore().getServer().getPlayer(getUuid());
    }

    public DuelsTeam getDuelsTeam() {
        return this.duelsTeam;
    }

    public void setDuelsTeam(DuelsTeam duelsTeam) {
        this.duelsTeam = duelsTeam;
    }


}
