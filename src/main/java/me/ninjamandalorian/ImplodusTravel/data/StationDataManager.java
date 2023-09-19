package me.ninjamandalorian.ImplodusTravel.data;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import me.ninjamandalorian.ImplodusTravel.Logger;
import me.ninjamandalorian.ImplodusTravel.object.Station;

public class StationDataManager {
    

    static List<Station> initStationFiles(String dataFolderPath) {
        ArrayList<Station> returnList = new ArrayList<>();
        
        File folder = new File(dataFolderPath + File.separator + "stations");

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File f, String name) {
                // Ignores non .yml files
                return name.endsWith(".yml");
            }
        };

        File[] files = folder.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            try {
            HashMap<String,Object> map = DataManager.getData("stations" + File.separator + files[i].getName());
            Station station = mapToStation(map);
            returnList.add(station);
            } catch (Exception e) {
                Logger.warn("Caught error when producing station " + files[i].getName());
                Logger.debug("ERROR - " + files[i].getName() + " " + e.getMessage());
            }
        }

        return returnList;
    }

    @SuppressWarnings("unchecked")
    private static Station mapToStation(HashMap<String, Object> hashMap) {

        // Getting variables for constructor
        UUID uuid = UUID.fromString((String) hashMap.get("uuid"));
        String displayName = (String) hashMap.get("displayName");
        UUID ownerUUID = UUID.fromString((String) hashMap.get("owner")); // Getting UUID of owner separately.
        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerUUID);
        Location location = stringToLocation((String) hashMap.get("location"));
        Location destination = stringToLocation((String) hashMap.get("destination"));

        Station station = new Station(uuid, displayName, owner, location, destination);

        if (hashMap.containsKey("destinationStations")) {
            List<String> destinationList = (List<String>) hashMap.get("destinationStations");
            for (String string : destinationList) {
                try {
                UUID destUuid = UUID.fromString(string);
                station.addDestination(destUuid);
                } catch (IllegalArgumentException e) {}
            }
        }
        return station;
    }

    private static HashMap<String, Object> stationToMap(Station station) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("uuid", station.getId().toString());
        map.put("displayName", station.getDisplayName());
        map.put("owner", station.getOwner().getUniqueId().toString());
        map.put("location", locationToString(station.getStationLocation()));
        map.put("destination", locationToString(station.getTeleportLocation()));

        if (station.getDestinations().size() > 0) {
            ArrayList<String> destinationList = new ArrayList<>();
            for (UUID id : station.getDestinations()) {
                destinationList.add(id.toString());
            }
            map.put("destinationStations", destinationList);
        }

        return map;
    }

    private static Location stringToLocation(String string) {
        // Format: "Overworld:123.0:456.0:789.0:90.0:0.0:"
        String[] stringList = string.split(":");
        World world = Bukkit.getWorld(UUID.fromString(stringList[0]));
        if (stringList.length == 6) {
            return new Location(world, Double.parseDouble(stringList[1]), Double.parseDouble(stringList[2]), Double.parseDouble(stringList[3]), Float.parseFloat(stringList[4]), Float.parseFloat(stringList[4]));
        } else if (stringList.length == 4) {
            return new Location(world, Double.parseDouble(stringList[1]), Double.parseDouble(stringList[2]), Double.parseDouble(stringList[3]));
        }
        return null;
    }

    private static String locationToString(Location location) {
        return location.getWorld().getUID().toString() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static void saveStation(Station station) throws Exception {
        HashMap<String, Object> stationMap = stationToMap(station);

        String filePath = "stations" + File.separator + station.getId().toString() + ".yml";
        DataManager.saveData(filePath, stationMap);
    }

    public static void saveAllStations() {
        for (Station station : Station.getStations()) {
            try {
                saveStation(station);
            } catch (Exception e) {
                Logger.log("Failed to save station - " + station.getId().toString() + " - " + e.getMessage());
            }
        }
    }

    public static void deleteStation(Station station) throws Exception {
        DataManager.deleteFile("stations" + File.separator + station.getIdString() + ".yml");
    }

}
