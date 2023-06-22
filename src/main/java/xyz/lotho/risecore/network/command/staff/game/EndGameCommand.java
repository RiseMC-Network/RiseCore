package xyz.lotho.risecore.network.command.staff.game;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.Game;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
@CommandAlias("endgame|end")
@CommandPermission("risecore.staff")
public class EndGameCommand extends BaseCommand {

    private final RiseCore riseCore;

    public EndGameCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Force end a game.")
    public void endGame(Player player) {
        Game game = getRiseCore().getGameManager().findGameByPlayer(player);

        if (game == null) {
            player.sendMessage(CC.RED + "You are not in a game!");
            return;
        }

        game.announce(CC.RED + "An administrator has force ended this game. Closing server...");
        getRiseCore().getServer().getScheduler().runTaskLater(getRiseCore(), game::endGame, 100L);
    }

}
