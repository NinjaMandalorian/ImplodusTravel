package me.ninjamandalorian.ImplodusTravel;

import org.bukkit.Bukkit;

public class Logger {
    public static void log(String string) {
        Bukkit.getLogger().info("[ImplodusTravel] " + string);
        quietLog("[INFO] " + string);
    }
    
    public static void warn(String string) {
        quietLog("[WARN] " + string);
    }
    
    public static void quietLog(String string) {
        // TODO Insert .txt logging system
    }

    public static void debug(String string) {
        Bukkit.getLogger().info("[IT-DEBUG] " + string);
        quietLog("[DEBUG-MODE] " + string);
    }
}
