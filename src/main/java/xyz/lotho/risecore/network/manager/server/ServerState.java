package xyz.lotho.risecore.network.manager.server;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import xyz.lotho.risecore.network.util.CC;

public enum ServerState {

    ONLINE(Material.EMERALD, CC.GREEN),
    LOCKED(Material.REDSTONE, CC.GOLD),
    OFFLINE(Material.BARRIER, CC.RED);

    private final Material serverIcon;
    private final String color;

    ServerState(Material serverIcon, String color) {
        this.serverIcon = serverIcon;
        this.color = color;
    }

    public Material getServerIcon() {
        return this.serverIcon;
    }

    public String getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return this.name().charAt(0) /*Capital first letter*/+ this.name().substring(1).toLowerCase() /*Rest letters lowercase*/;
    }

}
