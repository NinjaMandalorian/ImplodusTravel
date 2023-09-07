package me.ninjamandalorian.ImplodusTravel.ui.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.PagedMenu;

public class PageTask implements BaseTask {
    
    private int direction;
    
    public PageTask(int dir) {
        this.direction = dir;
    }
    
    @Override
    public void run(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof PagedMenu)) {
            return;
        }
        PagedMenu menu = (PagedMenu) event.getInventory().getHolder();
        menu.changePage((Player) event.getWhoClicked(), direction);
    }
}
