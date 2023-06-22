package xyz.lotho.risecore.game.murdermystery.player;

import lombok.Getter;
import xyz.lotho.risecore.game.murdermystery.role.GameRole;
import xyz.lotho.risecore.network.RiseCore;

import java.util.*;

@Getter
public class MurderMysteryPlayerManager {

    private final RiseCore riseCore;

    public Map<UUID, MurderMysteryPlayer> players = new HashMap<>();

    public MurderMysteryPlayerManager(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    public void addPlayer(UUID uuid) {
        players.put(uuid, new MurderMysteryPlayer(uuid));
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public MurderMysteryPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public List<MurderMysteryPlayer> getAlivePlayers() {
        return players.values().stream().filter(player -> player.isAlive() && player.getRole() != GameRole.MURDERER).toList();
    }

    public boolean isMurdererAlive() {
        return players.values().stream().anyMatch(player -> player.getRole() == GameRole.MURDERER && player.isAlive());
    }

    public MurderMysteryPlayer getMurderer() {
        return players.values().stream().filter(player -> player.getRole() == GameRole.MURDERER).findFirst().get();
    }

    public List<MurderMysteryPlayer> getAllAlivePlayers() {
        return players.values().stream().filter(MurderMysteryPlayer::isAlive).toList();
    }

    public GameRole getWinningRole() {
        if (getAlivePlayers().size() > 0) { // innocents/detective won
            return GameRole.INNOCENT;
        } else { // murderer won
            return GameRole.MURDERER;
        }
    }

}
