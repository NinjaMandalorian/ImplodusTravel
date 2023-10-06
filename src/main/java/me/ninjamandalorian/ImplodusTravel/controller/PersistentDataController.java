package me.ninjamandalorian.ImplodusTravel.controller;

import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.object.Station;

/** Controller for plugin's persistent data 
 * @author NinjaMandalorian
*/
public class PersistentDataController {
    
    private static NamespacedKey customKey = new NamespacedKey(ImplodusTravel.getInstance(), "customcheck"); // Key for generic custom check
    private static NamespacedKey tokenKey = new NamespacedKey(ImplodusTravel.getInstance(), "stationCode"); // Key for station map's code

    /** Checks if a stack is a station item
     * @param item - Item to check
     * @return Boolean of if is a station item
     */
    public static boolean isStationItem(ItemStack item) {
        if (item.getItemMeta().getPersistentDataContainer().has(customKey, PersistentDataType.INTEGER)) return true;
        return false;
    }

    /** Checks if a block is a station block
     * @param block - Block to check
     * @return Boolean of if is station block
     */
    public static boolean isStationBlock(Block block) {
        if (block.getState() instanceof Banner banner) {
            if (banner.getPersistentDataContainer().has(customKey, PersistentDataType.INTEGER)) return true;
        }
        return false;
    }

    /** Get UUID from item (Map)
     * @param item - Item to get checked
     * @return UUID or null
     */
    public static UUID getTokenUUID(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null; // Returns null if no ItemMeta
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(tokenKey, PersistentDataType.STRING)) {
            return null; // If doesn't have key, returns null
        }
        try {
            UUID stationUUID = UUID.fromString(pdc.get(tokenKey, PersistentDataType.STRING));
            return stationUUID; // Returns UUID construction
        } catch (IllegalArgumentException ex) {
            return null; // If UUID fails, returns null.
        }
    }

    /** Gives custom tag to a block
     * @param block - Block to give tag
     */
    public static void giveCustomTag(Block block) {
        BlockState state = block.getState();
        if (state instanceof PersistentDataHolder holder) { // If can be a holder
            holder.getPersistentDataContainer().set(customKey, PersistentDataType.INTEGER, 1); // Sets key to value of 1
        }
        state.update(); // Updates
    }

    /** Gives custom tag to an item
     * @param item - Item to give tag
     */
    public static void giveItemTag(ItemStack item) {
        giveItemTag(item, 2); // Gives tag of 2 (generic item no.) to item
    }

    /** Gives custom tag to an item, with a given number
     * @param item - Item to give tag
     * @param number - Number to assign as tag
     */
    public static void giveItemTag(ItemStack item, Integer number) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(customKey, PersistentDataType.INTEGER, number);
        item.setItemMeta(meta);
    }

    /** Gives token tag of a station to a specified item
     * @param item - Item to add tag to
     * @param station - Station to give tag of
     */
    public static void giveTokenTag(ItemStack item, Station station) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(tokenKey, PersistentDataType.STRING, station.getIdString());
        item.setItemMeta(meta);
    }

}
