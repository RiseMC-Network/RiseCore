package xyz.lotho.risecore.network.database.redis.packet.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;
import xyz.lotho.risecore.network.manager.report.Report;
import xyz.lotho.risecore.network.util.CC;

@Getter
@AllArgsConstructor
public class PlayerReportPacket extends Packet {

    private String serverId;
    private String reporter;
    private String reported;
    private String reason;
    private long reportTime;

    @Override
    public String getPacketName() {
        return "PlayerReportPacket";
    }

    @Override
    public void onReceive() {
        Report report = new Report(
                getServerId(),
                getReporter(),
                getReported(),
                getReason(),
                getReportTime()
        );

        RiseCore.getInstance().getReportManager().addReport(report);

        RiseCore.getInstance().getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("risecore.staff")).forEach(player -> {
            player.sendMessage(CC.translate("&c[REPORT]"+ " &f" + getReporter() + " &chas reported &f" + getReported() + " &cfor &f" + getReason()));
        });
    }
}
