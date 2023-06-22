package xyz.lotho.risecore.game.murdermystery.utils;

import xyz.lotho.risecore.game.duels.player.DuelsPlayer;
import xyz.lotho.risecore.game.duels.team.DuelsTeam;
import xyz.lotho.risecore.game.murdermystery.player.MurderMysteryPlayer;
import xyz.lotho.risecore.game.murdermystery.player.MurderMysteryPlayerManager;
import xyz.lotho.risecore.game.murdermystery.role.GameRole;
import xyz.lotho.risecore.network.util.CC;

public class MurderMysteryUtils {

    public static String winningMessage(GameRole winningRole, MurderMysteryPlayerManager murderMysteryPlayerManager) {
        StringBuilder line = new StringBuilder();

        String winningRoleName = winningRole == GameRole.INNOCENT ? winningRole.getRoleName() + "'s" : winningRole.getRoleName();

        line.append("\n&c&lGAME OVER!\n");
        line.append(winningRoleName).append(" &7has won the game!\n ");
        line.append("\n&aCongratulations\n");

        if (winningRole == GameRole.MURDERER) {
            MurderMysteryPlayer murderer = murderMysteryPlayerManager.getMurderer();
            line.append(" &7• &f").append(murderer.getPlayer().getName()).append(" &7(").append(murderer.getKills()).append(" kills)");
        } else {
            line.append(" &7• &aSurvivors: &7");
            for (MurderMysteryPlayer teamPlayer : murderMysteryPlayerManager.getAllAlivePlayers()) {
                line.append(teamPlayer.getPlayer().getName()).append(" ");
            }
        }

        return line.append("\n ").toString();
    }

}
