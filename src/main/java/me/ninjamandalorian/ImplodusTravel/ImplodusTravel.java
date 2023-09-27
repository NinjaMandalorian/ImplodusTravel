package me.ninjamandalorian.ImplodusTravel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ninjamandalorian.ImplodusTravel.command.*;
import me.ninjamandalorian.ImplodusTravel.controller.PlayerController;
import me.ninjamandalorian.ImplodusTravel.data.DataManager;
import me.ninjamandalorian.ImplodusTravel.data.StationDataManager;
import me.ninjamandalorian.ImplodusTravel.listener.AsyncChatListener;
import me.ninjamandalorian.ImplodusTravel.listener.BlockListener;
import me.ninjamandalorian.ImplodusTravel.listener.InventoryListener;
import me.ninjamandalorian.ImplodusTravel.listener.PlayerListener;
import me.ninjamandalorian.ImplodusTravel.settings.Settings;
import net.milkbowl.vault.economy.Economy;

public class ImplodusTravel extends JavaPlugin {

    private static ImplodusTravel instance;
    public static Economy econ;

    private static boolean townyInstalled;

    @Override
    public void onEnable() {
        instance = this;

        setupEconomy();

        Settings.init();
        new DataManager();
        new ImplodusTravelCommand();
        new PlayerController(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), instance); 
        pluginManager.registerEvents(new InventoryListener(), instance);
        pluginManager.registerEvents(new BlockListener(), instance);
        pluginManager.registerEvents(new AsyncChatListener(), instance);

        townyInstalled = (Bukkit.getPluginManager().getPlugin("Towny") != null);
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

    public static boolean isTownyInstalled() {
        return townyInstalled;
    }

}