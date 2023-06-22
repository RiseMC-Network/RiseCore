package xyz.lotho.risecore.network.manager.events;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.events.listener.*;

import java.util.ArrayList;

@Getter
public class ServerEventsManager {

    private final RiseCore riseCore;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    public ServerEventsManager(RiseCore riseCore) {
        this.riseCore = riseCore;

        addListener(new ConnectionListener(riseCore));
        addListener(new RestrictionListener(riseCore));
        addListener(new EnvironmentListener(riseCore));
        addListener(new ServerChatListener(riseCore));
        addListener(new InventoryClickListener(riseCore));

        loadListeners();
    }

    public void addListener(Listener listener) {
        getListeners().add(listener);
    }

    public void removeListener(Listener listener) {
        getListeners().remove(listener);
    }

    public void reloadListeners() {
        getListeners().forEach(HandlerList::unregisterAll);
        getListeners().forEach(listener -> getRiseCore().getServer().getPluginManager().registerEvents(listener, getRiseCore()));
    }

    public void loadListeners() {
        getListeners().forEach(listener -> getRiseCore().getServer().getPluginManager().registerEvents(listener, getRiseCore()));
    }

    public void unloadListeners() {
        getListeners().forEach(HandlerList::unregisterAll);
    }

}
