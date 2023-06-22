package xyz.lotho.risecore.network.database.redis.manager;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.cache.ProfileCacheManager;
import xyz.lotho.risecore.network.database.redis.packet.util.Packet;

import java.util.concurrent.ForkJoinPool;

@Getter
public class RedisManager {

    private final RiseCore riseCore;
    private final ProfileCacheManager profileCacheManager;

    private final String username;
    private final String password;
    private final String address;
    private final int port;
    private final String channel;

    private JedisPool jedisPool;
    private JedisPoolConfig jedisPoolConfig;
    private SubscriptionManager subscriber;

    public RedisManager(RiseCore riseCore) throws InterruptedException {
        this.riseCore = riseCore;
        this.profileCacheManager = new ProfileCacheManager(getRiseCore());

        this.username = getRiseCore().getConfigurationFile().get().getString("database.redis.username");;
        this.password = getRiseCore().getConfigurationFile().get().getString("database.redis.password");;
        this.address = getRiseCore().getConfigurationFile().get().getString("database.redis.address");
        this.port = getRiseCore().getConfigurationFile().get().getInt("database.redis.port");
        this.channel = "risecore-main";

        load();
    }

    public void load() {
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);

        // Load the jedis connection pool
        jedisPool = new JedisPool(
                getJedisPoolConfig(),
                "redis://" + getUsername() + ":" + getPassword() + "@" + getAddress() + ":" + getPort()
        );

        // Load the subscription handler
        subscriber = new SubscriptionManager(getRiseCore());

        // Asynchronously subscribe to the channel
        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedisSubscriber = getJedisPool().getResource()) {
                jedisSubscriber.subscribe(getSubscriber(), getChannel());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendPacket(Packet packet, boolean async) {
        String message = packet.getClass().getName() + "::" + getRiseCore().getGson().toJson(packet);

        try (Jedis jedis = getJedisPool().getResource()) {
            if (async) getRiseCore().getServer().getScheduler().runTaskAsynchronously(getRiseCore(), () -> jedis.publish(getChannel(), message));
            else jedis.publish(getChannel(), message);
        }
    }
    public void close() {
        try {
            if (getSubscriber() != null) getSubscriber().unsubscribe(getChannel());
            if (getJedisPool() != null) getJedisPool().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
