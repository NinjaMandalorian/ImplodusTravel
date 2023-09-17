package me.ninjamandalorian.ImplodusTravel.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.ninjamandalorian.ImplodusTravel.Logger;
import me.ninjamandalorian.ImplodusTravel.controller.PlayerController;
import me.ninjamandalorian.ImplodusTravel.event.PreTransportEvent;

/**
 * Station class is the object for transport locations. <p>
 * Contains both basic data & owner options
 */
public class Station {
    
    // Static Fields
    private static HashMap<UUID, Station> stations = new HashMap<>();

    // Instance fields
    private UUID id; // Station id
    private String displayName; // Station display name
    private OfflinePlayer owner; // Station owner
    private Location stationLocation; // Station location
    private Location teleportLocation; // Station's tp location
    private ArrayList<UUID> destinationStations = new ArrayList<>(); // Unlocked destinations

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
        // add wait time w/ movement cancel
        // add permissions
        PreTransportEvent preEvent = new PreTransportEvent(player, source, this);
        Bukkit.getServer().getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled()) return;
        PlayerController.startTeleport(player, this.teleportLocation, 3);
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
            if (station.getOwner().equals(player)) returnList.add(station);
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
            if (station.getTeleportLocation().getWorld().equals(loc.getWorld())) {
                if (station.getTeleportLocation().distance(loc) <= range) returnList.add(station);
            }
        }

        return returnList;
    }

}
