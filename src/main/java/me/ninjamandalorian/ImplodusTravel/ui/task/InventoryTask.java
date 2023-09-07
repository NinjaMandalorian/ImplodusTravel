package me.ninjamandalorian.ImplodusTravel.ui.task;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;

public class InventoryTask implements BaseTask {

    private BaseMenu menu;
    
    public InventoryTask(BaseMenu menu) {
        this.menu = menu;
    }
    
    @Override
    public void run(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        menu.open(player);
    }

}
