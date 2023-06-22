package xyz.lotho.risecore.network.database.redis.packet.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.Tasks;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class StaffTeleportPacket extends Packet {

    private final String staffName, targetName, serverId;

    @Override
    public String getPacketName() {
        return "StaffTeleportPacket";
    }

    @Override
    public void onReceive() {
        if (!Objects.equals(RiseCore.getInstance().getServerId(), serverId)) return;

        Player player = RiseCore.getInstance().getServer().getPlayer(staffName);
        Player target = RiseCore.getInstance().getServer().getPlayer(targetName);

        if (player == null || target == null) return;

        Tasks.runSync(() -> {
            player.teleport(target);
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(CC.translate("&aYou have been teleported to &f" + target.getName()));
        });
    }
}
