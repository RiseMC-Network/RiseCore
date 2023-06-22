package xyz.lotho.risecore.game.murdermystery.events;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.lotho.risecore.game.duels.events.listeners.DuelsDisconnectListener;
import xyz.lotho.risecore.game.duels.events.listeners.DuelsEnvironmentListener;
import xyz.lotho.risecore.game.duels.events.listeners.DuelsGameDeathListener;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.murdermystery.events.listener.*;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.network.RiseCore;

import java.util.ArrayList;

@Getter
public class MurderMysteryEventsManager {

    private final RiseCore riseCore;
    private final MurderMysteryGame murderMysteryGame;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    public MurderMysteryEventsManager(RiseCore riseCore, MurderMysteryGame murderMysteryGame) {
        this.riseCore = riseCore;
        this.murderMysteryGame = murderMysteryGame;

        addListener(new MurderMysteryGameDeathListener(riseCore));
        addListener(new MurderMysteryEnvironmentListener(riseCore));
        addListener(new MurderMysteryShootBowListener(riseCore));
        addListener(new MurderMysteryDamageEventListener(riseCore));
        addListener(new MurderMysteryInventoryMoveListener(riseCore));
        addListener(new MurderMysteryDisconnectListener(riseCore));

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
