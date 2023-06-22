package xyz.lotho.risecore.network.command.staff.game;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.menus.GamesListMenu;

@Getter
@CommandAlias("startgame")
@CommandPermission("risecore.staff")
public class StartGameCommand extends BaseCommand {

    private final RiseCore riseCore;

    public StartGameCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Force start a game.")
    public void startGame(Player player) {
        new GamesListMenu(getRiseCore()).open(player);
    }

}
