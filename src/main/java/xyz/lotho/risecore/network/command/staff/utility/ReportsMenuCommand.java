package xyz.lotho.risecore.network.command.staff.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.menus.ReportListMenu;

@CommandAlias("reports|rm")
@CommandPermission("risecore.staff")
public class ReportsMenuCommand extends BaseCommand {

    private final RiseCore riseCore;

    public ReportsMenuCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("View all active reports")
    public void seeReports(Player player) {
        new ReportListMenu(riseCore).open(player);
    }


}
