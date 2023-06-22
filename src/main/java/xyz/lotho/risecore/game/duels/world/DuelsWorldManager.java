package xyz.lotho.risecore.game.duels.world;

import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.GameState;
import xyz.lotho.risecore.network.RiseCore;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
public class DuelsWorldManager {

    private final RiseCore riseCore;
    private final DuelGame duelGame;

    private World world;

    public DuelsWorldManager(RiseCore riseCore, DuelGame duelGame) {
        this.riseCore = riseCore;
        this.duelGame = duelGame;
    }

    public void loadMap() {
        SlimeLoader loader = RiseCore.getInstance().getSlimePlugin().getLoader("mongodb");

        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setValue(SlimeProperties.SPAWN_X, 0);
        slimePropertyMap.setValue(SlimeProperties.SPAWN_Y, 86);
        slimePropertyMap.setValue(SlimeProperties.SPAWN_Z, 0);

        getRiseCore().getServer().getScheduler().runTaskAsynchronously(RiseCore.getInstance(), () -> {
            try {
                int gameId = new Random().nextInt(10000) + 1;

                SlimeWorld gameMapTemplate = RiseCore.getInstance().getSlimePlugin().loadWorld(loader, "arena-1-template", true, slimePropertyMap);
                SlimeWorld gameMap = gameMapTemplate.clone("game-" + gameId, null);

                getRiseCore().getServer().getScheduler().runTask(RiseCore.getInstance(), () -> {
                    RiseCore.getInstance().getSlimePlugin().loadWorld(gameMap);
                    setWorld(RiseCore.getInstance().getServer().getWorld(gameMap.getName()));

                    worldSetup();
                });

            } catch (IOException | WorldAlreadyExistsException | WorldLockedException | CorruptedWorldException | NewerFormatException | UnknownWorldException e) {
                e.printStackTrace();
            }
        });
    }

    public void worldSetup() {
        Set<String> teams = getDuelGame().getMapConfiguration().get().getConfigurationSection("duels.arena-1").getKeys(false);

        teams.forEach(teamId -> {
            String[] locationSplit = getDuelGame().getMapConfiguration().get().getString("duels.arena-1." + teamId + ".spawnLocation").split(", ");
            Location location = new Location(getWorld(), Float.parseFloat(locationSplit[0]), Float.parseFloat(locationSplit[1]), Float.parseFloat(locationSplit[2]), Float.parseFloat(locationSplit[3]), Float.parseFloat(locationSplit[4]));

            getDuelGame().getDuelsTeamManager().getTeam(teamId).setSpawnLocation(location);
        });

        getDuelGame().setGameState(GameState.GRACE);
    }

    public void unloadWorld() {
        getRiseCore().getServer().getScheduler().runTaskLater(getRiseCore(), () -> Bukkit.unloadWorld(getWorld(), true), 60L);
    }

}
