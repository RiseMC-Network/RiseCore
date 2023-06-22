package xyz.lotho.risecore.game.util;

import org.bukkit.Material;

public enum GameType {

    DUELS(Material.DIAMOND_SWORD, 2),
    BEDWARS(Material.RED_BED, 2),
    MURDER_MYSTERY(Material.BOW, 2);

    private final Material gameIcon;
    private final int minPlayers;

    GameType(Material gameIcon, int minPlayers) {
        this.gameIcon = gameIcon;
        this.minPlayers = minPlayers;
    }

    public Material getGameIcon() {
        return gameIcon;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

}
