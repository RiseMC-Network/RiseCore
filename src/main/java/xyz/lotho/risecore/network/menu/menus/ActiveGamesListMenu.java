package xyz.lotho.risecore.network.menu.menus;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.util.Menu;
import xyz.lotho.risecore.network.util.ItemBuilder;
import xyz.lotho.risecore.network.util.TimeUtil;

@Getter
public class ActiveGamesListMenu extends Menu {

    private final RiseCore riseCore;

    public ActiveGamesListMenu(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Override
    public String getMenuName() {
        return "Active Games";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void updateItems() {
        for (int i = 0; i < 9; i++) {
            if (i == 4) getInventory().setItem(i, new ItemBuilder(Material.LECTERN).setDisplayname("&6View active games").build());
            else getInventory().setItem(i, getFillerGlass());
        }

        if (getRiseCore().getGameManager().getGlobalGameDataManager().getGlobalGameData().isEmpty()) {
            ItemStack noGames = new ItemBuilder(Material.RED_BANNER)
                    .setDisplayname("&cNo games found!")
                    .lore(
                            " ",
                            "&7There are no games currently running.",
                            "&7Start one by joining a game queue!"
                    )
                    .build();

            getInventory().setItem(31, noGames);
        } else {
            getRiseCore().getGameManager().getGlobalGameDataManager().getGlobalGameData().forEach((id, game) -> {
                ItemStack gameIcon = new ItemBuilder(game.getGameType().getGameIcon())
                        .setDisplayname("&egame-" + game.getGameId())
                        .lore(
                                "&7State: &d" + game.getGameState().name(),
                                " ",
                                "&7Type: &f" + game.getGameType().name(),
                                "&7Server: &f" + game.getServerId(),
                                "&7Players: &f" + game.getPlayers().size(),
                                "&7Time: " + TimeUtil.formatMillis(System.currentTimeMillis() - game.getStartTime()),
                                "",
                                "&7(Click to travel to game)"
                        )
                        .flag(ItemFlag.HIDE_ATTRIBUTES)
                        .build();

                getInventory().addItem(gameIcon);
            });
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {

    }
}
