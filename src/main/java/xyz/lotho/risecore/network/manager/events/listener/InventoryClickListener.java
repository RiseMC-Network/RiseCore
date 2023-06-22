package xyz.lotho.risecore.network.manager.events.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.util.Menu;

public class InventoryClickListener implements Listener {

    private final RiseCore riseCore;

    public InventoryClickListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Menu && event.getCurrentItem() != null) {
            event.setCancelled(true);
            ((Menu) holder).handleClick(event);
        }

    }

}
