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

public class BlockListener implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        
        if (block.getState() instanceof Banner banner) {
            
            if (!PersistentDataController.isStationBlock(block)) return;
            Station station = Station.getStation(block.getLocation());
            if (station == null || player.hasPermission("implodustravel.admin")) {
                if (station != null) station.delete();
                Logger.log("Player " +  player.getUniqueId().toString() + " destroyed station banner " + String.valueOf(station));
            } else {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot break this station.");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        
        

        if (PersistentDataController.isStationItem(item) == false) return;
        if (!player.hasPermission("implodustravel.station.place")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot place this.");
            return;
        }

        if (Station.getPlayerStations(player).size() >= Settings.getMaxStationsPerPerson()) {
            if (player.hasPermission("implodustravel.admin")) {
                player.sendMessage(ChatColor.YELLOW + "You overrode the max station check as admin.");
                Logger.quietLog("Plugin admin " + player.getUniqueId().toString() + " overrode station no. limit.");
            } else {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You already have the maximum no. of stations.");
                return;
            }
        }

        Block block = e.getBlock();
        PersistentDataController.giveCustomTag(block);
        BlockState state = block.getState();
        Nameable nameable = (Nameable) state;
        nameable.setCustomName("Banner");
        state.update();
        
        Station newStation = new Station(
            UUID.randomUUID(),
            "Station " + (Station.getStations().size()+1) ,
            player,
            block.getLocation(),
            player.getLocation()
        );
        
        player.sendMessage("created station - " + newStation.getDisplayName() + " : " + newStation.getIdString());
        
    }
    
}
