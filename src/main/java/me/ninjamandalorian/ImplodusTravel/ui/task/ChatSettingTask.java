package me.ninjamandalorian.ImplodusTravel.ui.task;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.listener.AsyncChatListener;
import me.ninjamandalorian.ImplodusTravel.object.ChatSettable;
import me.ninjamandalorian.ImplodusTravel.object.SettingRequest;
import net.md_5.bungee.api.ChatColor;

public class ChatSettingTask implements BaseTask{

    private SettingRequest request;

    public ChatSettingTask(Player player, ChatSettable settee, String setting) {
        this.request = new SettingRequest(player, settee, setting);
    }

    @Override
    public void run(InventoryClickEvent event) {
        AsyncChatListener.startRequest(request);
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();
        player.sendMessage(ChatColor.GREEN + "Enter value:");
    }
    
}
