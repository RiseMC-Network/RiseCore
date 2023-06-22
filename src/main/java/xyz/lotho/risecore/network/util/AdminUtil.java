package xyz.lotho.risecore.network.util;

import xyz.lotho.risecore.network.RiseCore;

public class AdminUtil {

    public static void announce(String message) {
        RiseCore.getInstance().getServer().getOnlinePlayers().forEach((player) -> {
            if (player.hasPermission("risecore.admin")) {
                player.sendMessage(CC.translate(message));
            }
        });
    }
}
