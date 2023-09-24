package me.ninjamandalorian.ImplodusTravel.ui;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseButton;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;
import me.ninjamandalorian.ImplodusTravel.ui.object.Buildable;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu.Builder;
import me.ninjamandalorian.ImplodusTravel.ui.task.ChatSettingTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.CommandTask;
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
        builder.setButton(
            11, 
            BaseButton.create(Material.COMPASS)
                .name("&bAdd new station")
                .lore(colorMsg("&cWarning: Consumes all tokens."))
                .task(new CommandTask("/implodustravel addtokens " + station.getIdString()).autoClose())
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

        builder.setButton(11, generateRankButton(player, station, "towny_town", Material.HORN_CORAL_FAN) ); // Town
        builder.setButton(12, generateRankButton(player, station, "towny_nation", Material.TUBE_CORAL_FAN) ); // Nation
        builder.setButton(13, generateRankButton(player, station, "towny_ally", Material.BRAIN_CORAL_FAN) ); // Ally
        builder.setButton(14, generateRankButton(player, station, "neutral", Material.DEAD_BRAIN_CORAL) ); // Neutral
        builder.setButton(15, generateRankButton(player, station, "towny_enemy", Material.FIRE_CORAL_FAN) ); // Enemy
        
        builder.setButton(
            16, 
            BaseButton.create(Material.EMERALD).task(new ChatSettingTask(player, station, "defaultCost")).name("&aChange cost")
            .lore(colorMsg("&aCurrent cost: &6" + ImplodusTravel.getEcon().format(station.getDefaultCost())))
        );

        return builder;
    }

    private static BaseButton generateRankButton(Player player, Station station, String rank, Material material) {
        // TODO put warning if towny is not installed but allow editing
        BaseButton button = BaseButton.create(material);
        button.name(rank);

        Long rankMult = Math.round( station.getRankMult(rank)*100.0 );
        
        button.lore("Current value:\n" + (rankMult < 0 ? "Blacklisted": rankMult + "%"));
        button.task(new ChatSettingTask(player, station, "rank." + rank));
        return button;
    }

    private static ArrayList<BaseButton> generateStations(Player player, Station station) {
        ArrayList<BaseButton> stationButtons = new ArrayList<BaseButton>();

        ArrayList<Station> nearbyStations = Station.getStationsInRange(station, 5000.0);
        for (Station stationEntry : nearbyStations) {
            if (stationEntry.equals(station)) continue;
            BaseButton button = generateStationButton(player, station, stationEntry);
            if (button != null) stationButtons.add(button);
        }
        return stationButtons;
    }

    private static BaseButton generateStationButton(Player player, Station source, Station dest) {
        Material blockMaterial = dest.getStationLocation().getBlock().getType();
        BaseButton button = BaseButton.create(blockMaterial);
        if (source.getDestinations().contains(dest.getId())) { // Is unlocked
            if (dest.isBlacklisted(player)) {
                button.lore(colorMsg("&cYou are blacklisted from this station."));
                button.task(new MessageTask(colorMsg("&cYou are blacklisted from this station.")));
            } else {
                button.glow();
                button.task(new RunnableTask(() -> {
                    dest.teleportPlayer(player, source);
                }));
            }
        } else { // Is NOT unlocked
            // TODO - DEBUG TOOL TO ADD TO UNLOCKED
            // Will remove entire else section after adding maps : not to display locked destinations
            button.task(new RunnableTask(() -> {
                source.addDestination(dest);
                Bukkit.broadcastMessage("ADD "+dest.getDisplayName()+" TO "+source.getDisplayName());
            }));        
        }
        button.name("&b" + dest.getDisplayName());
        
        return button;
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
