package me.ninjamandalorian.ImplodusTravel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class ImplodusTravel extends JavaPlugin {

    private static ImplodusTravel instance;
    public static Economy econ;


    @Override
    public void onEnable() {
        instance = this;

        setupEconomy();
        PluginManager pluginManager = getServer().getPluginManager();
    }

    @Override
    public void onDisable() {

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