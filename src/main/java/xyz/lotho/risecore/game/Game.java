package xyz.lotho.risecore.game;

import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.manager.server.Server;

import java.util.ArrayList;

public abstract class Game {

    public abstract int getGameId();

    public abstract void startGame();

    public abstract void setGameState(GameState gameState);

    public abstract GameState getGameState();

    public abstract void gameTick();

    public abstract void announce(String message);

    public abstract void endGame();

    public abstract ArrayList<Player> getGamePlayers();

    public abstract GameType getGameType();

    public abstract Server getGameServer();

}
