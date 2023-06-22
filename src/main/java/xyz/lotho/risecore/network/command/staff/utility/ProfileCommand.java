package xyz.lotho.risecore.network.command.staff.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.menu.menus.PlayerInformationMenu;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.Tasks;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Getter
@CommandAlias("profile|pi")
@CommandPermission("risecore.staff")
public class ProfileCommand extends BaseCommand {

    private final RiseCore riseCore;

    public ProfileCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("View your profile or another player's profile.")
    public void profile(Player player, @Optional String target) {
        CompletableFuture<JsonObject> cf = new CompletableFuture<>();

        Tasks.runAsync(() -> {
            if (target == null) {
                cf.complete(getRiseCore().getProfileManager().getProfile(player.getUniqueId()).toJson());
            } else {
                Document document = getRiseCore().getMongoManager().getProfilesCollection().find()
                        .filter(Filters.regex("username", "^" + Pattern.quote(target) + "$", "i")).first();

                if (document == null) {
                    cf.completeExceptionally(new Exception(CC.RED + "Player does not exist."));
                } else {
                    cf.complete(getRiseCore().getGson().fromJson(document.toJson(), JsonObject.class));
                }
            }
        });

        cf.whenComplete((profileObject, throwable) -> {
            if (throwable != null) {
                player.sendMessage(throwable.getMessage());
                return;
            }

            Bukkit.getServer().getScheduler().runTaskLater(getRiseCore(), () -> {
                new PlayerInformationMenu(getRiseCore(), profileObject).open(player);
            }, 5L);
        });
    }

}
