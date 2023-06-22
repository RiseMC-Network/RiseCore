package xyz.lotho.risecore.game.murdermystery.events.listener;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.game.murdermystery.player.MurderMysteryPlayer;
import xyz.lotho.risecore.game.murdermystery.role.GameRole;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;

@Getter
public class MurderMysteryGameDeathListener implements Listener {

    private final RiseCore riseCore;

    public MurderMysteryGameDeathListener(RiseCore riseCore) {
        this.riseCore = riseCore;
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent event) {
        // handle detective shooting bow
        if (event.getDamager() instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player killer)) return;
            if (!(event.getEntity() instanceof Player victim)) return;

            MurderMysteryGame murderMysteryGame = getMurderMysteryGameByPlayer(killer);
            if (murderMysteryGame == null) return;

            MurderMysteryPlayer killerPlayer = getGamePlayer(murderMysteryGame, killer);
            MurderMysteryPlayer victimPlayer = getGamePlayer(murderMysteryGame, victim);

            if (victim.getName().equals(killer.getName())) {
                return;
            }
            else if (victimPlayer.getRole() == GameRole.INNOCENT || victimPlayer.getRole() == GameRole.DETECTIVE) {
                victimPlayer.kill();
                killerPlayer.kill();
                killerPlayer.addKill();

                murderMysteryGame.announce("&c&lKILL &8> " + killerPlayer.getRole().getRoleName() + " &f" + killer.getName() + " &7has been killed!");
                murderMysteryGame.announce("&c&lKILL &8> " + victimPlayer.getRole().getRoleName() + " &f" + victim.getName() + " &7has been killed!");
                murderMysteryGame.playMassSound(Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                return;
            }
            else if (victimPlayer.getRole() == GameRole.MURDERER) {
                victimPlayer.kill();
                killerPlayer.addKill();
                murderMysteryGame.announce("&c&lKILL &8> " + victimPlayer.getRole().getRoleName() + " &f" + victim.getName() + " &7has been killed!");
                murderMysteryGame.playMassSound(Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                return;
            }
        }

        // handle murderer killing
        if (!(event.getDamager() instanceof Player killer)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        MurderMysteryGame murderMysteryGame = getMurderMysteryGameByPlayer(killer);
        if (murderMysteryGame == null) return;

        event.setCancelled(true);

        MurderMysteryPlayer killerPlayer = getGamePlayer(murderMysteryGame, killer);
        MurderMysteryPlayer victimPlayer = getGamePlayer(murderMysteryGame, victim);

        // Ensure person killing is the murderer
        if (killer.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD && killerPlayer.getRole() == GameRole.MURDERER) {
            victimPlayer.kill();
            killerPlayer.addKill();

            murderMysteryGame.announce(CC.translate("&c&lKILL &8> " + victimPlayer.getRole().getRoleName() + " &f" + victim.getName() + " &7has been killed!"));
            murderMysteryGame.playMassSound(Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
        }
    }

    private MurderMysteryGame getMurderMysteryGameByPlayer(Player player) {
        return (MurderMysteryGame) riseCore.getGameManager().findGameByPlayer(player);
    }

    private MurderMysteryPlayer getGamePlayer(MurderMysteryGame murderMysteryGame, Player player) {
        return murderMysteryGame.getMurderMysteryPlayerManager().getPlayer(player.getUniqueId());
    }

}
