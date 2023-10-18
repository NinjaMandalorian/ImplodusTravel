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

/** Admin Command Class
 * @author NinjaMandalorian
 */
public class ImplodusTravelAdminCommand implements CommandExecutor, TabCompleter{

    /** Constructor that adds command to plugin */
    public ImplodusTravelAdminCommand() {
        ImplodusTravel plugin = ImplodusTravel.getInstance();
        plugin.getCommand("implodustraveladmin").setExecutor(this);
    }
    
    /** Admin command switch case */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Returns if no permission
        if (!sender.hasPermission("implodustravel.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
            return true;
        }
        // Returns if no arguments
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid command.");
            return true;
        }

        // Command switch
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

    /** Show station info to user
     * @param sender - user who requested info
     * @param args - arguments
     */
    private void viewStation(CommandSender sender, String[] args) {
        // Get station from 1st argument
        Station station = strToStation(args.length > 0 ? args[0] : null);
        if (station == null) {
            sender.sendMessage("Please specify a station UUID.");
            return;
        }
        // Sends info to player
        String stationString = "&aStation: &e" + station.getDisplayName() + "&a | Owner: &e" + station.getOwner().getName() + "&a | Banner: &e" + station.getStationLocation().toString();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', stationString));
    }
    
    /** Show station list to user
     * @param sender - user who requested list
     */
    private void listStations(CommandSender sender) {
        Collection<Station> stations = Station.getStations(); // Get stations
        sender.sendMessage("Name - Owner - ID");
        for (Station station : stations) { // Send all stations and their information
            sender.sendMessage(ChatColor.GREEN + station.getDisplayName() + " - " + station.getOwner().getName() + " - " + station.getIdString());
        }
    }
    
    /** Remotely delete station object
     * <p>Does not delete block from world.
     * @param sender - user who requested deletion
     * @param args - arguments
     */
    private void deleteStation(CommandSender sender, String[] args) {
        // Get station from 1st argument
        Station station = strToStation(args.length > 0 ? args[0] : null); 
        if (station == null) {
            sender.sendMessage("Please specify a station UUID.");
            return;
        }

        // Delete & confirm to player
        station.delete();
        sender.sendMessage(ChatColor.GREEN + "Station deleted.");
    }

    /** Teleport user to station banner
     * @param sender - user who requested teleport
     * @param args - arguments
     */
    private void teleportStation(CommandSender sender, String[] args) {
        // Return if not a player (console)
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command.");
            return;
        }
        // Get station from 1st argument
        Station station = strToStation(args.length > 0 ? args[0] : null);
        if (station == null) {
            sender.sendMessage("Please specify a station UUID.");
            return;
        }
        // Teleport & notify player
        player.teleport(station.getStationLocation());
        player.sendMessage(ChatColor.GREEN + "Teleported to " + station.getDisplayName());
    }

    /** Converts string to station
     * @param string - input string
     * @return Station or null
     */
    private Station strToStation(String string) {
        UUID id;
        try {
            id = UUID.fromString(string); // Attempts string to UUID
        } catch (Exception e) {
            return null; // Returns null if error
        }
        return Station.getStation(id); // Else returns station list result (may be null)
    }

    /** Tab Completion for admin commands */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("implodustravel.admin")) return Collections.emptyList(); // Gives no results if not an admin

        // Swithc case for no. of args
        switch (args.length) {
        case 0:
        case 1:
            return startOnly(Arrays.asList("list", "view", "delete", "teleport"), args[0]);
        case 2:
            switch (args[0]) {
                case "view":
                case "delete": 
                case "teleport":
                    ArrayList<String> returnList = new ArrayList<>();
                    for (Station station : Station.getStations()) returnList.add(station.getIdString()); // Adds strings of all station's ids
                    return startOnly(returnList, args[1]);
            }
        }
        return Collections.emptyList(); // If suits no other case, gets empty
    }
    
    /** Culls list to those beginning with a certain string
     * @param options - Available options
     * @param input - Beginning
     * @return list of options beginning with string
     */
    private static List<String> startOnly(List<String> options, String input) {
        ArrayList<String> returnList = new ArrayList<>();
        input = input.toLowerCase(); // Converts input to lowercase
        for (String string : options) {
            // Adds to return list if matches
            if (string.toLowerCase().startsWith(input)) returnList.add(string);
        }
        
        return returnList;
    }
    
    /**
     * Removes first argument of array
     * @param args - Array of arguments
     * @return Array arguments without the 1st argument
     */
    private String[] remFirst(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}