package xyz.lotho.risecore.network.command.staff.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.database.redis.packet.staff.StaffChatPacket;

@Getter
@CommandAlias("staffchat|sc")
@CommandPermission("risecore.staff")
public class StaffChatCommand extends BaseCommand {

    private final RiseCore riseCore;

    public StaffChatCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Send a message into staff chat.")
    public void sendChat(CommandSender sender, String message) {
        StaffChatPacket staffChatPacket = new StaffChatPacket(getRiseCore().getServerId(), sender.getName(), message);
        getRiseCore().getRedisManager().sendPacket(staffChatPacket, false);
    }

}
