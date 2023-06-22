package xyz.lotho.risecore.network.database.redis.packet.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.game.util.GameState;
import xyz.lotho.risecore.game.util.GameType;
import xyz.lotho.risecore.game.util.global.GlobalGameData;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameUpdateDataPacket extends Packet {

    private String serverId;
    private int gameId;
    private GameType gameType;
    private GameState gameState;
    private List<String> players;
    private long startTime;

    @Override
    public String getPacketName() {
        return "GameUpdateDataPacket";
    }

    @Override
    public void onReceive() {
        GlobalGameData globalGameData = new GlobalGameData(
                serverId,
                gameId,
                gameType,
                gameState,
                players,
                startTime
        );

        RiseCore.getInstance().getGameManager().getGlobalGameDataManager().updateGlobalGameData(globalGameData);
    }
}
