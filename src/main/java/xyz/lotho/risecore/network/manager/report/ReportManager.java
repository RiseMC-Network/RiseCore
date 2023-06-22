package xyz.lotho.risecore.network.manager.report;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReportManager {

    private final RiseCore riseCore;

    private final List<Report> reports = new ArrayList<>();

    public ReportManager(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    public void addReport(Report report) {
        getReports().add(report);
    }

    public void deleteReport(Report report) {
        getReports().remove(report);
    }

    // Get all reports that have been made about a player
    public Report getPlayerReports(String playerName) {
        for (Report report : getReports()) {
            if (report.getReported().equals(playerName)) {
                return report;
            }
        }

        return null;
    }

    // Get all reports submitted by a player
    public Report getReportsFromPlayer(String playerName) {
        for (Report report : getReports()) {
            if (report.getReporter().equals(playerName)) {
                return report;
            }
        }

        return null;
    }


}
