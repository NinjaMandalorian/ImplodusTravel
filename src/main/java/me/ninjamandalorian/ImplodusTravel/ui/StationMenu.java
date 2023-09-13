package me.ninjamandalorian.ImplodusTravel.ui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseButton;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu.Builder;
import me.ninjamandalorian.ImplodusTravel.ui.task.BaseTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.CommandTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.InventoryTask;
import me.ninjamandalorian.ImplodusTravel.ui.task.MessageTask;
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
        BaseTask task;
        if (station.getOwner().equals(player)) {
            task = new InventoryTask(stationConfigMenu(player, station, true));
        } else {
            task = new MessageTask("&cYou do not have permission to use this!");
        }

        builder.setButton(
            15,
            BaseButton.create(Material.WRITABLE_BOOK)
                .name("&eEdit station")
                .lore(colorMsg("&cOwner only"))
                .task(task)
        );

        return builder.build();
    }

    public static Builder stationListMenu(Player player, Station station, boolean returnButton) {
        return BaseMenu.createBuilder().rows(4).title(station.getDisplayName() + " - List");
    }

    public static Builder stationConfigMenu(Player player, Station station, boolean returnButton) {
        return BaseMenu.createBuilder().rows(6).fillOutline().title("Configure Station");
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
