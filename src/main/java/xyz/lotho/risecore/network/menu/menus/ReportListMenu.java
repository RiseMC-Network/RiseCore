package xyz.lotho.risecore.network.menu.menus;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.player.RequestPlayerPacket;
import xyz.lotho.risecore.network.database.redis.packet.staff.StaffTeleportPacket;
import xyz.lotho.risecore.network.manager.report.Report;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.menu.util.Menu;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.ItemBuilder;
import xyz.lotho.risecore.network.util.Tasks;
import xyz.lotho.risecore.network.util.TimeUtil;

@Getter
public class ReportListMenu extends Menu {

    private final RiseCore riseCore;

    public ReportListMenu(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Override
    public String getMenuName() {
        return "Reports";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void updateItems() {
        for (int i = 0; i < 9; i++) {
            if (i == 4)
                getInventory().setItem(i, new ItemBuilder(Material.LECTERN).setDisplayname("&6View active reports").build());
            else getInventory().setItem(i, getFillerGlass());
        }

        if (getRiseCore().getReportManager().getReports().isEmpty()) {
            ItemStack noReports = new ItemBuilder(Material.RED_BANNER)
                    .setDisplayname("&cNo reports found!")
                    .lore("&7Lets hope none come in!")
                    .build();

            getInventory().setItem(31, noReports);
        } else {
            for (Report report : getRiseCore().getReportManager().getReports()) {
                ItemStack reportItem = new ItemBuilder(Material.PLAYER_HEAD)
                        .setDisplayname("&a" + report.getReported() + " &7(" + TimeUtil.formatMillis(System.currentTimeMillis() - report.getReportTime()) + " ago)")
                        .lore("&7Server: &f" + report.getServerId(),
                                "&7Reporter: &f" + report.getReporter(),
                                "",
                                "&7Reason:",
                                "&f" + report.getReason(),
                                "",
                                "&7(Click to watch user)"
                        )
                        .build();

                getInventory().addItem(reportItem);
            }
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        String target = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]);
        Server server = getRiseCore().getServerManager().findPlayer(target);

        if (server == null) {
            player.sendMessage(CC.translate("&cThat player is not online anymore! &7Removing report..."));

            Report report = getRiseCore().getReportManager().getPlayerReports(target);
            getRiseCore().getReportManager().deleteReport(report);

            player.closeInventory();
            return;
        }

        RequestPlayerPacket requestPlayerPacket = new RequestPlayerPacket(player.getName(), server.getId(), "CONSOLE");
        getRiseCore().getRedisManager().sendPacket(requestPlayerPacket, false);

        Tasks.runLater(() -> {
            StaffTeleportPacket teleportPacket = new StaffTeleportPacket(player.getName(), target, server.getId());
            getRiseCore().getRedisManager().sendPacket(teleportPacket, false);
        }, 30L);

        player.closeInventory();
    }
}
