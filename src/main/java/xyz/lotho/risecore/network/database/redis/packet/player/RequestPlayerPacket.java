package xyz.lotho.risecore.network.database.redis.packet.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;
import xyz.lotho.risecore.network.util.BungeeUtil;
import xyz.lotho.risecore.network.util.CC;

@Getter
@AllArgsConstructor
public class RequestPlayerPacket extends Packet {

    private String playerName, serverId, requestedBy;

    @Override
    public String getPacketName() {
        return "RequestPlayerPacket";
    }

    @Override
    public void onReceive() {
        if (getPlayerName() == null || getServerId() == null) return;

        Player player = RiseCore.getInstance().getServer().getPlayer(getPlayerName());
        if (player == null) return;

        RiseCore.getInstance().getLogger().info(requestedBy + " requested " + playerName + " to be sent to " + serverId);

        player.sendMessage(CC.GREEN + "Sending you to server " + CC.WHITE + getServerId() + CC.GREEN + "...");
        BungeeUtil.sendPlayerToServer(player, getServerId());
    }
}
