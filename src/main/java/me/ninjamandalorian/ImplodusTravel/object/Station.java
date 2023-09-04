package me.ninjamandalorian.ImplodusTravel.object;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Banner;

/**
 * Station class is the object for transport locations. <p>
 * Contains both basic data & owner options
 */
public class Station {
    
    private String id; // Station id
    private String displayName; // Station display name
    private OfflinePlayer owner; // Station owner
    private Banner stationBanner; // Station banner
    private Location teleportLocation; // Station's tp location

    /**
     * Create a station
     * @param id
     * @param displayName
     * @param owner
     * @param stationBanner
     */
    public Station(String id, String displayName, OfflinePlayer owner, Banner stationBanner) {
        this.id = id;
        this.displayName = displayName;
        this.owner = owner;
        this.stationBanner = stationBanner;
    }

    /**
     * Get the id of the station
     * @return station id
     */
    public String getId() {
        return id;
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
     * Gets the station's banner
     * @return station banner
     */
    public Banner getStationBanner() {
        return stationBanner;
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
        // TODO Log transfer
        this.owner = newPlayer;
    }

}
