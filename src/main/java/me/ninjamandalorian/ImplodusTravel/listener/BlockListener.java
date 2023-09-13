package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.UUID;

import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import me.ninjamandalorian.ImplodusTravel.controller.PersistentDataController;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import net.md_5.bungee.api.ChatColor;

public class BlockListener implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        
        if (player != null && block != null) return;
        // if (block.getType().toString().contains("SIGN")) {
        //     Port port = Port.getPort(block.getLocation());
        //     if (port == null) return;
            
        //     if (player.hasPermission("implodusports.admin.destroy")) {
        //         Port.portDestroy(player, port);
        //         return;
        //     } else {
        //         e.setCancelled(true);
        //         player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[&6iPorts&9] &cYou do not have permission to destroy this."));
        //         return;
        //     }
        // }
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

        
    }
    
}
