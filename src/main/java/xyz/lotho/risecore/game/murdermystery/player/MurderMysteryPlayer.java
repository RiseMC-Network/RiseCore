package xyz.lotho.risecore.game.murdermystery.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.murdermystery.role.GameRole;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.ItemBuilder;

import java.util.UUID;

@Getter
public class MurderMysteryPlayer {

    private final UUID uuid;

    private GameRole role = GameRole.INNOCENT;
    private boolean alive = true;
    private int kills = 0;

    public MurderMysteryPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRole(GameRole role) {
        this.role = role;
    }

    public Player getPlayer() {
        return RiseCore.getInstance().getServer().getPlayer(uuid);
    }

    public void kill() {
        Player player = getPlayer();

        if (player != null) {
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            alive = false;
        }
    }

    public void addKill() {
        this.kills++;
    }

    public void spawn(Location location) {
        Player player = getPlayer();

        if (player != null) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);

            if (getRole() == GameRole.DETECTIVE) {
                player.getInventory().setItem(4, new ItemBuilder(Material.BOW).setDisplayname("&aProtector").enchant(Enchantment.ARROW_INFINITE, 1).build());
                player.getInventory().setItem(17, new ItemBuilder(Material.ARROW).amount(1).build());
            }
            else if (getRole() == GameRole.MURDERER) {
                player.getInventory().setItem(4, new ItemBuilder(Material.IRON_SWORD).setDisplayname("&cSlayer").build());
            }

            player.sendTitle(getRole().getRoleName(), getRole().getRoleMessage(), 60, 120, 60);
            player.teleport(location);
        }

    }

}
