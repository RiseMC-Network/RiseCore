package xyz.lotho.risecore.network.menu.menus;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.menu.util.Menu;
import xyz.lotho.risecore.network.util.ItemBuilder;
import xyz.lotho.risecore.network.util.TimeUtil;

@Getter
public class PlayerInformationMenu extends Menu {

    private final RiseCore riseCore;
    private final JsonObject profileObject;

    public PlayerInformationMenu(RiseCore riseCore, JsonObject profileObject) {
        this.riseCore = riseCore;
        this.profileObject = profileObject;
    }

    @Override
    public String getMenuName() {
        return "Player Information";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void updateItems() {
        Server server = getRiseCore().getServerManager().findPlayer(profileObject.get("username").getAsString());

        ItemBuilder serverInfoBuilder = new ItemBuilder(Material.ENDER_EYE)
                .setDisplayname("&6Server");

        if (server == null) {
            serverInfoBuilder.lore("&fNone");
        } else {
            serverInfoBuilder.lore("&f" + server.getId());
        }

        ItemStack serverInfo = serverInfoBuilder.build();

        ItemStack playerInfo = new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayname("&6" + profileObject.get("username").getAsString())
                .lore(
                        "&eUUID: &f" + profileObject.get("uuid").getAsString(),
                        "&eRank: &f" + getRiseCore().getRankManager().getRank(profileObject.get("rank").getAsString()).getPrefix(),
                        "&eFirst Login: &f" + TimeUtil.formatMillis(System.currentTimeMillis() - profileObject.get("firstLogin").getAsLong()),
                        "&eLast Login: &f" + TimeUtil.formatMillis(System.currentTimeMillis() - profileObject.get("lastLogin").getAsLong())
                )
                .build();

        getInventory().setItem(11, serverInfo);
        getInventory().setItem(15, playerInfo);

        fillRemainingSlots();
    }

    @Override
    public void handleClick(InventoryClickEvent event) {}
}
