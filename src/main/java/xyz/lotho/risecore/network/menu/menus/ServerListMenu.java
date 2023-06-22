package xyz.lotho.risecore.network.menu.menus;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.util.Menu;
import xyz.lotho.risecore.network.util.BungeeUtil;
import xyz.lotho.risecore.network.util.ItemBuilder;

@Getter
public class ServerListMenu extends Menu {

    private final RiseCore riseCore;

    public ServerListMenu(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Override
    public String getMenuName() {
        return "Servers";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void updateItems() {
        if (getRiseCore().getServerManager().getServers().isEmpty()) {
            ItemStack noServers = new ItemBuilder(Material.RED_BANNER)
                    .setDisplayname("&cNo servers found!")
                    .lore(
                            " ",
                            "&7Start a server through the console."
                    )
                    .build();

            getInventory().setItem(22, noServers);
        } else {
            for (int i = 0; i < 9; i++) {
                if (i == 4) getInventory().setItem(i, new ItemBuilder(Material.LECTERN).setDisplayname("&6View active servers").build());
                else getInventory().setItem(i, getFillerGlass());
            }

            getRiseCore().getServerManager().getServersList().forEach(server -> {
                ItemStack serverIcon = new ItemBuilder(server.getType().getServerTypeIcon())
                        .setDisplayname("&e" + server.getId())
                        .lore(
                                "&7Status: " + server.getState().getColor() + server.getState().name(),
                                " ",
                                "&7Port: &f" + server.getPort(),
                                "&7Type: &f" + server.getType().name(),
                                "&7Players: &f" + server.getPlayers().size() + "/" + server.getMaxPlayers(),
                                "&7Last Updated: &f" + server.getLastUpdatedPretty(),
                                "",
                                "&7Version: &f" + server.getVersion(),
                                "",
                                "&7(Click to travel to server)"
                        )
                        .flag(ItemFlag.HIDE_ATTRIBUTES)
                        .build();

                getInventory().addItem(serverIcon);
            });
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if (event.getSlot() <= 8 || item == null || item.getType() == Material.AIR) return;

        String serverId = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        BungeeUtil.sendPlayerToServer((Player) event.getWhoClicked(), serverId);
    }
}
