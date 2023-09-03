package me.ninjamandalorian.ImplodusTravel.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if (block==null) return;

        if (block.getType().toString().contains("BANNER")) {
            Bukkit.getLogger().info("BANNER ");
            
        }
        
    }

}
