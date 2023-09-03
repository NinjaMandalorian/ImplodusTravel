package me.ninjamandalorian.ImplodusTravel.ui.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.BaseMenu;

public class PageTask implements BaseTask {
    
    private int direction;
    
    public PageTask(int dir) {
        this.direction = dir;
    }
    
    @Override
    public void run(InventoryClickEvent event) {
        BaseMenu menu = (BaseMenu) event.getInventory().getHolder();
        menu.changePage((Player) event.getWhoClicked(), direction);
    }
}
