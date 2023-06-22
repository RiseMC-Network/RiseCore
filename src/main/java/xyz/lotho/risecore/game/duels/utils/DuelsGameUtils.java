package xyz.lotho.risecore.game.duels.utils;

import xyz.lotho.risecore.game.duels.player.DuelsPlayer;
import xyz.lotho.risecore.game.duels.team.DuelsTeam;
import xyz.lotho.risecore.network.util.CC;

public class DuelsGameUtils {

    public static String winningMessage(DuelsTeam winningDuelsTeam) {
        StringBuilder line = new StringBuilder();

        line.append(CC.translate("\n&c&lGAME OVER!\n"));
        line.append(winningDuelsTeam.getTeam().getColor()).append(winningDuelsTeam.getTeam().name()).append(CC.translate(" &fhas won the game!\n "));
        line.append(CC.translate("\n&aCongratulations\n"));

        for (DuelsPlayer teamPlayer : winningDuelsTeam.getTeamPlayers()) {
            line.append(CC.translate(" &7• &f" + teamPlayer.getPlayer().getName() + " &7(" + teamPlayer.getKills() + " kills)"));
        }

        return line.append("\n ").toString();
    }

}
