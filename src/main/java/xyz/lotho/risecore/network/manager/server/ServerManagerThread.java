package xyz.lotho.risecore.network.manager.server;

import lombok.Getter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.server.ServerUpdatePacket;

import java.util.stream.Collectors;

@Getter
public class ServerManagerThread extends BukkitRunnable {

    private final RiseCore riseCore;

    public ServerManagerThread(RiseCore riseCore) {
        this.riseCore = riseCore;

        runTaskTimerAsynchronously(getRiseCore(), 0, 20);
    }

    public void run() {
        ServerUpdatePacket serverUpdatePacket = new ServerUpdatePacket(
                getRiseCore().getServerId(),
                getRiseCore().getServer().getVersion(),
                getRiseCore().getServer().getIp(),
                getRiseCore().getServer().hasWhitelist() ? ServerState.LOCKED : ServerState.ONLINE,
                getRiseCore().getServerType(),
                getRiseCore().getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()),
                System.currentTimeMillis(),
                getRiseCore().getServer().getMaxPlayers(),
                getRiseCore().getServer().getPort()
        );

        getRiseCore().getRedisManager().sendPacket(serverUpdatePacket, false);
    }
}
