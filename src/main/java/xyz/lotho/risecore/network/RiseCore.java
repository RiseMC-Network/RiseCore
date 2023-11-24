package xyz.lotho.risecore.network;

import co.aikar.commands.BukkitCommandManager;
import com.google.gson.Gson;
import com.infernalsuite.aswm.api.SlimePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.risecore.game.util.GameManager;
import xyz.lotho.risecore.network.command.admin.game.GamesCommand;
import xyz.lotho.risecore.network.command.admin.server.RequestCommand;
import xyz.lotho.risecore.network.command.admin.server.ServerManagerCommand;
import xyz.lotho.risecore.network.command.admin.utility.RedisCommand;
import xyz.lotho.risecore.network.command.admin.utility.SetRankCommand;
import xyz.lotho.risecore.network.command.player.ReportCommand;
import xyz.lotho.risecore.network.command.staff.game.EndGameCommand;
import xyz.lotho.risecore.network.command.staff.game.StartGameCommand;
import xyz.lotho.risecore.network.command.staff.utility.*;
import xyz.lotho.risecore.network.database.mongo.MongoManager;
import xyz.lotho.risecore.network.database.redis.manager.RedisManager;
import xyz.lotho.risecore.network.manager.profile.Profile;
import xyz.lotho.risecore.network.manager.profile.ProfileManager;
import xyz.lotho.risecore.network.manager.rank.RankManager;
import xyz.lotho.risecore.network.manager.report.ReportManager;
import xyz.lotho.risecore.network.util.Configuration;
import xyz.lotho.risecore.network.manager.events.ServerEventsManager;
import xyz.lotho.risecore.network.manager.server.ServerManager;
import xyz.lotho.risecore.network.manager.server.ServerType;

import java.util.logging.Level;

@Getter
@Setter
public final class RiseCore extends JavaPlugin {

    private static RiseCore instance;

    private BukkitCommandManager commandManager;
    private ServerEventsManager eventsManager;

    private Configuration configurationFile;
    private Configuration languagesFile;
    private Configuration permissionsFile;

    private Gson gson;

    private MongoManager mongoManager;
    private RedisManager redisManager;
    private ProfileManager profileManager;
    private RankManager rankManager;
    private ServerManager serverManager;
    private ReportManager reportManager;
    private GameManager gameManager;

    private SlimePlugin slimePlugin;

    private String serverId;
    private ServerType serverType;
    private Location spawnLocation;

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable() {
        getLogger().log(Level.INFO, "RiseCore is now enabled!");

        // set static instance
        instance = this;

        // Initialize command manager and commands
        commandManager = new BukkitCommandManager(this);
        commandManager.enableUnstableAPI("help");
        loadCommands();

        // initialize events manager
        eventsManager = new ServerEventsManager(this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        slimePlugin = (SlimePlugin) getServer().getPluginManager().getPlugin("SlimeWorldManager");

        // load configuration files
        configurationFile = new Configuration(this, "config");
        permissionsFile = new Configuration(this, "permissions");

        // set server information
        serverId = getConfigurationFile().get().getString("server.id");
        serverType = ServerType.valueOf(getConfigurationFile().get().getString("server.type"));

        gson = new Gson();

        // Handle enabling
        try {
            mongoManager = new MongoManager(this);
            redisManager = new RedisManager(this);
            rankManager = new RankManager(this);
            profileManager = new ProfileManager(this);
            serverManager = new ServerManager(this);
            reportManager = new ReportManager(this);
            gameManager = new GameManager(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        getServer().getOnlinePlayers().forEach(player -> {
            Profile profile = getProfileManager().loadProfile(player.getUniqueId(), player.getName());
            profile.calculatePermissions();
        });
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "RiseCore is now disabled!");

        // Handle disabling
        getMongoManager().disconnect();
        getRedisManager().close();

        getEventsManager().unloadListeners();
        getServerManager().shutdown();
    }

    private void loadCommands() {
        getCommandManager().registerCommand(new ServerManagerCommand(this));
        getCommandManager().registerCommand(new RedisCommand(this));
        getCommandManager().registerCommand(new RequestCommand(this));
        getCommandManager().registerCommand(new StartGameCommand(this));
        getCommandManager().registerCommand(new GameModeCommand(this));
        getCommandManager().registerCommand(new EndGameCommand(this));
        getCommandManager().registerCommand(new GamesCommand(this));
        getCommandManager().registerCommand(new SetRankCommand(this));
        getCommandManager().registerCommand(new ProfileCommand(this));
        getCommandManager().registerCommand(new StaffTeleportCommand(this));
        getCommandManager().registerCommand(new StaffChatCommand(this));
        getCommandManager().registerCommand(new ReportCommand(this));
        getCommandManager().registerCommand(new ReportsMenuCommand(this));
    }

    public static RiseCore getInstance() {
        return instance;
    }
}
