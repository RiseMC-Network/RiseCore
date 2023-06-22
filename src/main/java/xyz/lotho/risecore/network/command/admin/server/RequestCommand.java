package xyz.lotho.risecore.network.command.admin.server;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.player.RequestPlayerPacket;
import xyz.lotho.risecore.network.util.CC;

@Getter
@CommandAlias("request|req")
@CommandPermission("risecore.admin")
public class RequestCommand extends BaseCommand {

    private final RiseCore riseCore;

    public RequestCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Request players from anywhere on the network.")
    public void request(CommandSender sender, String playerNames) {
        String[] names = playerNames.split(",");

        for (String name : names) {
            RequestPlayerPacket requestPlayerPacket = new RequestPlayerPacket(
                    name,
                    getRiseCore().getServerId(),
                    sender instanceof Player ? sender.getName() : "CONSOLE"
            );

            getRiseCore().getRedisManager().sendPacket(requestPlayerPacket, false);
        }

        sender.sendMessage(CC.GREEN + "Sent request for " + names.length + " players.");
    }

}
