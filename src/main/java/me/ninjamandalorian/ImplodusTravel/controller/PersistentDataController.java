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

public class PersistentDataController {
    
    private static NamespacedKey customKey = new NamespacedKey(ImplodusTravel.getInstance(), "customcheck");
    private static NamespacedKey tokenKey = new NamespacedKey(ImplodusTravel.getInstance(), "stationCode");

    public static boolean isStationItem(ItemStack item) {
        if (item.getItemMeta().getPersistentDataContainer().has(customKey, PersistentDataType.INTEGER)) return true;
        return false;
    }

    public static boolean isStationBlock(Block block) {
        if (block.getState() instanceof Banner banner) {
            if (banner.getPersistentDataContainer().has(customKey, PersistentDataType.INTEGER)) return true;
        }
        return false;
    }

    public static UUID getTokenUUID(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(tokenKey, PersistentDataType.STRING)) {
            return null;
        }
        try {
            UUID stationUUID = UUID.fromString(pdc.get(tokenKey, PersistentDataType.STRING));
            return stationUUID;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static void giveCustomTag(Block block) {
        BlockState state = block.getState();
        if (state instanceof PersistentDataHolder holder) {
            holder.getPersistentDataContainer().set(customKey, PersistentDataType.INTEGER, 1);
        }
        state.update();
    }

    public static void giveItemTag(ItemStack item) {
        giveItemTag(item, 2);
    }

    public static void giveItemTag(ItemStack item, Integer number) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(customKey, PersistentDataType.INTEGER, number);
        item.setItemMeta(meta);
    }

    public static void giveTokenTag(ItemStack item, Station station) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(tokenKey, PersistentDataType.STRING, station.getIdString());
        item.setItemMeta(meta);
    }

}
