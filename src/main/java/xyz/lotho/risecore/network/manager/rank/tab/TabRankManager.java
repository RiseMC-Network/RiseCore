package xyz.lotho.risecore.network.manager.rank.tab;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.rank.Rank;
import xyz.lotho.risecore.network.manager.rank.RankManager;
import xyz.lotho.risecore.network.util.CC;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TabRankManager {

    private final RiseCore riseCore;
    private final RankManager rankManager;

    private final Map<String, Team> rankTeams = new HashMap<>();

    public TabRankManager(RiseCore riseCore, RankManager rankManager) {
        this.riseCore = riseCore;
        this.rankManager = rankManager;
    }

    public void load() {
        Scoreboard scoreboard = getRiseCore().getServer().getScoreboardManager().getMainScoreboard();

        if (scoreboard.getObjective("showhealth") != null)
            scoreboard.getObjective("showhealth").unregister();

        Objective objective = scoreboard.registerNewObjective("showhealth", "health");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName(CC.translate("&c❤"));

        for (Map.Entry<String, Rank> rank : getRankManager().getRanks().entrySet()) {
            String rankId = rank.getValue().getPriority() + "_" + rank.getKey().toUpperCase();

            if (scoreboard.getTeam(rankId) != null) {
                scoreboard.getTeam(rankId).unregister();
            }

            Team team = scoreboard.registerNewTeam(rankId);

            team.setPrefix(CC.translate(rank.getValue().getPrefix())); // extra space for spacing between rank and name
            team.setColor(rank.getValue().getNameColor());
            team.setSuffix(CC.translate("&r"));

            // make sure collisions never happen
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

            getRiseCore().getLogger().info("Loaded rank team " + team.getName() + " for rank " + rank.getKey().toUpperCase());

            addRankTeam(rank.getKey().toUpperCase(), team);
        }
    }

    public Team getRankTeam(String rankId) {
        return getRankTeams().getOrDefault(rankId, null);
    }

    public void addRankTeam(String rankId, Team team) {
        getRankTeams().put(rankId, team);
    }

}
