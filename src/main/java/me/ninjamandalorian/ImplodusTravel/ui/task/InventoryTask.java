package me.ninjamandalorian.ImplodusTravel.ui.task;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;
import me.ninjamandalorian.ImplodusTravel.ui.object.Buildable;

public class InventoryTask implements BaseTask {

    private BaseMenu menu;
    private Buildable builder;
    
    public InventoryTask(BaseMenu menu) {
        this.menu = menu;
    }

    public InventoryTask(Buildable builder) {
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
