package xyz.lotho.risecore.game.duels.player;

import lombok.Getter;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.network.RiseCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class DuelsPlayerManager {

    private final RiseCore riseCore;
    private final DuelGame duelGame;

    private final Map<UUID, DuelsPlayer> gamePlayers = new HashMap<>();

    public DuelsPlayerManager(RiseCore riseCore, DuelGame duelGame) {
        this.riseCore = riseCore;
        this.duelGame = duelGame;
    }

    public void addPlayer(UUID uuid) {
        gamePlayers.put(uuid, new DuelsPlayer(riseCore, duelGame, uuid));
    }

    public DuelsPlayer getPlayer(UUID uuid) {
        return gamePlayers.getOrDefault(uuid, null);
    }

}
