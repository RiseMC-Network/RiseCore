package xyz.lotho.risecore.game.util;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GameManager {

    private final RiseCore riseCore;

    private final Map<Integer, Game> games = new HashMap<>();

    public GameManager(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    public Game findGameByUuid(int id) {
        return games.getOrDefault(id, null);
    }

    public Game findGameByPlayer(Player player) {
        for (Game game : games.values()) {
            if (game.getGamePlayers().contains(player)) {
                return game;
            }
        }

        return null;
    }

    public void addGame(Game game) {
        games.put(game.getGameId(), game);
    }

    public void removeGame(int gameId) {
        games.remove(gameId);
    }


}
