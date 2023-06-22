package xyz.lotho.risecore.network.database.redis.packet.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;
import xyz.lotho.risecore.network.util.CC;

@Getter
@AllArgsConstructor
public class StaffJoinPacket extends Packet {

    private String serverId, staffUsername;

    @Override
    public String getPacketName() {
        return "StaffJoinPacket";
    }

    @Override
    public void onReceive() {
        RiseCore.getInstance().getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("risecore.staff")).forEach(player -> {
            player.sendMessage(CC.translate("&b[STAFF] &f" + getStaffUsername() + " &bhas joined &f" + getServerId()));
        });
    }
}
