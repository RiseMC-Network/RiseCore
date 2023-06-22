package xyz.lotho.risecore.network.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;

public class BungeeUtil {

    public static void sendPlayerToServer(Player player, String serverId) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverId);

        player.sendPluginMessage(RiseCore.getInstance(), "BungeeCord", out.toByteArray());
    }

}
