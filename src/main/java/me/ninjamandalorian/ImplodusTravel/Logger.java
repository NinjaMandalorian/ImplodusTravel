package me.ninjamandalorian.ImplodusTravel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;

public class Logger {

    private static File logFolder;

    public static void log(String string) {
        Bukkit.getLogger().info("[ImplodusTravel] " + string);
        quietLog("[INFO] " + string);
    }
    
    public static void warn(String string) {
        quietLog("[WARN] " + string);
    }
    
    public static void quietLog(String string) {
        String logMsg = formatMsg(string);
        fileLog(logMsg);    
    }

    public static void debug(String string) {
        Bukkit.getLogger().info("[IT-DEBUG] " + string);
        quietLog("[DEBUG-MODE] " + string);
    }

    private static void fileLog(String logMsg) {
        if (logFolder == null) logFolder = generateFolder();
        File logFile = new File(logFolder, "latest.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Encountered error when creating log file");
                return;
            }
        }
        try {
            FileOutputStream stream = new FileOutputStream(logFile, true);
            stream.write(logMsg.getBytes());
            stream.close();
        } catch (IOException e) {
            return;
        }
    }
    
    private static File generateFolder() {
        File folderFile = new File("plugins" + File.separator + "ImplodusTravel");
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        return folderFile;
    }

    private static String formatMsg(String msg) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String logMsg = "[" + format.format(date) + "] " + msg + "\n";
        return logMsg;
    }
}
