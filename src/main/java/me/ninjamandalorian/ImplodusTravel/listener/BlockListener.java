package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.UUID;

import org.bukkit.Nameable;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.ninjamandalorian.ImplodusTravel.Logger;
import me.ninjamandalorian.ImplodusTravel.controller.PersistentDataController;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.settings.Settings;
import net.md_5.bungee.api.ChatColor;

/** Listener for block events
 * @author NinjaMandalorian
 */
public class BlockListener implements Listener {
    
    /** Block break listener
     * @param e - Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        
        if (block.getState() instanceof Banner banner) {
            if (!PersistentDataController.isStationBlock(block)) return; // Ignores if not station
            Station station = Station.getStation(block.getLocation());
            if (station == null || player.hasPermission("implodustravel.admin")) { // Checks if admin or null station
                if (station != null) station.delete(); // Deletes station object
                Logger.log("Player " +  player.getUniqueId().toString() + " destroyed station banner " + String.valueOf(station));
            } else {
                e.setCancelled(true); // Cancels and notifies player
                player.sendMessage(ChatColor.RED + "You cannot break this station.");
            }
        }
    }

    /** Block placed listener
     * @param e - Event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();

        if (PersistentDataController.isStationItem(item) == false) return; // Ignores if not station item
        if (!player.hasPermission("implodustravel.station.place")) {
            e.setCancelled(true); // Cancels if player lacks permission & 
            player.sendMessage(ChatColor.RED + "You cannot place this.");
            return;
        }

        // Max station check
        if (Station.getPlayerStations(player).size() >= Settings.getMaxStationsPerPerson()) {
            if (player.hasPermission("implodustravel.admin")) {
                // Admins can override the max
                player.sendMessage(ChatColor.YELLOW + "You overrode the max station check as admin.");
                Logger.quietLog("Plugin admin " + player.getUniqueId().toString() + " overrode station no. limit.");
            } else {
                // Cancels
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You already have the maximum no. of stations.");
                return;
            }
        }

        // If placing is valid, it begins initialising
        Block block = e.getBlock();
        PersistentDataController.giveCustomTag(block); // Gives banner block tag
        BlockState state = block.getState();
        Nameable nameable = (Nameable) state;
        nameable.setCustomName("Banner");
        state.update(); // Updates block
        
        Station newStation = new Station( // Create station object
            UUID.randomUUID(),
            "Station " + (Station.getStations().size()+1) ,
            player,
            block.getLocation(),
            player.getLocation()
        );
        // Notify and log
        player.sendMessage(ChatColor.GREEN + "You created " + ChatColor.YELLOW + newStation.getDisplayName() + ChatColor.GREEN + ".");
        Logger.quietLog(player.getUniqueId().toString() +  " created station - " + newStation.getDisplayName() + " : " + newStation.getIdString());
        
    }
    
}
