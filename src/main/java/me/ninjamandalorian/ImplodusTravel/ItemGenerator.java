package me.ninjamandalorian.ImplodusTravel;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.ninjamandalorian.ImplodusTravel.object.Station;
import net.md_5.bungee.api.ChatColor;

public class ItemGenerator {
    
    public static ItemStack getStationItem() {
        ItemStack stationItemStack = new ItemStack(Material.YELLOW_BANNER);
        ItemMeta meta = stationItemStack.getItemMeta();
        meta.setDisplayName(colorMsg("&e&lStation Item"));
        meta.setLore(Arrays.asList(colorMsg("&aPlace this to create a new station.")));

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(ImplodusTravel.getInstance(), "customCheck"), PersistentDataType.INTEGER, 1);

        stationItemStack.setItemMeta(meta);
        return stationItemStack;
    }

    public static ItemStack getDiscoveryTokenItem(Station discoveredStation) {
        ItemStack tokenStack = new ItemStack(Material.PAPER);
        ItemMeta meta = tokenStack.getItemMeta();

        meta.setDisplayName(colorMsg("&aStation Token &7- &e" + discoveredStation.getDisplayName()));
        meta.setLore(Arrays.asList(colorMsg("&bUse this at any station to grant travel!"), colorMsg("&4One-use only.")));

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(ImplodusTravel.getInstance(), "stationTokenId"), PersistentDataType.STRING, discoveredStation.getIdString());

        tokenStack.setItemMeta(meta);
        return tokenStack;
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
