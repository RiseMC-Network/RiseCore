package xyz.lotho.risecore.network.manager.server;

import org.bukkit.Material;

public enum ServerType {

    LOBBY(Material.SPYGLASS),
    GAME(Material.GOAT_HORN);

    private final Material serverTypeIcon;

    ServerType(Material serverTypeIcon) {
        this.serverTypeIcon = serverTypeIcon;
    }

    public Material getServerTypeIcon() {
        return this.serverTypeIcon;
    }

    @Override
    public String toString() {
        return this.name().charAt(0) /*Capital first letter*/+ this.name().substring(1).toLowerCase() /*Rest letters lowercase*/;
    }

}
