package xyz.lotho.risecore.game.murdermystery.game;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotho.risecore.game.util.Game;
import xyz.lotho.risecore.game.util.GameState;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.game.murdermystery.board.MurderMysteryBoardManager;
import xyz.lotho.risecore.game.murdermystery.events.MurderMysteryEventsManager;
import xyz.lotho.risecore.game.murdermystery.map.MapManager;
import xyz.lotho.risecore.game.murdermystery.player.MurderMysteryPlayer;
import xyz.lotho.risecore.game.murdermystery.player.MurderMysteryPlayerManager;
import xyz.lotho.risecore.game.murdermystery.role.GameRole;
import xyz.lotho.risecore.game.murdermystery.utils.MurderMysteryUtils;
import xyz.lotho.risecore.game.murdermystery.world.MurderMysteryWorldManager;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.game.GameEndPacket;
import xyz.lotho.risecore.network.database.redis.packet.game.GameStartPacket;
import xyz.lotho.risecore.network.database.redis.packet.player.RequestPlayerPacket;
import xyz.lotho.risecore.network.manager.profile.Profile;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Getter
public class MurderMysteryGame extends Game {

    private final RiseCore riseCore;
    private final GameStartPacket gameStartPacket;

    private final Configuration mapConfiguration;
    private final MapManager mapManager;
    private final MurderMysteryPlayerManager murderMysteryPlayerManager;
    private final MurderMysteryWorldManager worldManager;
    private final MurderMysteryBoardManager boardManager;
    private final MurderMysteryEventsManager eventsManager;

    private GameState gameState;
    private long startTime;

    private final int maxTime = 600_000; // 10 minutes in millis

    private int gameTickTaskId;

    public MurderMysteryGame(RiseCore riseCore, GameStartPacket gameStartPacket) {
        this.riseCore = riseCore;
        this.gameStartPacket = gameStartPacket;

        mapConfiguration = new Configuration(getRiseCore(), "maps");
        mapManager = new MapManager(getRiseCore(), this);
        murderMysteryPlayerManager = new MurderMysteryPlayerManager(getRiseCore());
        worldManager = new MurderMysteryWorldManager(getRiseCore(), this);
        boardManager = new MurderMysteryBoardManager(getRiseCore(), this);
        eventsManager = new MurderMysteryEventsManager(getRiseCore(), this);
    }

    public void playMassSound(Sound sound, float volume, float pitch) {
        for (Player player : getGamePlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public long getGameTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public int getGameId() {
        return gameStartPacket.getGameId();
    }

    @Override
    public void startGame() {
        setGameState(GameState.WAITING);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        switch (gameState) {
            case WAITING -> {
                BukkitTask gameTickTask = getRiseCore().getServer().getScheduler().runTaskTimer(getRiseCore(), this::gameTick, 0L, 20L);
                this.gameTickTaskId = gameTickTask.getTaskId();

                getGameStartPacket().getPlayerNames().forEach(playerName -> {
                    Player player = getRiseCore().getServer().getPlayer(playerName);
                    if (player != null) getMurderMysteryPlayerManager().addPlayer(player.getUniqueId());
                });

                getWorldManager().loadMap();
            }
            case PLAYING -> {
                List<MurderMysteryPlayer> players = new ArrayList<>(murderMysteryPlayerManager.getPlayers().values());

                // Shuffle so roles are always random
                Collections.shuffle(players);

                MurderMysteryPlayer detective = players.get(0);
                MurderMysteryPlayer murderer = players.get(1);

                detective.setRole(GameRole.DETECTIVE);
                murderer.setRole(GameRole.MURDERER);

                // Spawn player at random location defined in maps.yml for this map
                players.forEach(player -> {
                    List<Location> locations = getWorldManager().getPossibleSpawnLocations();
                    player.spawn(locations.get(new Random().nextInt(locations.size())));
                });

                announce("&aThe game has started now!\nAnyone could be the &cmurderer&a! Be wary.");
            }
            case ENDED -> {
                // TODO: FIND WINNER, REWARDS, ETC
                GameRole winningRole = getMurderMysteryPlayerManager().getWinningRole();
                announce(MurderMysteryUtils.winningMessage(winningRole, getMurderMysteryPlayerManager()));

                // add winning prize (20 gold) to each player on winning team
                getMurderMysteryPlayerManager().getAllAlivePlayers().forEach(teamPlayer -> {
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
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void gameTick() {
        switch (gameState) {
            case PLAYING -> {
                getBoardManager().updateBoards();

                long difference = getGameTime();

                if (difference >= getMaxTime()) {
                    announce("&cGame has ended! Match lasted too long.");
                    endGame();
                }

                // check if murderer has died
                if (!getMurderMysteryPlayerManager().isMurdererAlive() || getMurderMysteryPlayerManager().getAlivePlayers().size() < 1) {
                    setGameState(GameState.ENDED);
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
        getBoardManager().destroyBoards();

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
        return new ArrayList<>(this.murderMysteryPlayerManager.getPlayers().values().stream()
                .map(player -> getRiseCore().getServer().getPlayer(player.getUuid()))
                .toList());
    }

    @Override
    public GameType getGameType() {
        return gameStartPacket.getGameType();
    }

    @Override
    public Server getGameServer() {
        return getRiseCore().getServerManager().getServer(gameStartPacket.getGameServerId());
    }
}
