package xyz.lotho.risecore.game.duels.kit.util;

import org.bukkit.entity.Player;

public abstract class Kit {

    public abstract KitType getName();

    public abstract void apply(Player player);

}
