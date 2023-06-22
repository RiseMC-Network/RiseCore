package xyz.lotho.risecore.network.database.redis.packet.util;

public abstract class Packet {

    public abstract String getPacketName();

    public abstract void onReceive();
}
