package xyz.lotho.risecore.network.database.redis.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.profile.Profile;

import java.util.UUID;

@Getter
public class ProfileCacheManager {

    private final RiseCore riseCore;
    private final int expireTime;

    public ProfileCacheManager(RiseCore riseCore) {
        this.riseCore = riseCore;
        this.expireTime = 3600; // 1 hour
    }

    public void cacheProfile(Profile profile) {
        try (Jedis jedis = getRiseCore().getRedisManager().getJedisPool().getResource()) {
            jedis.set(profile.getUuid().toString(), profile.toJson().toString());
            jedis.expire(profile.getUuid().toString(), getExpireTime());
        }
    }

    public JsonObject getProfile(UUID uuid) {
        try (Jedis jedis = getRiseCore().getRedisManager().getJedisPool().getResource()) {
            return (JsonObject) JsonParser.parseString(jedis.get(uuid.toString()));
        }
    }

    public boolean isCached(UUID uuid) {
        try (Jedis jedis = getRiseCore().getRedisManager().getJedisPool().getResource()) {
            return jedis.exists(uuid.toString());
        }
    }

    public void unCacheProfile(UUID uuid) {
        try (Jedis jedis = getRiseCore().getRedisManager().getJedisPool().getResource()) {
            jedis.del(uuid.toString());
        }
    }

    public void purgeCache() {
        try (Jedis jedis = getRiseCore().getRedisManager().getJedisPool().getResource()) {
            jedis.flushAll();
        }
    }

}
