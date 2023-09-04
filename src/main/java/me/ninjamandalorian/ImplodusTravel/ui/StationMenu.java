package me.ninjamandalorian.ImplodusTravel.ui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.ui.BaseMenu.Builder;
import me.ninjamandalorian.ImplodusTravel.ui.tasks.BaseTask;
import me.ninjamandalorian.ImplodusTravel.ui.tasks.CommandTask;
import me.ninjamandalorian.ImplodusTravel.ui.tasks.InventoryTask;
import me.ninjamandalorian.ImplodusTravel.ui.tasks.MessageTask;
import net.md_5.bungee.api.ChatColor;

public class StationMenu {
    
    public static BaseMenu stationMenu(Player player, Station station) {
        Builder builder = BaseMenu.createBuilder().rows(3);
        builder.fillOutline();
        builder.title("Station - " + station.getDisplayName());
        builder.setButton(
            11, 
            BaseButton.create(Material.COMPASS)
                .name("&9Add stations")
                .lore("&cWarning: Consumes all tokens.")
                .task(new CommandTask("/implodustravel addtokens " + station.getId()).autoClose())
        );
        builder.setButton(
            13,
            BaseButton.create(Material.FILLED_MAP)
                .name("&aOpen station list")
                .task(new InventoryTask(stationListMenu(player, station)))
        );
        BaseTask task;
        if (station.getOwner().equals(player)) {
            task = new InventoryTask(stationConfigMenu(player, station));
        } else {
            task = new MessageTask("&cYou do not have permission to use this!");
        }

        builder.setButton(
            15,
            BaseButton.create(Material.WRITABLE_BOOK)
                .name("&eEdit station")
                .lore("&cOwner only")
                .task(task)
        );

        return builder.build();
    }

    public static BaseMenu stationListMenu(Player player, Station station) {
        return null;
    }

    public static BaseMenu stationConfigMenu(Player player, Station station) {
        return null;
    }

    private static String colorMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
