package xyz.lotho.risecore.game.duels.kit.kits;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.duels.kit.util.Kit;
import xyz.lotho.risecore.game.duels.kit.util.KitType;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.ItemBuilder;

@Getter
public class DefaultKit extends Kit {

    private final RiseCore riseCore;

    public DefaultKit(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Override
    public KitType getName() {
        return KitType.DEFAULT;
    }

    @Override
    public void apply(Player player) {
        player.getInventory().clear();

        player.getInventory().setHelmet(new ItemBuilder(Material.NETHERITE_HELMET).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.NETHERITE_CHESTPLATE).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.NETHERITE_LEGGINGS).build());
        player.getInventory().setBoots(new ItemBuilder(Material.NETHERITE_BOOTS).build());

        player.getInventory().setItem(0, new ItemBuilder(Material.NETHERITE_SWORD).build());
        player.getInventory().setItem(1, new ItemBuilder(Material.BOW).build());
        player.getInventory().setItem(2, new ItemBuilder(Material.ARROW).amount(32).build());
        player.getInventory().setItem(3, new ItemBuilder(Material.COOKED_BEEF).amount(32).build());
    }
}
