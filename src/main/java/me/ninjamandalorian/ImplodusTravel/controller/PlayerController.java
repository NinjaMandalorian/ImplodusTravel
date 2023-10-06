package me.ninjamandalorian.ImplodusTravel.controller;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import net.md_5.bungee.api.ChatColor;

/** Controller for players, teleporting e.t.c.
 * @author NinjaMandalorian
 */
public class PlayerController {
    
    private static HashMap<Player, Long> playerWaitTimes = new HashMap<>(); // Wait time map
    private static HashMap<Player, Location> playerTeleportLocations = new HashMap<>(); // Location map

    /** Initialiser, schedules teleportTick()
     * @param instance - Plugin instance
     */
    public PlayerController(ImplodusTravel instance) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            teleportTick();
        }, 0L, 20L);
    }

    /** Initialises teleport for a player
     * @param player - Player
     * @param dest - Destination
     * @param waitTime - Time to wait (in ms)
     */
    public static void startTeleport(Player player, Location dest, int waitTime) {
        // Cancels if player has a pending teleport
        if (playerWaitTimes.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "You already have a pending teleport.");
            return;
        }
        // Adds to maps & notifies player
        playerWaitTimes.put(player, System.currentTimeMillis() + (waitTime * 1000L));
        playerTeleportLocations.put(player, dest);
        player.sendMessage(colorMsg("&bYou will be teleported in &a" + waitTime  + "&b seconds."));
    }

    /** End section of teleport
     * @param player - Player to teleport
     */
    private static void endTeleport(Player player) {
        player.teleport(playerTeleportLocations.get(player)); // Teleports player
        player.sendMessage(colorMsg("&2Teleportation Successful.")); // Notifies
        // Removes player from maps
        playerWaitTimes.remove(player);
        playerTeleportLocations.remove(player);
    }

    /** Failure of a teleport
     * @param player - Player
     */
    private static void failTeleport(Player player) {
        playerWaitTimes.remove(player);
        playerTeleportLocations.remove(player);
        player.sendMessage(colorMsg("&4Teleportation Failed."));
    }

    /** Teleport tick function */
    private static void teleportTick() {
        for (Entry<Player, Long> entry : playerWaitTimes.entrySet()) {
            if (System.currentTimeMillis() > entry.getValue()) {
                endTeleport(entry.getKey());
            }
        }
    }

    /** Runs on player move to check for teleport failures
     * @param e - Event
     */
    public static void playerMoved(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (playerWaitTimes.containsKey(player)) { // If player is waiting
            if ((e.getFrom().distance(e.getTo())) > 0.05) { // And if is non-rotational distance
                failTeleport(player); // Runs fail
            }
        }
    }

    /** Gets if the player is teleporting
     * @param player - Player to check
     * @return Bool of if is teleporting
     */
    public static boolean isPlayerTeleporting(Player player) {
        return playerWaitTimes.containsKey(player);
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
