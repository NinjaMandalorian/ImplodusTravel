package me.ninjamandalorian.ImplodusTravel.object;

import java.util.HashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.ninjamandalorian.ImplodusTravel.Logger;

public class TravelNetwork {
    
    // Network Registry
    private static HashSet<TravelNetwork> networks = new HashSet<TravelNetwork>();
    private static TravelNetwork defaultNetwork = new TravelNetwork("default", null);

    // Static Initializer
    {
        networks.add(defaultNetwork);
    }

    // Object Fields
    private String name;
    private World world;
    private HashSet<Station> stations;
    
    public TravelNetwork(String name, World world, HashSet<Station> stations) {
        this.name = name;
        this.world = world;
        this.stations = stations;

        networks.add(this);
    }

    public TravelNetwork(String name, World world) {
        this(name, world, new HashSet<Station>());
    }

    public TravelNetwork(Map<String, Object> map) {
        this((String) map.get("name"), Bukkit.getWorld((String) map.get("world")));
    }
    
    public String getName() {
        return name;
    }
    
    public World getWorld() {
        return world;
    }
    
    public HashSet<Station> getStations() {
        return stations;
    }
    
    public void addStation(Station station) {
        stations.add(station);
    }

    public void removeStation(Station station) {
        stations.remove(station);
    }

    ////////////////////////////////////////
    // STATIC METHODS                     //
    ////////////////////////////////////////

    /**
     * Get all networks
     * @return Set of networks
     */
    public static HashSet<TravelNetwork> getNetworks() {
        return networks;
    }

    /**
     * Get a network by name
     * @param name - Network name
     * @return Network
     */
    public static TravelNetwork getNetwork(String name) {
        for (TravelNetwork network : networks) {
            if (network != null && network.getName().equals(name)) {
                return network;
            }
        }
        return defaultNetwork;
    }

    public static void addNetwork(TravelNetwork network) {
        Logger.debug("Adding network: " + network.getName());
        networks.add(network);
    }

}
