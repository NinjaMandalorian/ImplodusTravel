package me.ninjamandalorian.ImplodusTravel.ui.task;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu.Builder;

public class InventoryTask implements BaseTask {

    private BaseMenu menu;
    private Builder builder;
    
    public InventoryTask(BaseMenu menu) {
        this.menu = menu;
    }

    public InventoryTask(Builder builder) {
        this.builder = builder;
    }
    
    @Override
    public void run(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (builder != null) {
            menu = builder.build();
        }
        menu.open(player);
    }

}
