package me.ninjamandalorian.ImplodusTravel.controller;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.Logger;
import net.md_5.bungee.api.ChatColor;

public class PlayerController {
    
    private static HashMap<Player, Long> playerWaitTimes = new HashMap<>();
    private static HashMap<Player, Location> playerTeleportLocations = new HashMap<>();

    public PlayerController(ImplodusTravel instance) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            teleportTick();
        }, 0L, 20L);
    }

    public static void startTeleport(Player player, Location dest, int waitTime) {
        playerWaitTimes.put(player, System.currentTimeMillis() + (waitTime * 1000L));
        playerTeleportLocations.put(player, dest);
        player.sendMessage(colorMsg("&bYou will be teleported in &a" + waitTime  + "&b seconds."));
    }

    private static void endTeleport(Player player) {
        player.teleport(playerTeleportLocations.get(player));
        player.sendMessage(colorMsg("&2Teleportation Successful."));
        playerWaitTimes.remove(player);
        playerTeleportLocations.remove(player);
    }

    private static void failTeleport(Player player) {
        playerWaitTimes.remove(player);
        playerTeleportLocations.remove(player);
        player.sendMessage(colorMsg("&4Teleportation Failed."));
    }

    public static void teleportTick() {
        for (Entry<Player, Long> entry : playerWaitTimes.entrySet()) {
            if (System.currentTimeMillis() > entry.getValue()) {
                endTeleport(entry.getKey());
            }
        }
    }
    public static void playerMoved(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (playerWaitTimes.containsKey(player)) {
            if ((e.getFrom().distance(e.getTo())) > 0.05) {
                failTeleport(player);
            }
        }
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
