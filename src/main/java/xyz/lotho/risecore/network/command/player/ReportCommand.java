package xyz.lotho.risecore.network.command.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.staff.PlayerReportPacket;
import xyz.lotho.risecore.network.util.CC;

@Getter
@CommandAlias("report")
public class ReportCommand extends BaseCommand {

    private final RiseCore riseCore;

    public ReportCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Report a player for breaking the rules.")
    public void createReport(Player player, String reported, String reason) {
        if (reported.equalsIgnoreCase(player.getName())) {
            player.sendMessage(CC.RED + "You cannot report yourself.");
            return;
        }

        if (getRiseCore().getServerManager().findPlayer(reported) == null) {
            player.sendMessage(CC.RED + "That player is not online.");
            return;
        }

        PlayerReportPacket playerReportPacket = new PlayerReportPacket(
                getRiseCore().getServerId(),
                player.getName(),
                reported,
                reason,
                System.currentTimeMillis()
        );

        getRiseCore().getRedisManager().sendPacket(playerReportPacket, false);
        player.sendMessage(CC.GREEN + "Your report against " + reported + " has been submitted.");
    }

}
