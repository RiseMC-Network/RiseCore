package xyz.lotho.risecore.game.duels.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotho.risecore.game.util.Game;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.game.duels.board.DuelsBoardManager;
import xyz.lotho.risecore.game.duels.events.DuelsEventsManager;
import xyz.lotho.risecore.game.duels.kit.KitManager;
import xyz.lotho.risecore.game.duels.player.DuelsPlayerManager;
import xyz.lotho.risecore.game.util.GameState;
import xyz.lotho.risecore.game.duels.team.DuelsTeam;
import xyz.lotho.risecore.game.duels.team.DuelsTeamManager;
import xyz.lotho.risecore.game.duels.utils.DuelsGameUtils;
import xyz.lotho.risecore.game.duels.world.DuelsWorldManager;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.game.GameEndPacket;
import xyz.lotho.risecore.network.database.redis.packet.game.GameStartPacket;
import xyz.lotho.risecore.network.database.redis.packet.player.RequestPlayerPacket;
import xyz.lotho.risecore.network.manager.profile.Profile;
import xyz.lotho.risecore.network.util.Configuration;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.util.CC;

import java.util.*;

@Getter
@Setter
public class DuelGame extends Game {

    private final RiseCore riseCore;
    private final GameStartPacket gameStartPacket;

    private final Configuration mapConfiguration;
    private final DuelsPlayerManager gamePlayerManager;
    private final DuelsTeamManager duelsTeamManager;
    private final DuelsWorldManager worldManager;
    private final DuelsBoardManager duelsBoardManager;
    private final DuelsEventsManager eventsManager;
    private final KitManager kitManager;

    private long startTime;
    private GameState gameState;

    private int graceTime = 10;
    private final int maxTime = 600_000; // 10 minutes in millis

    private int gameTickTaskId;

    public DuelGame(RiseCore riseCore, GameStartPacket gameStartPacket) {
        this.riseCore = riseCore;
        this.gameStartPacket = gameStartPacket;

        mapConfiguration = new Configuration(getRiseCore(), "maps");
        gamePlayerManager = new DuelsPlayerManager(getRiseCore(), this);
        duelsTeamManager = new DuelsTeamManager(getRiseCore(), this);
        worldManager = new DuelsWorldManager(getRiseCore(), this);
        duelsBoardManager = new DuelsBoardManager(getRiseCore(), this);
        eventsManager = new DuelsEventsManager(getRiseCore(), this);
        kitManager = new KitManager(getRiseCore());
    }

    public long getGameTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public int getGameId() {
        return this.gameStartPacket.getGameId();
    }

    @Override
    public void startGame() {
        setGameState(GameState.WAITING);
        startTime = System.currentTimeMillis();
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(GameState state) {
        this.gameState = state;

        switch (state) {
            case WAITING -> {
                getGameStartPacket().getPlayerNames().forEach(playerName -> {
                    Player player = getRiseCore().getServer().getPlayer(playerName);
                    if (player != null) getGamePlayerManager().addPlayer(player.getUniqueId());
                });

                BukkitTask gameTickTask = getRiseCore().getServer().getScheduler().runTaskTimer(getRiseCore(), this::gameTick, 0L, 20L);
                this.gameTickTaskId = gameTickTask.getTaskId();

                getDuelsTeamManager().loadTeams();
                getWorldManager().loadMap();
            }
            case GRACE -> {
                getDuelsTeamManager().getTeams().forEach((teamName, team) -> team.spawn());
            }
            case PLAYING -> announce("&aThe game has started!");
            case ENDED -> {
                DuelsTeam winningDuelsTeam = getDuelsTeamManager().getWinningTeam();
                announce(DuelsGameUtils.winningMessage(winningDuelsTeam));

                // add winning prize (20 gold) to each player on winning team
                winningDuelsTeam.getTeamPlayers().forEach(teamPlayer -> {
                    Profile profile = getRiseCore().getProfileManager().getProfile(teamPlayer.getUuid());
                    profile.setGold(profile.getGold() + 20);

                    getRiseCore().getProfileManager().saveProfile(profile);
                    teamPlayer.getPlayer().sendMessage(CC.translate("&6&l+20 Gold"));
                });

                getRiseCore().getServer().getScheduler().runTaskLater(getRiseCore(), this::endGame, 100L);
            }
        }
    }

    @Override
    public void gameTick() {
        updateGlobalGameData();

        switch (getGameState()) {
            case GRACE -> {
                getDuelsBoardManager().updateBoards();

                if (getGraceTime() <= 0) {
                    announce("&aFight!");
                    setGameState(GameState.PLAYING);
                    break;
                }

                if (getGraceTime() <= 5) {
                    announce("&aGame is starting in &f" + getGraceTime() + " &aseconds!");
                }

                this.graceTime--;
            }

            case PLAYING -> {
                getDuelsBoardManager().updateBoards();

                long difference = getGameTime();

                if (difference >= getMaxTime()) {
                    announce("&cGame has ended! Match lasted too long.");
                    endGame();
                }

                if (getDuelsTeamManager().getAliveTeams().size() == 1) {
                    this.setGameState(GameState.ENDED);
                }
            }
        }
    }

    @Override
    public void announce(String message) {
        getGamePlayers().forEach(player -> player.sendMessage(CC.translate(message)));
    }

    @Override
    public void endGame() {
        getRiseCore().getServer().getScheduler().cancelTask(getGameTickTaskId());

        getRiseCore().getLogger().info("Destroying game boards...");
        getDuelsBoardManager().destroyBoards();

        Server server = getRiseCore().getServerManager().getRandomLobbyServer();
        getGamePlayers().forEach(player -> {
            RequestPlayerPacket requestPlayerPacket = new RequestPlayerPacket(player.getName(), server.getId(), "CONSOLE");
            getRiseCore().getRedisManager().sendPacket(requestPlayerPacket, false);
        });

        getRiseCore().getLogger().info("Attempting to unload game listeners..");
        getEventsManager().unloadListeners();

        getRiseCore().getLogger().info("Attempting to unload game world...");
        getWorldManager().unloadWorld();

        GameEndPacket gameEndPacket = new GameEndPacket(getGameId());
        getRiseCore().getRedisManager().sendPacket(gameEndPacket, false);
    }

    @Override
    public ArrayList<Player> getGamePlayers() {
        return new ArrayList<>(this.gamePlayerManager.getGamePlayers().values().stream()
                .map(gp -> getRiseCore().getServer().getPlayer(gp.getUuid()))
                .toList());
    }

    @Override
    public GameType getGameType() {
        return this.gameStartPacket.getGameType();
    }

    @Override
    public Server getGameServer() {
        return getRiseCore().getServerManager().getServer(this.gameStartPacket.getGameServerId());
    }
}
