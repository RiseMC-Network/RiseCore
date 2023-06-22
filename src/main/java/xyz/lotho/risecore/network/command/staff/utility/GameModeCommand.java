package xyz.lotho.risecore.network.command.staff.utility;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@CommandAlias("gamemode|gm")
@CommandPermission("risecore.staff")
public class GameModeCommand extends BaseCommand {

    private final RiseCore riseCore;

    public GameModeCommand(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @Default
    @Description("Update your gamemode.")
    public void setGameMode(Player player, String gamemode) {
        GameMode newGameMode;

        switch (gamemode) {
            case "creative", "c", "1" -> newGameMode = GameMode.CREATIVE;
            case "survival", "s", "0" -> newGameMode = GameMode.SURVIVAL;
            case "adventure", "a", "2" -> newGameMode = GameMode.ADVENTURE;
            case "spectator", "sp", "3" -> newGameMode = GameMode.SPECTATOR;
            default -> {
                player.sendMessage(CC.RED + "Invalid gamemode!");
                return;
            }
        }

        player.setGameMode(newGameMode);
        player.sendMessage(CC.GREEN + "Your gamemode has been updated to " + CC.WHITE + newGameMode.name().toLowerCase() + CC.GREEN + ".");
    }

}
