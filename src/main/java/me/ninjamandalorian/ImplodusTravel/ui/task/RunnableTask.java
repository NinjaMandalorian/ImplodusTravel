package me.ninjamandalorian.ImplodusTravel.ui.task;

import org.bukkit.event.inventory.InventoryClickEvent;

public class RunnableTask implements BaseTask {

    private Runnable runnable;
    private boolean closeMenu = false;

    public RunnableTask(Runnable runnable) {
        this.runnable = runnable;
    }    

    public RunnableTask closeMenu() {
        closeMenu = !closeMenu;
        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        runnable.run();
        if (closeMenu) event.getWhoClicked().closeInventory();
    }
    
}
