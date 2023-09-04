package me.ninjamandalorian.ImplodusTravel.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.ItemGenerator;

public class ImplodusTravelCommand implements CommandExecutor {

    public ImplodusTravelCommand() {
        ImplodusTravel plugin = ImplodusTravel.getInstance();
        plugin.getCommand("implodustravel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args[0].toLowerCase()) {
            case "givestation":
                if (sender instanceof Player plr) {
                    if (!plr.hasPermission("implodustravel.admin")) return true;
                    plr.getInventory().addItem(ItemGenerator.getStationItem());
                    plr.updateInventory();
                }
                return true;
        }
        

        return true;
    }
    
}
