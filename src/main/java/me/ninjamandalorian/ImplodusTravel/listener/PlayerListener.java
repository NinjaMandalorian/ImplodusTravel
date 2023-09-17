package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ninjamandalorian.ImplodusTravel.controller.PersistentDataController;
import me.ninjamandalorian.ImplodusTravel.controller.PlayerController;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.ui.StationMenu;

public class PlayerListener implements Listener {
    
    HashMap<Player, Long> cooldownMap = new HashMap<Player, Long>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if (block==null) return;
        if (cooldownMap.containsKey(player) && System.currentTimeMillis() - cooldownMap.get(player) < 250) return;
        cooldownMap.put(player, System.currentTimeMillis());

        if (block.getState() instanceof Banner banner) {
            
            if (!PersistentDataController.isStationBlock(block)) return;
            Action action = e.getAction();
            if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                Station station = Station.getStation(block.getLocation());
                if (station == null) {
                    Bukkit.getLogger().info("[IMPLODUSTRAVEL] ERROR: BANNER AT " + block.getLocation() + " HAS MARK BUT NO OBJECT");
                    return;
                }
                StationMenu.stationMenu(player, station).open(player);
            }
        }
        
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        PlayerController.playerMoved(e);
    }

}
