package me.ninjamandalorian.ImplodusTravel.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.object.Station;
import net.md_5.bungee.api.ChatColor;

public class ImplodusTravelAdminCommand implements CommandExecutor, TabCompleter{

    public ImplodusTravelAdminCommand() {
        ImplodusTravel plugin = ImplodusTravel.getInstance();
        plugin.getCommand("implodustraveladmin").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!sender.hasPermission("implodustravel.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid command.");
            return true;
        }

        switch (args[0]) {
            case "list":
                listStations(sender);
                break;
            case "view":
                viewStation(sender, remFirst(args));
                break;
            case "delete":
                deleteStation(sender, remFirst(args));
                break;
            case "teleport":
                teleportStation(sender, remFirst(args));
            default:
                break;
        }
        return true;
    }

    private void viewStation(CommandSender sender, String[] args) {
        Station station = strToStation(args.length > 0 ? args[0] : null);
        if (station == null) {
            sender.sendMessage("Please specify a station UUID.");
            return;
        }
        String stationString = "&aStation: &e" + station.getDisplayName() + "&a | Owner: &e" + station.getOwner().getName() + "&a | Banner: &e" + station.getStationLocation().toString();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', stationString));
    }
    
    private void listStations(CommandSender sender) {
        Collection<Station> stations = Station.getStations();
        sender.sendMessage("Name - Owner - ID");
        for (Station station : stations) {
            sender.sendMessage(ChatColor.GREEN + station.getDisplayName() + " - " + station.getOwner().getName() + " - " + station.getIdString());
        }
    }
    
    private void deleteStation(CommandSender sender, String[] args) {
        Station station = strToStation(args.length > 0 ? args[0] : null);
        if (station == null) {
            sender.sendMessage("Please specify a station UUID.");
            return;
        }

        station.delete();
        sender.sendMessage(ChatColor.GREEN + "Station deleted.");
    }

    private void teleportStation(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command.");
            return;
        }

        Station station = strToStation(args.length > 0 ? args[0] : null);
        if (station == null) {
            sender.sendMessage("Please specify a station UUID.");
            return;
        }

        player.teleport(station.getStationLocation());
        player.sendMessage(ChatColor.GREEN + "Teleported to " + station.getDisplayName());
    }

    private Station strToStation(String string) {
        UUID id;
        try {
            id = UUID.fromString(string); 
        } catch (Exception e) {
            return null;
        }
        return Station.getStation(id);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("implodustravel.admin")) return Collections.emptyList();

        switch (args.length) {
        case 0:
        case 1:
            return startOnly(Arrays.asList("list", "view"), args[0]);
        case 2:
            if (args[0].equalsIgnoreCase("view")) {
                ArrayList<String> returnList = new ArrayList<>();
                for (Station station : Station.getStations()) returnList.add(station.getIdString());
                return startOnly(returnList, args[1]);
            }
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
    
    private String[] remFirst(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}