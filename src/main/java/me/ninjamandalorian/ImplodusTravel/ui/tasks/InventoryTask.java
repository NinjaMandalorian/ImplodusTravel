package me.ninjamandalorian.ImplodusTravel.ui.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.BaseMenu;

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
