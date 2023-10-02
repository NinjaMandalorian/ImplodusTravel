package me.ninjamandalorian.ImplodusTravel.settings;

import org.bukkit.configuration.file.FileConfiguration;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;

public class Settings {
    
    private static ImplodusTravel plugin;
    private static FileConfiguration config;

    public static void init() {
        plugin = ImplodusTravel.getInstance();
        config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public static void reloadConfig() {
        plugin = ImplodusTravel.getInstance();

        plugin.reloadConfig();
        plugin.saveDefaultConfig();
    }

    public static double getDefaultTravelCost() {
        return config.getDouble("costs.default_travel");
    }

    public static double getDefaultMapCost() {
        return config.getDouble("costs.default_map");
    }

    public static double getDefaultStationCost() {
        return config.getDouble("costs.default_station");
    }

    public static int getSettingTimeout() {
        return config.getInt("setting_timeout");
    }

    public static boolean isDebugMode() {
        return config.getBoolean("debug_mode");
    }

    public static int getMaxStationsPerPerson() {
        return config.getInt("stations_per_person");
    }
}
