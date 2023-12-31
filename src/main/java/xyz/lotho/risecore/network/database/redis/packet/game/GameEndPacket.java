package xyz.lotho.risecore.network.database.redis.packet.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;

@Getter
@AllArgsConstructor
public class GameEndPacket extends Packet {

    private int gameId;

    @Override
    public String getPacketName() {
        return "GameEndPacket";
    }

    @Override
    public void onReceive() {
        // handle removing the actual game instance on the game server
        RiseCore.getInstance().getGameManager().removeGame(getGameId());

        // handle removing the game data from every other server on the network
        RiseCore.getInstance().getGameManager().getGlobalGameDataManager().removeGlobalGameData(getGameId());
    }
}
