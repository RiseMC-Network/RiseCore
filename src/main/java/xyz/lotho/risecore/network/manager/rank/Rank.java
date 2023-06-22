package xyz.lotho.risecore.network.manager.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Rank {

    private final String id, prefix, nameColor, chatColor;
    private int priority;
    private final boolean defaultRank;
    private final List<String> inheritedRanks;
    private final Set<String> permissions;

    public ChatColor getNameColor() {
        return ChatColor.valueOf(nameColor);
    }

    public ChatColor getChatColor() {
        return ChatColor.valueOf(chatColor);
    }

}
