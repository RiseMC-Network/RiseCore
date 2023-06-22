package xyz.lotho.risecore.network.manager.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Report {

    private final String serverId;
    private final String reporter;
    private final String reported;
    private final String reason;
    private final long reportTime;

}
