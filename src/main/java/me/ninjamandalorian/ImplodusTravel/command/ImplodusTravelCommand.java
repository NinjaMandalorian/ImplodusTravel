package me.ninjamandalorian.ImplodusTravel.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.ItemGenerator;
import me.ninjamandalorian.ImplodusTravel.controller.PersistentDataController;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import me.ninjamandalorian.ImplodusTravel.settings.Settings;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class ImplodusTravelCommand implements CommandExecutor, TabCompleter {

    public ImplodusTravelCommand() {
        ImplodusTravel plugin = ImplodusTravel.getInstance();
        plugin.getCommand("implodustravel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args[0].toLowerCase()) {
            case "buystation":
                if (sender instanceof Player plr) {
                    if (!plr.hasPermission("implodustravel.station.buy")) return true;
                    buyStationCommand(plr, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    sender.sendMessage("Not possible for console.");
                }
                return true;
            case "admintoken":
                if (sender instanceof Player plr) {
                    if (!plr.hasPermission("implodustravel.admin")) return true;
                    plr.getInventory().addItem(ItemGenerator.getDiscoveryTokenItem(new Station(UUID.randomUUID(), "TEST_STATION", plr, null, null)));
                    plr.updateInventory();
                }
                return true;
        }
        

        return true;
    }
    
    private void buyStationCommand(Player player, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().name().endsWith("BANNER")) {
            Economy econ = ImplodusTravel.getEcon();
            double balance = econ.getBalance(player);
            final double STATION_COST = Settings.getDefaultStationCost();
            if (balance < STATION_COST) {
                player.sendMessage(ChatColor.RED + "You need " + econ.format(STATION_COST) + " to do this.");
                return;
            }
            econ.withdrawPlayer(player, STATION_COST);
            PersistentDataController.giveItemTag(item);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Arrays.asList(ChatColor.GREEN + "Place this to create a new station."));
            item.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "Your banner is now a station. Place it to create the station.");
        } else {
            player.sendMessage(ChatColor.RED + "You must use a banner.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return startOnly(Arrays.asList("buystation"), args[0]);
        }
        return Collections.emptyList();
    }

    private static List<String> startOnly(List<String> options, String input) {
        ArrayList<String> returnList = new ArrayList<>();
        input = input.toLowerCase();
        for (String string : options) {
            if (string.toLowerCase().startsWith(input)) returnList.add(string);
        }

        return returnList;
    }
}
