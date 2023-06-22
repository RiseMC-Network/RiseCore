package xyz.lotho.risecore.game.murdermystery.map;

import lombok.Getter;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.network.RiseCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class MapManager {

    private final RiseCore riseCore;
    private final MurderMysteryGame murderMysteryGame;

    public MapManager(RiseCore riseCore, MurderMysteryGame murderMysteryGame) {
        this.riseCore = riseCore;
        this.murderMysteryGame = murderMysteryGame;
    }

    public String getRandomMap() {
        List<String> mapNames = new ArrayList<>(this.murderMysteryGame.getMapConfiguration().get().getConfigurationSection("murder-mystery").getKeys(false));
        return mapNames.get(new Random().nextInt(mapNames.size()));
    }

}
