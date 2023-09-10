package me.ninjamandalorian.ImplodusTravel.controller;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;

public class PersistentDataController {
    
    private static NamespacedKey customKey = new NamespacedKey(ImplodusTravel.getInstance(), "customcheck");
    
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

    public static void giveCustomTag(Block block) {
        BlockState state = block.getState();
        if (state instanceof PersistentDataHolder holder) {
            holder.getPersistentDataContainer().set(customKey, PersistentDataType.INTEGER, 1);
        }
    }

}
