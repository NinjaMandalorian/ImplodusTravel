package me.ninjamandalorian.ImplodusTravel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ImplodusTravel extends JavaPlugin {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(getName() + " enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(getName() + " disabled.");
    }

}