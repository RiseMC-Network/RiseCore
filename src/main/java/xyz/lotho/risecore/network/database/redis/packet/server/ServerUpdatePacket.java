package xyz.lotho.risecore.network.database.redis.packet.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.manager.server.ServerState;
import xyz.lotho.risecore.network.manager.server.ServerType;
import xyz.lotho.risecore.network.util.AdminUtil;
import xyz.lotho.risecore.network.util.CC;

import java.util.List;

@AllArgsConstructor
@Getter
public class ServerUpdatePacket extends Packet {

    private String serverId, serverVersion, serverAddress;
    private ServerState serverState;
    private ServerType serverType;
    private List<String> players;
    private long lastUpdated;
    private int maxPlayers, port;

    @Override
    public String getPacketName() {
        return "ServerUpdatePacket";
    }

    @Override
    public void onReceive() {
        Server newServer = new Server(
                getServerId(),
                getServerVersion(),
                getServerAddress(),
                getServerState(),
                getServerType(),
                getPlayers(),
                getLastUpdated(),
                getMaxPlayers(),
                getPort()
        );

        Server oldServer = RiseCore.getInstance().getServerManager().getServer(getServerId());
        boolean wasOffline = oldServer == null || oldServer.getState() == ServerState.OFFLINE;
        boolean isOnline = newServer.getState() == ServerState.ONLINE;

        String message = "";

        if (wasOffline && isOnline) {
            message = CC.translate("&d[SERVER] &f" + getServerId() + " &dis now back &fonline &7(/sm)");
        } else if (!wasOffline && !isOnline) {
            message = CC.translate("&d[SERVER] &f" + getServerId() + " &dhas went &foffline &7(/sm)");
        }

        if (!message.isEmpty()) AdminUtil.announce(message);
        RiseCore.getInstance().getServerManager().updateServer(getServerId(), newServer);
    }
}
