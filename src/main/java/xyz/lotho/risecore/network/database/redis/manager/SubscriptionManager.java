package xyz.lotho.risecore.network.database.redis.manager;

import lombok.Getter;
import redis.clients.jedis.JedisPubSub;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;

@Getter
public class SubscriptionManager extends JedisPubSub {

    private final RiseCore riseCore;

    public SubscriptionManager(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Override
    public void onMessage(String channel, String message) {
        // getRiseCore().getLogger().log(Level.INFO, "RECEIVED PACKET: " + message);

        int index = message.indexOf("::");
        String packetName = message.substring(0, index);
        String packetContent = message.substring(index + 2);

        try {
            Packet packet = (Packet) getRiseCore().getGson().fromJson(packetContent, Class.forName(packetName));
            packet.onReceive();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
