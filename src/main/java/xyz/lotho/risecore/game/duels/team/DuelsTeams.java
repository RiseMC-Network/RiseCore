package xyz.lotho.risecore.game.duels.team;

import lombok.Getter;
import xyz.lotho.risecore.network.util.CC;

@Getter
public enum DuelsTeams {

    RED("red", CC.RED),
    BLUE("blue", CC.BLUE);

    private final String name;
    private final String color;

    DuelsTeams(String name, String color) {
        this.name = name;
        this.color = color;
    }

}
