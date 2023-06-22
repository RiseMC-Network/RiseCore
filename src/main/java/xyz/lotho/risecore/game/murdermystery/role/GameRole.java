package xyz.lotho.risecore.game.murdermystery.role;

import xyz.lotho.risecore.network.util.CC;

public enum GameRole {

    INNOCENT(CC.translate("&aInnocent"), CC.translate("&7Try to survive!")),
    DETECTIVE(CC.translate("&9Detective"), CC.translate("&7Find the murderer!")),
    MURDERER(CC.translate("&cMurderer"), CC.translate("&7Kill everyone!"));

    private final String roleName;
    private final String roleMessage;

    GameRole(String roleName, String roleMessage) {
        this.roleName = roleName;
        this.roleMessage = roleMessage;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleMessage() {
        return roleMessage;
    }
}
