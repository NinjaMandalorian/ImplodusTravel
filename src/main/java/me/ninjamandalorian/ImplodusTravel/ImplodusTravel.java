package me.ninjamandalorian.ImplodusTravel;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ninjamandalorian.ImplodusTravel.command.*;
import me.ninjamandalorian.ImplodusTravel.controller.PlayerController;
import me.ninjamandalorian.ImplodusTravel.data.DataManager;
import me.ninjamandalorian.ImplodusTravel.data.StationDataManager;
import me.ninjamandalorian.ImplodusTravel.listener.BlockListener;
import me.ninjamandalorian.ImplodusTravel.listener.InventoryListener;
import me.ninjamandalorian.ImplodusTravel.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;

public class ImplodusTravel extends JavaPlugin {

    private static ImplodusTravel instance;
    public static Economy econ;


    @Override
    public void onEnable() {
        instance = this;

        setupEconomy();

        new DataManager();
        new ImplodusTravelCommand();
        new PlayerController(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), instance); 
        pluginManager.registerEvents(new InventoryListener(), instance);
        pluginManager.registerEvents(new BlockListener(), instance);
    }

    @Override
    public void onDisable() {
        StationDataManager.saveAllStations();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static ImplodusTravel getInstance() {
        return instance;
    }

    public static Economy getEcon() {
        return econ;
    }

}