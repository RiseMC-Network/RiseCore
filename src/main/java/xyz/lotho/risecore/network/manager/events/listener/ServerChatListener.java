package xyz.lotho.risecore.network.manager.events.listener;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.manager.profile.Profile;
import xyz.lotho.risecore.network.util.CC;

@Getter
public class ServerChatListener implements Listener {

    private final RiseCore riseCore;

    public ServerChatListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        Profile profile = getRiseCore().getProfileManager().getProfile(player.getUniqueId());

        String message = event.getMessage();
        String prefix = CC.translate(profile.getRank().getPrefix());

        ChatColor nameColor = profile.getRank().getNameColor();
        ChatColor chatColor = profile.getRank().getChatColor();

        event.getRecipients().forEach(recipient -> {
            recipient.sendMessage(prefix + nameColor + player.getName() + CC.DARK_GRAY + " » " + chatColor + message);
        });
    }

}
