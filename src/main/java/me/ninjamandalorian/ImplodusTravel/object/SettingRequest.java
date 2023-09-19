package me.ninjamandalorian.ImplodusTravel.object;

import org.bukkit.entity.Player;

public class SettingRequest {
    
    private Player player;
    private ChatSettable settable;
    private String setting;
    private Long requestTime;

    public SettingRequest(Player player, ChatSettable settable, String settingName) {
        this.player = player;
        this.settable = settable;
        this.setting = settingName;
        this.requestTime = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public ChatSettable getSettable() {
        return settable;
    }

    public String getSetting() {
        return setting;
    }
    
    public Long getRequestTime() {
        return requestTime;
    }

}
