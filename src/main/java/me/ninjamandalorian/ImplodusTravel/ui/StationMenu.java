package me.ninjamandalorian.ImplodusTravel.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseButton;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;
import me.ninjamandalorian.ImplodusTravel.ui.object.Buildable;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu.Builder;
import me.ninjamandalorian.ImplodusTravel.ui.task.ChatSettingTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.InventoryTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.MessageTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.RunnableTask;
import net.md_5.bungee.api.ChatColor;

/**
 * Class for generating the menus related to stations and their functions.
 */
public class StationMenu {
    
    /**
     * Creates the primary menu for stations
     * @param player - Player receiving the menu
     * @param station - Station of menu
     * @return - Completed menu
     */
    public static BaseMenu stationMenu(Player player, Station station) {
        Builder builder = BaseMenu.createBuilder().rows(3);
        builder.fillOutline();
        builder.title("Station - " + station.getDisplayName());
        RunnableTask tokenTask = new RunnableTask(() -> {station.addDestination(player);});
        tokenTask = tokenTask.closeMenu();
        builder.setButton(
            11, 
            BaseButton.create(Material.COMPASS)
                .name("&bAdd new station")
                .lore(colorMsg("&cWarning: Consumes all tokens."))
                .task(tokenTask)
        );
        builder.setButton(
            13,
            BaseButton.create(Material.FILLED_MAP)
                .name("&aOpen station list")
                .task(new InventoryTask(stationListMenu(player, station, true)))
        );
        BaseButton editButton = BaseButton.create(Material.WRITABLE_BOOK).name("&eEdit station")
        .lore(colorMsg("&cOwner only"));

        if (station.getOwner().equals(player)) {
            editButton.task(new InventoryTask(stationConfigMenu(player, station, true)));
            editButton.glow();
        } else {
            editButton.task(new MessageTask(colorMsg("&cYou do not have permission to use this!")));
        }

        builder.setButton(
            15,
            editButton
        );

        return builder.build();
    }

    public static Buildable stationListMenu(Player player, Station station, boolean returnButton) {
        // TODO improve list (design)
        // Paged menu (setContents with stationButtons?)
        // click -> teleport attempt
        // Outline? yes/no?
        // page buttons
        Builder builder = BaseMenu.createBuilder().rows(4).title(station.getDisplayName() + " - List");

        ArrayList<BaseButton> stationButtons = generateStations(player, station);

        for (int i = 0; i < stationButtons.size(); i++) {
            builder.setButton(i, stationButtons.get(i));
        }

        return builder;
    }

    public static Buildable stationConfigMenu(Player player, Station station, boolean returnButton) {
        Builder builder = BaseMenu.createBuilder().rows(6).fillOutline().title("Configure Station");
        builder.setButton(
            10, 
            BaseButton.create(Material.OAK_SIGN).task(new ChatSettingTask(player, station, "rename")).name("&aRename Station")
            .lore(colorMsg("&dTyped in chat. &cAlphanumeric.\n&eClick to start."))
        );

        builder.setButton(11, generateRankButton(player, station, "towny_town", Material.HORN_CORAL_FAN).name("&3Town Multiplier") ); // Town
        builder.setButton(12, generateRankButton(player, station, "towny_nation", Material.TUBE_CORAL_FAN).name("&3Nation Multiplier") ); // Nation
        builder.setButton(13, generateRankButton(player, station, "towny_ally", Material.BRAIN_CORAL_FAN).name("&3Ally Multiplier") ); // Ally
        builder.setButton(14, generateRankButton(player, station, "neutral", Material.DEAD_BRAIN_CORAL).name("&3Neutral Multiplier") ); // Neutral
        builder.setButton(15, generateRankButton(player, station, "towny_enemy", Material.FIRE_CORAL_FAN).name("&3Enemy Multiplier") ); // Enemy
        
        builder.setButton(
            16, 
            BaseButton.create(Material.EMERALD).task(new ChatSettingTask(player, station, "defaultCost")).name("&aChange cost")
            .lore(colorMsg("&aCurrent cost: &6" + ImplodusTravel.getEcon().format(station.getDefaultCost())))
        );

        boolean isOwnerOnly = station.hasOwnerOnlyMaps();
        builder.setButton(
            19, 
            BaseButton.create(Material.MAP)
            .task(new RunnableTask(() -> {
                station.setOwnerOnlyMaps(!isOwnerOnly);
                player.sendMessage(colorMsg("&aOwner only maps is now: " + (!isOwnerOnly ? "&2" : "&c") + String.valueOf(!isOwnerOnly)));
            }).closeMenu())
            .name("&4Owner Only Map Making")
            .lore(colorMsg("&7When true, only you can make maps.\n&7This is currently set to: " 
            + (isOwnerOnly ? "&a" : "&c") + String.valueOf(isOwnerOnly) + "&7."))
        );

        return builder;
    }

    private static BaseButton generateRankButton(Player player, Station station, String rank, Material material) {
        // TODO put warning if towny is not installed but allow editing
        BaseButton button = BaseButton.create(material);
        button.name(rank);

        Long rankMult = Math.round( station.getRankMult(rank)*100.0 );
        
        button.lore(colorMsg("&7The percent of the station's travel\n&7cost that is charged to this group."
        + "\n&7Current value: " + (rankMult < 0 ? "&cBlacklisted": "&a" +rankMult + "%")));
        button.task(new ChatSettingTask(player, station, "rank." + rank));
        return button;
    }

    private static ArrayList<BaseButton> generateStations(Player player, Station station) {
        ArrayList<BaseButton> stationButtons = new ArrayList<BaseButton>();

        ArrayList<Station> nearbyStations = Station.getStationsInRange(station, 5000.0);
        for (Station stationEntry : nearbyStations) {
            if (stationEntry.equals(station)) continue;
            if (!station.getDestinations().contains(stationEntry.getId())) continue;
            BaseButton button = generateStationButton(player, station, stationEntry);
            if (button != null) stationButtons.add(button);
        }
        return stationButtons;
    }

    private static BaseButton generateStationButton(Player player, Station source, Station dest) {
        
        // Creates ItemStack from block (same pattern & color)
        Block bannerBlock = dest.getStationLocation().getBlock();
        Banner banner = (Banner) bannerBlock.getState();
        List<Pattern> patterns = banner.getPatterns();
        ItemStack buttonStack = new ItemStack(bannerBlock.getType());
        BannerMeta meta = (BannerMeta) buttonStack.getItemMeta();
        meta.setPatterns(patterns);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        buttonStack.setItemMeta(meta);

        BaseButton button = BaseButton.create().itemStack(buttonStack);
        if (dest.isBlacklisted(player)) {
            button.lore(colorMsg("&cYou are blacklisted from this station."));
            button.task(new MessageTask(colorMsg("&cYou are blacklisted from this station.")));
        } else {
            button.glow();
            button.task(new RunnableTask(() -> {
                dest.teleportPlayer(player, source);
            }));
        }
        button.name("&b" + dest.getDisplayName());

        String loreString = "";
        int distance = (int) Math.ceil(source.getStationLocation().distance(dest.getTeleportLocation()));
        loreString += "&7Distance: &6" + distance;
        double cost = dest.getCost(player) + source.getCost(player);
        loreString += "\n&7Cost: &6" + ImplodusTravel.getEcon().format(cost);
        button.lore(colorMsg(loreString));

        return button;
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
