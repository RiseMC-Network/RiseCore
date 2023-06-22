package xyz.lotho.risecore.game.duels.kit;

import lombok.Getter;
import xyz.lotho.risecore.game.duels.kit.kits.DefaultKit;
import xyz.lotho.risecore.game.duels.kit.util.Kit;
import xyz.lotho.risecore.game.duels.kit.util.KitType;
import xyz.lotho.risecore.network.RiseCore;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KitManager {

    private final RiseCore riseCore;

    private final Map<KitType, Kit> kits = new HashMap<>();

    public KitManager(RiseCore riseCore) {
        this.riseCore = riseCore;

        addKit(new DefaultKit(riseCore));
    }

    public void addKit(Kit kit) {
        kits.put(kit.getName(), kit);
    }

    public Kit getKit(KitType kitType) {
        return kits.get(kitType);
    }

}
