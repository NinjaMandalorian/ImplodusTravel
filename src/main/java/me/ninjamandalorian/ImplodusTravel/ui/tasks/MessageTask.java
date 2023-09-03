package me.ninjamandalorian.ImplodusTravel.ui.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MessageTask implements BaseTask {

    private String message;
    
    public MessageTask(String msg) {
        this.message = msg;
    }
    
    @Override
    public void run(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        player.sendMessage(message);
    }

}
