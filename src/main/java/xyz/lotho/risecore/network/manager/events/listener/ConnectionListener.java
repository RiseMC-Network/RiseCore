package xyz.lotho.risecore.network.manager.events.listener;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.staff.StaffJoinPacket;
import xyz.lotho.risecore.network.database.redis.packet.staff.StaffLeavePacket;
import xyz.lotho.risecore.network.manager.profile.Profile;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.Tasks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class ConnectionListener implements Listener {

    private final RiseCore riseCore;

    public ConnectionListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        Profile profile = getRiseCore().getProfileManager().loadProfile(event.getUniqueId(), event.getName());

        if (profile == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.RED + "Error loading your profile!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Profile profile = getRiseCore().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        profile.calculatePermissions();

        Player player = event.getPlayer();

        if (player.hasPermission("risecore.staff")) {
            StaffJoinPacket staffJoinPacket = new StaffJoinPacket(getRiseCore().getServerId(), player.getName());
            getRiseCore().getRedisManager().sendPacket(staffJoinPacket, false);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("risecore.staff")) {
            StaffLeavePacket staffLeavePacket = new StaffLeavePacket(getRiseCore().getServerId(), player.getName());
            getRiseCore().getRedisManager().sendPacket(staffLeavePacket, false);
        }

        getRiseCore().getProfileManager().saveProfile(getRiseCore().getProfileManager().getProfile(player.getUniqueId()));
        getRiseCore().getProfileManager().removeProfile(player.getUniqueId());

        event.setQuitMessage(null);
    }

}
