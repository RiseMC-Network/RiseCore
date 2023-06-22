package xyz.lotho.risecore.game.duels.team;

import lombok.Getter;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.duels.player.DuelsPlayer;
import xyz.lotho.risecore.network.RiseCore;

import java.util.*;

@Getter
public class DuelsTeamManager {

    private final RiseCore riseCore;
    private final DuelGame duelGame;

    public HashMap<String, DuelsTeam> teams = new HashMap<>();

    public DuelsTeamManager(RiseCore riseCore, DuelGame duelGame) {
        this.riseCore = riseCore;
        this.duelGame = duelGame;
    }

    public void loadTeams() {
        for (DuelsTeams team : DuelsTeams.values()) {
            teams.put(team.getName(), new DuelsTeam(team, duelGame));
        }

        int splitPoint = getDuelGame().getGamePlayerManager().getGamePlayers().size() / 2;

        ArrayList<DuelsPlayer> players = new ArrayList<>(getDuelGame().getGamePlayerManager().getGamePlayers().values());
        Collections.shuffle(players);

        ArrayList<DuelsPlayer> redPlayers = new ArrayList<>(players.subList(0, splitPoint));
        ArrayList<DuelsPlayer> bluePlayers = new ArrayList<>(players.subList(splitPoint, players.size()));

        redPlayers.forEach(player -> player.setDuelsTeam(getTeam("red")));
        bluePlayers.forEach(player -> player.setDuelsTeam(getTeam("blue")));

        getTeam("red").setTeamPlayers(redPlayers);
        getTeam("blue").setTeamPlayers(bluePlayers);
    }

    public DuelsTeam getTeam(String teamId) {
        return getTeams().getOrDefault(teamId, null);
    }

    public ArrayList<DuelsTeam> getAliveTeams() {
        ArrayList<DuelsTeam> duelsTeams = new ArrayList<>();

        this.getTeams().forEach((teamName, team) -> {
            if (team.getAliveMembers().size() > 0) duelsTeams.add(team);
        });

        return duelsTeams;
    }

    public DuelsTeam getWinningTeam() {
        return getAliveTeams().get(0);
    }

}
