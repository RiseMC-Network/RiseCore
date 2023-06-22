package xyz.lotho.risecore.network.database.redis.packet.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.game.GameType;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.player.RequestPlayerPacket;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameStartPacket extends Packet {

    private int gameId;
    private GameType gameType;
    private List<String> playerNames;
    private String gameServerId;

    @Override
    public String getPacketName() {
        return "GameStartPacket";
    }

    @Override
    public void onReceive() {
        // prevent game starting on any server that is not the already set game server
        if (!getGameServerId().equals(RiseCore.getInstance().getServerId())) return;

        RiseCore.getInstance().getLogger().info("Received game start packet for " + getGameType() + " with " + getPlayerNames().size() + " players.");

        // send all players to the game server
        for (String playerName : getPlayerNames()) {
            RiseCore.getInstance().getRedisManager().sendPacket(new RequestPlayerPacket(playerName, getGameServerId(), "CONSOLE"), false);
        }

        if (getGameType() == GameType.DUELS) {
            DuelGame duelGame = new DuelGame(RiseCore.getInstance(), this);
            RiseCore.getInstance().getGameManager().addGame(duelGame);

            RiseCore.getInstance().getServer().getScheduler().runTaskLater(RiseCore.getInstance(), duelGame::startGame, 100L);
        }
        else if (getGameType() == GameType.MURDER_MYSTERY) {
            MurderMysteryGame murderMysteryGame = new MurderMysteryGame(RiseCore.getInstance(), this);
            RiseCore.getInstance().getGameManager().addGame(murderMysteryGame);

            RiseCore.getInstance().getServer().getScheduler().runTaskLater(RiseCore.getInstance(), murderMysteryGame::startGame, 100L);
        }
    }
}
