package xyz.lotho.risecore.network.manager.rank;

import lombok.Getter;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.rank.tab.TabManager;
import xyz.lotho.risecore.network.manager.rank.tab.TabRankManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class RankManager {

    private final RiseCore riseCore;
    private final TabManager tabManager;
    private final TabRankManager tabRankManager;

    private final Map<String, Rank> ranks = new ConcurrentHashMap<>();

    public RankManager(RiseCore riseCore) {
        this.riseCore = riseCore;
        this.tabManager = new TabManager(getRiseCore());
        this.tabRankManager = new TabRankManager(getRiseCore(), this);

        load();
    }

    public void load() {
        for (String rankId : getRiseCore().getPermissionsFile().get().getConfigurationSection("ranks").getKeys(false)) {
            Rank rank = new Rank(
                    rankId.toUpperCase(),
                    getRiseCore().getPermissionsFile().get().getString("ranks." + rankId + ".prefix"),
                    getRiseCore().getPermissionsFile().get().getString("ranks." + rankId + ".nameColor"),
                    getRiseCore().getPermissionsFile().get().getString("ranks." + rankId + ".chatColor"),
                    getRiseCore().getPermissionsFile().get().getInt("ranks." + rankId + ".priority"),
                    getRiseCore().getPermissionsFile().get().getBoolean("ranks." + rankId + ".default"),
                    getRiseCore().getPermissionsFile().get().getStringList("ranks." + rankId + ".inheritance"),
                    getInheritedPermissions(rankId)
            );

            getRanks().put(rankId.toUpperCase(), rank);
        }

        // load all tab list rank teams
        getTabRankManager().load();
    }

    public Rank getRank(String rankId) {
        return getRanks().get(rankId.toUpperCase());
    }

    public Rank getDefaultRank() {
        return getRanks().values().stream().filter(Rank::isDefaultRank).findFirst().orElse(null);
    }

    // Usage of "HashSet" to avoid duplicate permissions when iterating through inherited ranks
    public Set<String> getInheritedPermissions(String rankName) {
        List<String> inheritedRanks = getRiseCore().getPermissionsFile().get().getStringList("ranks." + rankName.toLowerCase() + ".inheritance");
        Set<String> permissions = new HashSet<>();

        // Recursive call for each inherited rank
        for (String inheritedRank : inheritedRanks) {
            Set<String> inheritedPermissions = getInheritedPermissions(inheritedRank);
            permissions.addAll(inheritedPermissions);
        }

        // Add rank's own permissions
        permissions.addAll(getRiseCore().getPermissionsFile().get().getStringList("ranks." + rankName.toLowerCase() + ".permissions"));

        return permissions;
    }
}
