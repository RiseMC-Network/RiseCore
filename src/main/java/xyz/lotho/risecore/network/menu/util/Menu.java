package xyz.lotho.risecore.network.menu.util;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.ItemBuilder;

@Getter
public abstract class Menu implements InventoryHolder {

    private Inventory inventory = null;
    private final ItemStack fillerGlass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build();

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void updateItems();

    public abstract void handleClick(InventoryClickEvent event);

    public void open(Player player) {
         this.inventory = RiseCore.getInstance().getServer().createInventory(this, getSlots(), getMenuName());

        this.updateItems();
        // this.fillRemainingSlots();

        player.openInventory(getInventory());
    }

    public void fillRemainingSlots() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) inventory.setItem(i, fillerGlass);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
