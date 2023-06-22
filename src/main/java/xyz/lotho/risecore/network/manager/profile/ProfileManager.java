package xyz.lotho.risecore.network.manager.profile;

import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.Tasks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ProfileManager {

    private final RiseCore riseCore;

    private final Map<UUID, Profile> profiles = new ConcurrentHashMap<>();

    public ProfileManager(RiseCore riseCore) {
        this.riseCore = riseCore;

        Tasks.runAsyncTimer(() -> {
            for (Map.Entry<UUID, Profile> entry : getProfiles().entrySet()) {
                saveProfile(entry.getValue());
            }
        }, 0, 20 * 900); // every 15 mins
    }

    public Profile loadProfile(UUID uuid, String username) {
        long startTime = System.currentTimeMillis();

        Profile profile = new Profile(uuid, username);
        String loadedWith = "";

        if (getRiseCore().getRedisManager().getProfileCacheManager().isCached(uuid)) {
            profile.update(getRiseCore().getRedisManager().getProfileCacheManager().getProfile(uuid));
            loadedWith = "Redis Cache";
        } else {
            Document document = getRiseCore().getMongoManager().getProfilesCollection().find(new Document("uuid", uuid.toString())).first();
            if (document != null && !document.isEmpty()) {
                if (!username.equals(document.get("username"))) document.put("username", username);
                profile.update(getRiseCore().getGson().fromJson(document.toJson(), JsonObject.class));
                loadedWith = "Mongo";
            } else {
                loadedWith = "New Profile";
            }
        }

        getRiseCore().getLogger().info("Loaded profile for " + username + " in " + (System.currentTimeMillis() - startTime) + "ms from " + loadedWith + ".");

        saveProfile(profile);
        getProfiles().put(uuid, profile);

        return profile;
    }

    public void saveProfile(Profile profile) {
        Tasks.runAsync(() -> {
            getRiseCore().getRedisManager().getProfileCacheManager().cacheProfile(profile);
            getRiseCore().getMongoManager().getProfilesCollection().replaceOne(
                    Filters.eq("uuid", profile.getUuid().toString()), Document.parse(profile.toJson().toString()), new ReplaceOptions().upsert(true)
            );
        });
    }

    public Profile getProfile(String username) {
        for (Map.Entry<UUID, Profile> entry : getProfiles().entrySet()) {
            if (entry.getValue().getUsername().equalsIgnoreCase(username)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public Profile getProfile(UUID uuid) {
        return getProfiles().get(uuid);
    }

    public void removeProfile(UUID uuid) {
        getProfiles().remove(uuid);
    }

}
