package xyz.lotho.risecore.network.command.admin.server;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.menus.ServerListMenu;

@Getter
@CommandAlias("servermanager|sm")
@CommandPermission("risecore.admin")
public class ServerManagerCommand extends BaseCommand {

    private final RiseCore riseCore;

    public ServerManagerCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("View all active servers on the network.")
    public void server(Player player) {
        new ServerListMenu(getRiseCore()).open(player);
    }
}
