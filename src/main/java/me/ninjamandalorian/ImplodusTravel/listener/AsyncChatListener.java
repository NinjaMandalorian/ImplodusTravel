package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ninjamandalorian.ImplodusTravel.exceptions.ChatSettingException;
import me.ninjamandalorian.ImplodusTravel.object.SettingRequest;
import me.ninjamandalorian.ImplodusTravel.settings.Settings;
import net.md_5.bungee.api.ChatColor;

/** Chat skimmer for settings
 * @author NinjaMandalorian
 */
public class AsyncChatListener implements Listener {
    
    private static HashMap<Player, SettingRequest> settingRequests = new HashMap<>(); // Map for handling requests

    /** Runs on a player sending a message in chat
     * @param e - Event
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (settingRequests.containsKey(e.getPlayer())) {
            // If player is in map, gets the request and removes it from the list
            Player player = e.getPlayer();
            SettingRequest request = settingRequests.get(player);
            settingRequests.remove(e.getPlayer());
            if (request.getRequestTime() + (1000L * Settings.getSettingTimeout()) < System.currentTimeMillis()) {
                player.sendMessage(ChatColor.RED + "Setting request expired. Please try again.");
                return; // If past expiry date, return
            }

            // Try to set setting, catches exception to show player.
            try {
                request.getSettable().setSetting(request.getSetting(), e.getMessage());
            } catch (ChatSettingException ex) {
                player.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
            }
            try {
                for (Player p : e.getRecipients()) {
                    if (!p.equals(player)) {
                        // Hides player's chat message for all other players
                        e.getRecipients().remove(p);
                    }
                }   
            } catch (Exception ex) {}
        }
    }

    /** Starts a request
     * @param request - Request to be added to checks
     */
    public static void startRequest(SettingRequest request) {
        settingRequests.put(request.getPlayer(), request);
    }

}
