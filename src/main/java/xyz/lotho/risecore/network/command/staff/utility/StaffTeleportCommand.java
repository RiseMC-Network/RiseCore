package xyz.lotho.risecore.network.command.staff.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.player.RequestPlayerPacket;
import xyz.lotho.risecore.network.database.redis.packet.staff.StaffTeleportPacket;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.Tasks;

@Getter
@CommandAlias("stp")
@CommandPermission("risecore.staff")
public class StaffTeleportCommand extends BaseCommand {

    private final RiseCore riseCore;

    public StaffTeleportCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Teleport command sender to target player across servers.")
    public void teleport(Player player, String target) {
        Server server = getRiseCore().getServerManager().findPlayer(target);

        if (server == null) {
            player.sendMessage(CC.RED + "Player not found!");
            return;
        }

        RequestPlayerPacket requestPlayerPacket = new RequestPlayerPacket(player.getName(), server.getId(), "CONSOLE");
        getRiseCore().getRedisManager().sendPacket(requestPlayerPacket, false);

        Tasks.runLater(() -> {
            StaffTeleportPacket teleportPacket = new StaffTeleportPacket(player.getName(), target, server.getId());
            getRiseCore().getRedisManager().sendPacket(teleportPacket, false);
        }, 30L);
    }
}
