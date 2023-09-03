package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.HashMap;

import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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
            
            // TODO If NOT station return
            Action action = e.getAction();
            if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                // TODO Open menu
                player.sendMessage("OPEN MENU");
            }
        }
        
    }

}
