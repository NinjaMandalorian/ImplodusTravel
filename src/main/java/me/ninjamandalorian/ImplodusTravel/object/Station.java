package me.ninjamandalorian.ImplodusTravel.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;
import me.ninjamandalorian.ImplodusTravel.Logger;
import me.ninjamandalorian.ImplodusTravel.controller.PersistentDataController;
import me.ninjamandalorian.ImplodusTravel.controller.PlayerController;
import me.ninjamandalorian.ImplodusTravel.data.StationDataManager;
import me.ninjamandalorian.ImplodusTravel.event.PreTransportEvent;
import me.ninjamandalorian.ImplodusTravel.exceptions.ChatSettingException;
import me.ninjamandalorian.ImplodusTravel.settings.Settings;
import net.milkbowl.vault.economy.Economy;

/**
 * Station class is the object for transport locations. <p>
 * Contains both basic data & owner options
 */
public class Station implements ChatSettable {
    
    // Static Fields
    private static HashMap<UUID, Station> stations = new HashMap<>();

    // Instance fields
    private UUID id; // Station id
    private String displayName; // Station display name
    private OfflinePlayer owner; // Station owner
    private Location stationLocation; // Station location
    private Location teleportLocation; // Station's tp location
    private ArrayList<UUID> destinationStations = new ArrayList<>(); // Unlocked destinations

    private double defaultCost; // Cost without any multipliers (editable)
    private HashMap<String, Double> rankMultMap = new HashMap<>();

    /** Constructor method
     * @param id
     * @param displayName
     * @param owner
     * @param location
     */
    public Station(UUID id, String displayName, OfflinePlayer owner, Location location, Location destination) {
        this.id = id;
        this.displayName = displayName;
        this.owner = owner;
        this.stationLocation = location;
        this.teleportLocation = destination;

        // Ensure unique UUID for new station
        while (stations.containsKey(this.id)) {
            this.id = UUID.randomUUID();
        }

        this.defaultCost = Settings.getDefaultTravelCost();
        
        stations.put(this.id, this); // Add to station registry
    }

    /** UUID accessor
     * Get the id of the station
     * @return station id
     */
    public UUID getId() {
        return id;
    }

    /** UUID string accessor
     * Get the id of the station as a string
     * @return station id
     */
    public String getIdString() {
        return id.toString();
    }
    
    /** Display name accessor
     * Gets the display name of the station
     * @return station display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /** Owner OfflinePlayer accessor
     * Gets the current owner of the station
     * @return station owner
     */
    public OfflinePlayer getOwner() {
        return owner;
    }

    /**
     * Gets the station location
     * @return station location
     */
    public Location getStationLocation() {
        return stationLocation;
    }

    /**
     * Gets location for teleport
     * @return station teleport
     */
    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public ArrayList<UUID> getDestinations() {
        return destinationStations;
    }

    /** Adds destinations depending on maps in player inventory.
     * @param player - Player trying to add maps
     */
    public void addDestination(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        UUID itemUUID = PersistentDataController.getTokenUUID(item);
        if (itemUUID != null) {
            destinationStations.add(itemUUID);
            Logger.quietLog(player.getName() + " added " + itemUUID.toString() + " to " + id.toString());
            player.getInventory().remove(item);
            Station addedStation = Station.getStation(itemUUID);
            if (addedStation == null) player.sendMessage(ChatColor.GREEN + "Station added.");
            else player.sendMessage(ChatColor.GREEN + "You added " + addedStation.getDisplayName() + " to the station.");
        } else {
            player.sendMessage(ChatColor.RED + "Please hold a valid map.");
        }
    }

    public void addDestination(UUID uuid) {
        destinationStations.add(uuid);
    }

    public void addDestination(Station station) {
        destinationStations.add(station.getId());
    }

    /**
     * Transfer ownership to new player
     * @param newPlayer - New owner
     */
    public void transferOwnership(OfflinePlayer newPlayer) {
        Logger.log("Station " + this.getIdString() + " transferred from " + this.owner.getUniqueId() + " to " + newPlayer.getUniqueId());
        this.owner = newPlayer;
    }

    /**
     * Gets all stations within a range of this station
     * @param range maximum distance (circular)
     * @return
     */
    public ArrayList<Station> getStationsInRange(Double range) {
        return getStationsInRange(this, range);
    }

