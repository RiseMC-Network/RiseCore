package xyz.lotho.risecore.network.database.redis.packet.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;
import xyz.lotho.risecore.network.util.CC;

@AllArgsConstructor
@Getter
public class ServerAlertPacket extends Packet {

    public String senderUsername;
    public String messageContent;
    public String permissionToReceive;

    @Override
    public String getPacketName() {
        return "MessageAlertPacket";
    }

    @Override
    public void onReceive() {
        if (getPermissionToReceive().isEmpty()) {
            RiseCore.getInstance().getServer().broadcastMessage(getMessageContent());
        } else {
            RiseCore.getInstance().getServer().getOnlinePlayers().forEach((player) -> {
                if (player.hasPermission(getPermissionToReceive())) {
                    player.sendMessage(CC.translate(getMessageContent()));
                }
            });
        }
    }
}
