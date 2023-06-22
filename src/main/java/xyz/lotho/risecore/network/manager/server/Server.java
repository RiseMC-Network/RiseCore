package xyz.lotho.risecore.network.manager.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import xyz.lotho.risecore.network.util.TimeUtil;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Server {

    private String id, version, address;
    private ServerState state;
    private ServerType type;
    private List<String> players;
    private long lastUpdated;
    private int maxPlayers, port;

    public String getLastUpdatedPretty() {
        return TimeUtil.formatMillis(System.currentTimeMillis() - this.lastUpdated);
    }
}
