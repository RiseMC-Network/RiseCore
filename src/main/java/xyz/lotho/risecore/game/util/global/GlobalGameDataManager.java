package xyz.lotho.risecore.game.util.global;

import lombok.Getter;
import xyz.lotho.risecore.network.RiseCore;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GlobalGameDataManager {

    private final RiseCore riseCore;

    private final Map<Integer, GlobalGameData> globalGameData = new HashMap<>();

    public GlobalGameDataManager(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    public void updateGlobalGameData(GlobalGameData globalGameData) {
        this.globalGameData.put(globalGameData.getGameId(), globalGameData);
    }

    public void removeGlobalGameData(int gameId) {
        this.globalGameData.remove(gameId);
    }

    public GlobalGameData getGameData(int gameId) {
        return this.globalGameData.get(gameId);
    }

}
