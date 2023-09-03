package me.ninjamandalorian.ImplodusTravel.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.ninjamandalorian.ImplodusTravel.ImplodusTravel;

public class ImplodusTravelCommand implements CommandExecutor {

    public ImplodusTravelCommand() {
        ImplodusTravel plugin = ImplodusTravel.getInstance();
        plugin.getCommand("implodustravel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args[0].toLowerCase()) {
        }
        

        return true;
    }
    
}
