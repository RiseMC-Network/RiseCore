package xyz.lotho.risecore.network.manager.server;

import lombok.Getter;
import org.bukkit.entity.HumanEntity;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.server.ServerUpdatePacket;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ServerManager {

    private final RiseCore riseCore;
    private final ServerManagerThread serverManagerThread;

    private final Map<String, Server> servers = new HashMap<>();

    public ServerManager(RiseCore riseCore) {
        this.riseCore = riseCore;

        serverManagerThread = new ServerManagerThread(getRiseCore());
    }

    public void updateServer(String serverId, Server server) {
        this.servers.put(serverId, server);
    }

    public Server getServer(String serverId) {
        return this.servers.getOrDefault(serverId, null);
    }

    public void removeServer(String serverId) {
        this.servers.remove(serverId);
    }

    public boolean serverExists(String serverId) {
        return this.servers.containsKey(serverId);
    }

    public Server getRandomGameServer() {
        List<Server> gameServers = getServers().values().stream()
                .filter(server -> server.getType() == ServerType.GAME && server.getState() == ServerState.ONLINE)
                .toList();

        if (!gameServers.isEmpty()) {
            return gameServers.get(new Random().nextInt(gameServers.size()));
        }

        return null;
    }

    public Server getRandomLobbyServer() {
        List<Server> gameServers = getServers().values().stream()
                .filter(server -> server.getType() == ServerType.LOBBY && server.getState() == ServerState.ONLINE)
                .toList();

        if (!gameServers.isEmpty()) {
            return gameServers.get(new Random().nextInt(gameServers.size()));
        }

        return null;
    }

    public Server findPlayer(String username) {
        Optional<Server> serverOptional = getServers().values().stream().filter(
                server -> server.getPlayers().stream().anyMatch(
                        player -> player.equalsIgnoreCase(username)
                )
        ).findFirst();

        return serverOptional.orElse(null);
    }

    // Returns servers in a List instead of HashMap, ordered by LOBBY
    public List<Server> getServersList() {
        ArrayList<Server> servers = new ArrayList<>(this.servers.values());

        Comparator<Server> serverComparator = Comparator.comparing(Server::getType, Comparator.comparingInt(ServerType::ordinal));
        servers.sort(serverComparator);

        return servers;
    }

    public int getNetworkPlayerCount() {
        return getServers().values().stream().mapToInt(server -> server.getPlayers().size()).sum();
    }

    public int getPlayingCount() {
        return getServers().values().stream().filter(server -> server.getType() == ServerType.GAME).mapToInt(server -> server.getPlayers().size()).sum();
    }

    public void shutdown() {
        getServerManagerThread().cancel();

        ServerUpdatePacket serverUpdatePacket = new ServerUpdatePacket(
                getRiseCore().getServerId(),
                getRiseCore().getServer().getVersion(),
                getRiseCore().getServer().getIp(),
                ServerState.OFFLINE,
                getRiseCore().getServerType(),
                new ArrayList<>(),
                System.currentTimeMillis(),
                0,
                getRiseCore().getServer().getPort()
        );

        getRiseCore().getRedisManager().sendPacket(serverUpdatePacket, false);
    }
}
