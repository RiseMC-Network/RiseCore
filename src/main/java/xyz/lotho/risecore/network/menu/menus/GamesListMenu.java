package xyz.lotho.risecore.network.menu.menus;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.risecore.game.GameType;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.game.GameStartPacket;
import xyz.lotho.risecore.network.manager.server.Server;
import xyz.lotho.risecore.network.menu.util.Menu;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.ItemBuilder;

import java.util.Random;

@Getter
public class GamesListMenu extends Menu {

    private final RiseCore riseCore;

    public GamesListMenu(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Override
    public String getMenuName() {
        return "Available Games";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void updateItems() {
        for (GameType game : GameType.values()) {
            ItemStack gameItem  = new ItemBuilder(game.getGameIcon())
                    .setDisplayname("&e" + game.name())
                    .lore("&7Click to force start this game type!")
                    .build();

            getInventory().addItem(gameItem);
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GameType gameType = GameType.valueOf(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

        Server gameServer = getRiseCore().getServerManager().getRandomGameServer();

        if (gameServer == null) {
            player.sendMessage(CC.RED + "No game servers found! Try again later.");
            player.closeInventory();
            return;
        }

        GameStartPacket gameStartPacket = new GameStartPacket(
                new Random().nextInt(10000) + 1,
                gameType,
                getRiseCore().getServer().getOnlinePlayers().stream().map(Player::getName).toList(),
                gameServer.getId()
        );

        getRiseCore().getRedisManager().sendPacket(gameStartPacket, false);
        player.sendMessage(CC.GREEN + "Starting game...");
        player.closeInventory();
    }
}
