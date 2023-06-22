package xyz.lotho.risecore.game.duels.events;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.lotho.risecore.game.duels.events.listeners.DuelsDisconnectListener;
import xyz.lotho.risecore.game.duels.events.listeners.DuelsEnvironmentListener;
import xyz.lotho.risecore.game.duels.events.listeners.DuelsGameDeathListener;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.network.RiseCore;

import java.util.ArrayList;

@Getter
public class DuelsEventsManager {

    private final RiseCore riseCore;
    private final DuelGame duelGame;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    public DuelsEventsManager(RiseCore riseCore, DuelGame duelGame) {
        this.riseCore = riseCore;
        this.duelGame = duelGame;

        addListener(new DuelsEnvironmentListener(riseCore));
        addListener(new DuelsGameDeathListener(riseCore));
        addListener(new DuelsDisconnectListener(riseCore));

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