    public void teleportPlayer(Player player, Station source) {
        // TODO debug teleport
        if (isBlacklisted(player)) return;

        if (PlayerController.isPlayerTeleporting(player)) {
            player.sendMessage(ChatColor.RED + "You already have a pending teleport.");
            return;
        }

        Double cost = getCost(player);
        Economy econ = ImplodusTravel.getEcon();
        if (econ.getBalance(player) < cost) {
            player.sendMessage(ChatColor.RED + "You do not have enough money to travel here.");
            return;
        }
        econ.withdrawPlayer(player, cost);
        player.sendMessage(ChatColor.GREEN + "You have paid " + econ.format(cost) + " to travel to " + this.displayName);

        PreTransportEvent preEvent = new PreTransportEvent(player, source, this);
        Bukkit.getServer().getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled()) return;
        PlayerController.startTeleport(player, this.teleportLocation, 3); // Change to some different request system whereby cost is saved and refunded
    }

    /////////////
    // Economy //
    /////////////

    public double getDefaultCost() {
        return defaultCost;
    }

    public void setDefaultCost(double defaultCost) {
        this.defaultCost = defaultCost;
    }

    public double getCost(Player player) {
        return defaultCost * Math.max(0, getRankMult(player));
    }
    

    ////////////////////////////////
    // Rank Section (%/Blacklist) //
    ////////////////////////////////

    public String getPlayerRank(Player player) {
        if (!ImplodusTravel.isTownyInstalled()) {
            return "neutral";
        }

        if (TownyAPI.getInstance().getResident(owner.getUniqueId()) == null) return "neutral";

        // Towny : towny_town, towny_nation, towny_ally, neutral (default), towny_enemy
        Town ownerTown = TownyAPI.getInstance().getResident(owner.getUniqueId()).getTownOrNull();
        Town playerTown = TownyAPI.getInstance().getTown(player);
        if (ownerTown == null || playerTown == null) return "neutral";
        if (ownerTown.equals(playerTown)) return "towny_town";
        if (!ownerTown.hasNation() || !playerTown.hasNation()) return "neutral"; // Must be in a nation to have allies or enemies
        if (ownerTown.getNationOrNull().equals(playerTown.getNationOrNull())) return "towny_nation";
        if (ownerTown.getNationOrNull().hasAlly(playerTown.getNationOrNull())) return "towny_ally";
        if (ownerTown.getNationOrNull().hasEnemy(playerTown.getNationOrNull())) return "towny_enemy";
        return "neutral";
    }

    public boolean isBlacklisted(String rankName) {
        return getRankMult(rankName) < 0;
    }

    public boolean isBlacklisted(Player player) {
        return isBlacklisted(getPlayerRank(player));
    }

    public Double getRankMult(String rankName) {
        return rankMultMap.containsKey(rankName) ? rankMultMap.get(rankName) : 1.0;
    }

    public Double getRankMult(Player player) {
        return getRankMult(getPlayerRank(player));
    }

    public void setRankMult(String rankName, double mult) {
        rankMultMap.put(rankName, mult);
    }

    public HashMap<String, Double> getRankMultMap() {
        return rankMultMap;
    }

    //////////////////////
    // Settings Editing //
    //////////////////////
    @Override
    public boolean setSetting(String setting, Object value) throws ChatSettingException {
        // TODO add parts
        // default station cost; throw SettingsError
        String[] settingDir = setting.split("\\.");
        switch (settingDir[0]) {
            case "rename":
                renameSetting((String) value);
                break;
            case "rank":
                rankSetting(setting.substring(5), (String) value);
                break;
            case "defaultCost":
                defaultCostSetting((String) value);
                break;
            default:
                break;
        }
        
        return true;
    }

    private void renameSetting(String newName) {
        newName = newName.replaceAll("[^a-zA-Z0-9\\s]", "");
        newName = newName.stripLeading();
        newName = newName.stripTrailing();
        Logger.log(this.id.toString() + "changing name from " + this.displayName + " to " + newName);
        this.displayName = newName;
        save();
    }

    private void rankSetting(String rank, String value) throws ChatSettingException {
        if (value.toLowerCase().strip().startsWith("blacklist")) {
            rankMultMap.put(rank, -1.0);
        } else {
            value = value.replaceAll("[^0-9.-]", "");
            Double doubleValue = null;
            try { doubleValue = Double.parseDouble(value); } catch (Exception e) {
            };
            if (doubleValue == null) throw new ChatSettingException("Invalid value.");
            if (doubleValue < 0) {
                rankMultMap.put(rank, -1.0);
            } else if (doubleValue < 1) {
                rankMultMap.put(rank, doubleValue);
            } else {
                rankMultMap.put(rank, doubleValue / 100);
            }
            for (String key : rankMultMap.keySet()) {
                Logger.log(key + " " + rankMultMap.get(key));
            }
        }
        save();
    }

    private void defaultCostSetting(String value) throws ChatSettingException {
        value = value.replaceAll("[^0-9.-]", "");
        Double doubleValue = null;
        try { doubleValue = Double.parseDouble(value); } catch (Exception e) {};
        if (doubleValue == null) throw new ChatSettingException("Invalid value.");
        this.defaultCost = doubleValue;
    }

    public void save() {
        try {
            StationDataManager.saveStation(this);
        } catch (Exception e) {
            Logger.log("Failed to save " + id.toString() + " - " + e.getMessage());
        }
    }

    public void delete() {
        stations.remove(this.id);
        try {
            StationDataManager.deleteStation(this);
        } catch (Exception e) {
            Logger.log("Failed to delete " + id.toString() + " - " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Station{"+this.id.toString()+"}";
    }

    // Static methods

    public static Station getStation(UUID uuid) {
        return stations.get(uuid);
    }

    public static Station getStation(Location location) { 
        for (Station station : stations.values()) {
            if (station.getStationLocation().equals(location)) return station;
        }
        return null;
    }

    public static ArrayList<Station> getPlayerStations(OfflinePlayer player) {
        ArrayList<Station> returnList = new ArrayList<>();

        for (Station station : stations.values()) {
            try{
            if (station.getOwner().equals(player)) returnList.add(station);
            } catch (Exception e) {Logger.log("Station " + station.getIdString() + " error: " + e.getMessage());};
        }

        return returnList;
    }

    public static Collection<Station> getStations() {
        return stations.values();
    }

    public static ArrayList<Station> getStationsInRange(Station station, Double range) {
        return getStationsInRange(station.getStationLocation(), range);
    }

    private static ArrayList<Station> getStationsInRange(Location loc, Double range) {
        ArrayList<Station> returnList = new ArrayList<>();

        for (Station station : stations.values()) {
            try {
                if (station.getTeleportLocation().getWorld().equals(loc.getWorld())) {
                    if (station.getTeleportLocation().distance(loc) <= range) returnList.add(station);
                }
            } catch (Exception e) {Logger.log("Station " + station.getIdString() + " error: " + e.getMessage());};
        }

        return returnList;
    }

}
