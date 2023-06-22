package xyz.lotho.risecore.network.manager.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.rank.Rank;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private final UUID uuid;

    private String username;
    private Rank rank;
    private int gold;
    private long firstLogin;
    private long lastLogin;

    public Profile(UUID uuid, String username) {
        this.uuid = uuid;

        this.username = username;
        this.rank = RiseCore.getInstance().getRankManager().getDefaultRank();
        this.gold = 0;
        this.firstLogin = System.currentTimeMillis();
        this.lastLogin = System.currentTimeMillis();
    }

    public void update(JsonObject json) {
        this.username = json.get("username").getAsString();
        this.rank = RiseCore.getInstance().getRankManager().getRank(json.get("rank").getAsString());
        this.gold = json.get("gold").getAsInt();
        this.firstLogin = json.get("firstLogin").getAsLong();
    }

    public void calculatePermissions() {
        revokePermissions();
        assignPermissions();

        // add player to rank team
        RiseCore.getInstance().getRankManager().getTabRankManager().getRankTeam(getRank().getId()).addEntry(getUsername());
    }

    public void assignPermissions() {
        Player player = RiseCore.getInstance().getServer().getPlayer(getUuid());

        if (player != null) {
            PermissionAttachment attachment = player.addAttachment(RiseCore.getInstance());
            getRank().getPermissions().forEach(permission -> attachment.setPermission(permission, true));
        }
    }

    public void revokePermissions() {
        Player player = RiseCore.getInstance().getServer().getPlayer(getUuid());

        if (player != null) {
            player.getEffectivePermissions().stream()
                    .filter(p -> p.getAttachment() != null && p.getAttachment().getPlugin() == RiseCore.getInstance())
                    .findFirst()
                    .map(PermissionAttachmentInfo::getAttachment)
                    .ifPresent(player::removeAttachment);
        }
    }

    public void setRank(Rank rank) {
        this.rank = rank;

        // re-calculate user permissions upon rank change
        calculatePermissions();

        // update redis cache with new information, database will save changes upon player logout
        RiseCore.getInstance().getRedisManager().getProfileCacheManager().cacheProfile(this);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(RiseCore.getInstance().getGson().toJson(this));

        // update json with rank name and not rank class
        jsonObject.addProperty("rank", getRank().getId());

        return jsonObject;
    }


}
