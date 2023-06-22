package xyz.lotho.risecore.network.manager.rank.tab;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
public class TabManager extends BukkitRunnable {

    private final RiseCore riseCore;

    public TabManager(RiseCore riseCore) {
        this.riseCore = riseCore;

        runTaskTimerAsynchronously(getRiseCore(), 0, 20);
    }

    public void run() {
        for (Player player : getRiseCore().getServer().getOnlinePlayers()) {
            String header = getRiseCore().getConfigurationFile().get().getString("tab.header")
                    .replace("%online%", String.valueOf(getRiseCore().getServerManager().getNetworkPlayerCount()))
                    .replace("%server%", getRiseCore().getServerId())
                    .replace("%serverCount%", String.valueOf(getRiseCore().getServer().getOnlinePlayers().size()));

            String footer = getRiseCore().getConfigurationFile().get().getString("tab.footer")
                    .replace("%online%", String.valueOf(getRiseCore().getServerManager().getNetworkPlayerCount()))
                    .replace("%server%", getRiseCore().getServerId())
                    .replace("%serverCount%", String.valueOf(getRiseCore().getServer().getOnlinePlayers().size()));

            player.setPlayerListHeader(CC.translate(header));
            player.setPlayerListFooter(CC.translate(footer));
        }
    }
}
