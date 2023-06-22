package xyz.lotho.risecore.game.duels.board;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.risecore.game.duels.game.DuelGame;
import xyz.lotho.risecore.game.GameState;
import xyz.lotho.risecore.network.RiseCore;
import xyz.lotho.risecore.network.util.CC;
import xyz.lotho.risecore.network.util.TimeUtil;

import java.util.*;

@Getter
public class DuelsBoardManager {

    private final RiseCore riseCore;
    private final DuelGame duelGame;

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public DuelsBoardManager(RiseCore riseCore, DuelGame duelGame) {
        this.riseCore = riseCore;
        this.duelGame = duelGame;
    }

    public FastBoard getBoard(Player player) {
        return getBoards().getOrDefault(player.getUniqueId(), new FastBoard(player));
    }

    public void updateBoards() {
        getDuelGame().getGamePlayers().forEach(player -> {
            FastBoard board = getBoard(player);
            board.updateTitle(CC.translate("&aMatch"));

            Collection<String> lines = new ArrayList<>();
            lines.add(CC.translate("&m----------------"));
            lines.add("");

            lines.addAll(getDuelGame().getGamePlayers().stream()
                    .limit(6)
                    .map(limited -> {
                        String name = limited.getName().equals(player.getName()) ? " &7• &a" + limited.getName() : " &7• &c" + limited.getName();
                        return CC.translate(name + " &7" + (int) limited.getHealth() + " &c❤");
                    })
                    .toList());

            lines.add("");
            lines.add(CC.translate("&dTime: " + (getDuelGame().getGameState() == GameState.GRACE ? "&fGrace Period" : TimeUtil.formatMillis(System.currentTimeMillis() - getDuelGame().getStartTime()))));
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
