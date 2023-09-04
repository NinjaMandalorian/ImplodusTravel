package me.ninjamandalorian.ImplodusTravel;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;

public class ItemGenerator {
    
    public static ItemStack getStationItem() {
        ItemStack stationItemStack = new ItemStack(Material.YELLOW_BANNER);
        ItemMeta meta = stationItemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lStation Item"));
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&aPlace this to create a new station.")));

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(ImplodusTravel.getInstance(), "customCheck"), PersistentDataType.INTEGER, 1);

        stationItemStack.setItemMeta(meta);

        return stationItemStack;
    }

}
