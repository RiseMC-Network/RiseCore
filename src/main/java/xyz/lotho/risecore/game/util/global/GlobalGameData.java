package xyz.lotho.risecore.game.util.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.lotho.risecore.game.util.GameState;
import xyz.lotho.risecore.game.util.GameType;

import java.util.List;

@Getter
@AllArgsConstructor
public class GlobalGameData {

    private final String serverId;
    private final int gameId;
    private final GameType gameType;
    private final GameState gameState;
    private final List<String> players;
    private final long startTime;

}
