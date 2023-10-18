package me.ninjamandalorian.ImplodusTravel.object;

import org.bukkit.entity.Player;

/** Setting Request Object
 * @author NinjaMandalorian
 */
public class SettingRequest {
    
    private Player player; // Player making request
    private ChatSettable settable; // Settable
    private String setting; // Key
    private Long requestTime; // Time of request

    /** Constructor
     * @param player - Player
     * @param settable - Settable object
     * @param settingName - Key for setting
     */
    public SettingRequest(Player player, ChatSettable settable, String settingName) {
        this.player = player;
        this.settable = settable;
        this.setting = settingName;
        this.requestTime = System.currentTimeMillis();
    }

    /** Get player
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /** Get settable
     * @return Settable
     */
    public ChatSettable getSettable() {
        return settable;
    }

    /** Get setting
     * @return Setting key
     */
    public String getSetting() {
        return setting;
    }
    
    /** Get request time
     * @return Request time
     */
    public Long getRequestTime() {
        return requestTime;
    }

}
