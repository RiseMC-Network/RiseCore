package xyz.lotho.risecore.network.command.admin.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.profile.Profile;
import xyz.lotho.risecore.network.manager.rank.Rank;
import xyz.lotho.risecore.network.util.CC;

import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@Setter
@CommandAlias("setrank")
@CommandPermission("risecore.admin")
public class SetRankCommand extends BaseCommand {

    private final RiseCore riseCore;

    public SetRankCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Set a player's rank.")
    public void setRank(CommandSender sender, String target, @Single String rankName) {
        getRiseCore().getServer().getScheduler().runTaskAsynchronously(getRiseCore(), () -> {
            Document document = getRiseCore().getMongoManager().getProfilesCollection().find()
                    .filter(Filters.regex("username", "^" + Pattern.quote(target) + "$", "i")).first();

            if (document == null) {
                sender.sendMessage(CC.RED + "Player does not exist.");
                return;
            }

            UUID uuid = UUID.fromString(document.getString("uuid"));

            Rank rank = getRiseCore().getRankManager().getRank(rankName);
            if (rank == null) {
                sender.sendMessage(CC.RED + "Rank does not exist.");
                return;
            }

            Profile profile = getRiseCore().getProfileManager().getProfile(target);

            if (profile == null) {
                document.put("rank", rank.getId().toUpperCase());

                getRiseCore().getMongoManager().getProfilesCollection().replaceOne(
                        Filters.regex("username", "^" + Pattern.quote(target) + "$", "i"), document, new ReplaceOptions().upsert(true)
                );

                // handle deletion of cached profile in redis as it has been updated and profile no longer exists
                if (getRiseCore().getRedisManager().getProfileCacheManager().isCached(uuid)) {
                    getRiseCore().getRedisManager().getProfileCacheManager().unCacheProfile(uuid);
                }

            } else {
                Player player = getRiseCore().getServer().getPlayer(profile.getUuid());

                if (player != null) {
                    profile.setRank(rank);
                    getRiseCore().getProfileManager().saveProfile(profile);

                    if (!sender.getName().equals(player.getName())) {
                        player.sendMessage(CC.GREEN + "Your rank has been updated to " + CC.WHITE + rank.getId().toUpperCase() + CC.GREEN + ".");
                    }
                }
            }

            sender.sendMessage(CC.GREEN + "Successfully set " + document.get("username") + "'s rank to " + CC.WHITE + rank.getId().toUpperCase() + CC.GREEN + ".");
        });
    }

}
