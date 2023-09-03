package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.HashMap;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;

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
            NamespacedKey key = new NamespacedKey(ImplodusTravel.getInstance(), "banner-count");
            player.sendMessage(""+banner.getPersistentDataContainer().has(key, PersistentDataType.INTEGER));
            if(!banner.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                banner.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 0);
                banner.update();
            } else {
                Integer count = banner.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                banner.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, count + 1);
                banner.update();
                player.sendMessage(count + " - " + banner.getPersistentDataContainer().get(key, PersistentDataType.INTEGER));
            }
        }
        
    }

}
