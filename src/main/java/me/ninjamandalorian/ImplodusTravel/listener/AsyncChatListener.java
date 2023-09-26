package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ninjamandalorian.ImplodusTravel.exceptions.ChatSettingException;
import me.ninjamandalorian.ImplodusTravel.object.SettingRequest;
import net.md_5.bungee.api.ChatColor;

public class AsyncChatListener implements Listener {
    
    private static HashMap<Player, SettingRequest> settingRequests = new HashMap<>();

    private final static int SETTING_TIMEOUT = 30;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (settingRequests.containsKey(e.getPlayer())) {
            Player player = e.getPlayer();
            SettingRequest request = settingRequests.get(player);
            settingRequests.remove(e.getPlayer());
            if (request.getRequestTime() + (1000L * SETTING_TIMEOUT) < System.currentTimeMillis()) {
                player.sendMessage(ChatColor.RED + "Setting request expired. Please try again.");
                return;
            }

            try {
                request.getSettable().setSetting(request.getSetting(), e.getMessage());
            } catch (ChatSettingException ex) {
                player.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
            }
            try {
                for (Player p : e.getRecipients()) {
                    if (!p.equals(player)) {
                        e.getRecipients().remove(p);
                    }
                }   
            } catch (Exception ex) {}
            // e.setCancelled(true);
        }
    }

    public static void startRequest(SettingRequest request) {
        settingRequests.put(request.getPlayer(), request);
    }

}
