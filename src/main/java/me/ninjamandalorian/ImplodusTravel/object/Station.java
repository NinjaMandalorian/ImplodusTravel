package me.ninjamandalorian.ImplodusTravel.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import me.ninjamandalorian.ImplodusTravel.Logger;

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

    /**
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

    /**
     * Get the id of the station
     * @return station id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get the id of the station as a string
     * @return station id
     */
    public String getIdString() {
        return id.toString();
    }
    
    /**
     * Gets the display name of the station
     * @return station display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
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

    /**
     * Transfer ownership to new player
     * @param newPlayer - New owner
     */
    public void transferOwnership(OfflinePlayer newPlayer) {
        Logger.log("Station " + this.getIdString() + " transferred from " + this.owner.getUniqueId() + " to " + newPlayer.getUniqueId());
        this.owner = newPlayer;
    }

    @Override
    public String toString() {
        return "";
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
}
