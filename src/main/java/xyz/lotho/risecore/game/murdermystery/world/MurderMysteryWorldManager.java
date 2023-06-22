package xyz.lotho.risecore.game.murdermystery.world;

import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.lotho.risecore.game.GameState;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.network.RiseCore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class MurderMysteryWorldManager {

    private final RiseCore riseCore;
    private final MurderMysteryGame murderMysteryGame;

    private String mapName;
    private World world;

    private final List<Location> possibleSpawnLocations = new ArrayList<>();

    public MurderMysteryWorldManager(RiseCore riseCore, MurderMysteryGame murderMysteryGame) {
        this.riseCore = riseCore;
        this.murderMysteryGame = murderMysteryGame;
    }

    public void loadMap() {
        SlimeLoader loader = RiseCore.getInstance().getSlimePlugin().getLoader("mongodb");

        getRiseCore().getServer().getScheduler().runTaskAsynchronously(RiseCore.getInstance(), () -> {
            try {
                int gameId = new Random().nextInt(10000) + 1;
                mapName = getMurderMysteryGame().getMapManager().getRandomMap();

                SlimeWorld gameMapTemplate = RiseCore.getInstance().getSlimePlugin().loadWorld(loader, mapName, true, new SlimePropertyMap());
                SlimeWorld gameMap = gameMapTemplate.clone("game-" + gameId, null);

                getRiseCore().getServer().getScheduler().runTask(RiseCore.getInstance(), () -> {
                    RiseCore.getInstance().getSlimePlugin().loadWorld(gameMap);

                    world = RiseCore.getInstance().getServer().getWorld(gameMap.getName());

                    worldSetup();
                });

            } catch (IOException | WorldAlreadyExistsException | WorldLockedException | CorruptedWorldException |
                     NewerFormatException | UnknownWorldException e) {
                e.printStackTrace();
            }
        });
    }

    public void worldSetup() {
        List<String> locations = getMurderMysteryGame().getMapConfiguration().get().getStringList("murder-mystery." + mapName + ".spawnLocations");

        locations.forEach(stringLoc -> {
            String[] locationSplit = stringLoc.split(", ");
            Location location = new Location(getWorld(), Float.parseFloat(locationSplit[0]), Float.parseFloat(locationSplit[1]), Float.parseFloat(locationSplit[2]), Float.parseFloat(locationSplit[3]), Float.parseFloat(locationSplit[4]));

            possibleSpawnLocations.add(location);
        });

        getMurderMysteryGame().setGameState(GameState.PLAYING);
    }

    public void unloadWorld() {
        getRiseCore().getServer().getScheduler().runTaskLater(getRiseCore(), () -> Bukkit.unloadWorld(getWorld(), true), 60L);
    }



}
