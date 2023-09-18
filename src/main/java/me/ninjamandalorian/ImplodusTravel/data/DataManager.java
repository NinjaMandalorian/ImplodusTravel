package me.ninjamandalorian.ImplodusTravel.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import me.ninjamandalorian.ImplodusTravel.Logger;

public class DataManager {
	
    private static String dataPath = "plugins" + File.separator + "ImplodusTravel" + File.separator + "data";
    private static DumperOptions options;
    
	public DataManager() {
		initData();
	}

	private static void initData() {
        File dataFolder = new File(dataPath);
        if (!dataFolder.exists()) { 
            dataFolder.mkdirs();
        }

        List<String> list = Arrays.asList(new String[]{"stations"});
        // Creates nodes and custom-types folders
        for (int i = 0; i < list.size(); i++) {
            File tempFolder = new File(dataFolder, list.get(i));
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }
        }

        StationDataManager.initStationFiles(dataPath);
    }
	
	/**
	 * Saves data into a .yml format
	 * @param filePath - File's path after ImplodusTravel/Data/
	 * @param map - Map to be saved
	 */
	public static void saveData(String filePath, HashMap<String,Object> map) {
        File file = getFile(filePath);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);

            if (options == null) {
                options = new DumperOptions();
                options.setIndent(2);
                options.setPrettyFlow(true);
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            }
            
            Yaml yaml = new Yaml(options);
            yaml.dump(map, writer);
            writer.close();
        } catch (FileNotFoundException e) {
            Logger.log("Encountered error when creating PrintWriter for " + filePath);
        }
	}
	
	/**
	 * Gets the data back as a HashMap
	 * @param filePath - FilePath of data
	 * @return HashMap of data or null
	 */
	public static HashMap<String,Object> getData(String filePath) {
        
        File file = getFile(filePath);
        if (!file.exists()) {
            Logger.log("Attempted to retrieve non-existent file: " + filePath);
            return null;
        }
        
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("[ImplodusTravel] Encountered error when creating FileInputStream for " + filePath);
            return null;
        }
        Yaml yaml = new Yaml();
        HashMap<String, Object> data = yaml.load(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        
        return data;
	}
	
	public static boolean deleteFile(String filePath) {
        File file = getFile(filePath);
        
        if (!file.exists()) {
            Logger.log("Attempted to retrieve non-existent file: " + filePath + ".yml");
            return false;
        }
        
        try {
            return Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	/**
	 * Converts filePath into File
	 * @param path - File's path within data folder
	 * @return File or null
	 */
	private static File getFile(String path) {
        path = dataPath + File.separator + path;
        
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Logger.log("Encountered error when creating file - " + path);
                return null;
            }
        }
        return file;
    }
	
}
