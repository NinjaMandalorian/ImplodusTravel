package me.ninjamandalorian.ImplodusTravel.listener;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.Logger;
import me.ninjamandalorian.ImplodusTravel.controller.PersistentDataController;
import me.ninjamandalorian.ImplodusTravel.controller.PlayerController;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.settings.Settings;
import me.ninjamandalorian.ImplodusTravel.ui.StationMenu;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

/** Player listeners class
 * @author NinjaMandalorian
 */
public class PlayerListener implements Listener {

    HashMap<Player, Long> cooldownMap = new HashMap<Player, Long>(); // Map for stopping interaction spam

    /** Player interaction listener
     * @param e - Event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if (block == null)
            return; // Return if null
        if (cooldownMap.containsKey(player) && System.currentTimeMillis() - cooldownMap.get(player) < 250)
            return; // Return if on cooldown
        cooldownMap.put(player, System.currentTimeMillis());

        if (block.getState() instanceof Banner banner) { // If not banner ignore

            if (!PersistentDataController.isStationBlock(block))
                return; // Return if not station block
            Action action = e.getAction();
            if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                Station station = Station.getStation(block.getLocation());
                if (station == null) {
                    Logger.warn("[IMPLODUSTRAVEL] ERROR: BANNER AT " + block.getLocation() + " HAS MARK BUT NO OBJECT");
                    return;
                }
                if (station.isBlacklisted(player)) {
                    player.sendMessage(ChatColor.RED + "You are blacklisted from this station.");
                    return; // If blacklisted, notifies player and returns.
                }
                // If using a map, runs the map method
                ItemStack item = e.getItem();
                if (item != null) {
                    Logger.log(item.getType().toString());
                    if (e.getItem().getType().equals(Material.MAP)) {
                        Logger.log("map");
                        // PersistentDataController.giveItemTag(item);
                        mapHandle(e, station);
                        return;
                    }
                }
                // Otherwise, opens station.
                e.setCancelled(true);
                StationMenu.stationMenu(player, station).open(player);
            }
        }

    }

    /** Player move event
     * @param e - Event
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        PlayerController.playerMoved(e); // Hands over to PlayerController for teleports
    }

    /** Handles station map creation
     * @param e - Event
     * @param station - Station interacted with
     */
    private static void mapHandle(PlayerInteractEvent e, Station station) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        // Catch incase changes happen between methods
        if (station.isBlacklisted(player)) {
            e.setCancelled(true);
            return;
        }

        // No permission
        if (!player.hasPermission("implodustravel.station.map")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have permission to create station tokens.");
            return;
        }

        // Must be a single stack
        if (item.getAmount() > 1) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Hold a single map to make a token.");
            return;
        }

        if (station.hasOwnerOnlyMaps() && !player.equals(station.getOwner())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "This station has owner-only map-making.");
            return;
        }

        // Must be in survival/adventure
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You need to be in survival to make a token.");
            return;
        }

        // Must have required money.
        Economy econ = ImplodusTravel.getEcon();
        Double cost = Settings.getDefaultMapCost() * station.getRankMult(player);
        if (econ.getBalance(player) < cost) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You need " + econ.format(cost) + " to make a token.");
            return;
        }

        int slotNum = e.getPlayer().getInventory().getHeldItemSlot();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ImplodusTravel.getInstance(), new Runnable() {

            @Override
            public void run() {
                ItemStack item = e.getPlayer().getInventory().getItem(slotNum);
                if (item.getType().equals(Material.FILLED_MAP)) {
                    if (econ.getBalance(player) < cost) return;
                    econ.withdrawPlayer(player, cost);
                    PersistentDataController.giveTokenTag(item, station);

                    // Add ItemStack information
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aStation Map &7- &e" + station.getDisplayName()));
                    String lore = "&7Hold this whilst clicking\n&7the &bAdd new station &7button\n&7to add it to the station list.";
                    lore = ChatColor.translateAlternateColorCodes('&', lore);
                    meta.setLore((Arrays.asList(lore.split("\n"))));
                    item.setItemMeta(meta);

                    player.sendMessage(ChatColor.GREEN + "You paid " + econ.format(cost) + " to make a map for " + ChatColor.YELLOW + station.getDisplayName());
                } else {
                    Logger.log(player.getName() + " switched inventory slot real fast.");
                }
            }
        }, 3L);
    }

}
