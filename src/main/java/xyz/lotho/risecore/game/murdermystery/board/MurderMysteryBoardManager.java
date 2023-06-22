package xyz.lotho.risecore.game.murdermystery.board;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.GameState;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.murdermystery.game.MurderMysteryGame;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.TimeUtil;

import java.util.*;

@Getter
public class MurderMysteryBoardManager {

    private final RiseCore riseCore;
    private final MurderMysteryGame murderMysteryGame;

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public MurderMysteryBoardManager(RiseCore riseCore, MurderMysteryGame murderMysteryGame) {
        this.riseCore = riseCore;
        this.murderMysteryGame = murderMysteryGame;
    }

    public FastBoard getBoard(Player player) {
        return getBoards().getOrDefault(player.getUniqueId(), new FastBoard(player));
    }

    public void updateBoards() {
        getMurderMysteryGame().getGamePlayers().forEach(player -> {
            FastBoard board = getBoard(player);
            board.updateTitle(CC.translate("&aMurder Mystery"));

            Collection<String> lines = new ArrayList<>();
            lines.add(CC.translate("&m----------------"));
            lines.add("");
            lines.add(CC.translate("&7Role:"));
            lines.add(CC.translate(" &7- &a" + getMurderMysteryGame().getMurderMysteryPlayerManager().getPlayer(player.getUniqueId()).getRole().getRoleName()));
            lines.add("");
            lines.add(CC.translate("&7Players left: " + getMurderMysteryGame().getMurderMysteryPlayerManager().getAlivePlayers().size()));
            lines.add("");
            lines.add(CC.translate("&dTime: " + TimeUtil.formatMillis(getMurderMysteryGame().getGameTime())));
            lines.add("");
            lines.add(CC.translate("&m----------------"));

            board.updateLines(lines);
        });
    }

    public void destroyBoards() {
        getBoards().forEach((uuid, board) -> board.delete());
        getBoards().clear();
    }
}