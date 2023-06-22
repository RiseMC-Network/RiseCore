package xyz.lotho.risecore.network.command.admin.game;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.menus.ActiveGamesListMenu;

@Getter
@CommandAlias("game|games")
@CommandPermission("risecore.admin")
public class GamesCommand extends BaseCommand {

    private final RiseCore riseCore;

    public GamesCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("See all active games on the network.")
    public void seeGames(Player player) {
        new ActiveGamesListMenu(getRiseCore()).open(player);
    }
}
