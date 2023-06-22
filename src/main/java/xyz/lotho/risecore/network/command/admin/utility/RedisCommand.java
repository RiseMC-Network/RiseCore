package xyz.lotho.risecore.network.command.admin.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import redis.clients.jedis.Jedis;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
@CommandAlias("redis")
@CommandPermission("risecore.admin")
public class RedisCommand extends BaseCommand {

    private final RiseCore riseCore;

    public RedisCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Subcommand("ping")
    @Description("View redis server information.")
    public void redis(CommandSender sender) {
        try (Jedis jedis = getRiseCore().getRedisManager().getJedisPool().getResource()) {
            long startTime = System.currentTimeMillis();
            String response = jedis.ping();
            long endTime = System.currentTimeMillis();

            sender.sendMessage(
                    CC.translate("&6&lRedis"),
                    CC.GREEN + " Latency: " + CC.WHITE + (endTime - startTime) + "ms",
                    CC.GREEN + " Status: " + CC.WHITE + (jedis.isConnected() ? "Connected" : "Disconnected"),
                    CC.GREEN + " Response: " + CC.WHITE + response
            );
        }
    }

    @Subcommand("purgecache|pc")
    @Description("Purges the profile cache on redis.")
    public void purgeCache(CommandSender sender) {
        this.riseCore.getRedisManager().getProfileCacheManager().purgeCache();
        sender.sendMessage(CC.GREEN + "You have purged the redis cache.");
    }

}
