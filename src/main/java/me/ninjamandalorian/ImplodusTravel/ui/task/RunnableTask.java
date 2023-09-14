package me.ninjamandalorian.ImplodusTravel.ui.task;

import org.bukkit.event.inventory.InventoryClickEvent;

public class RunnableTask implements BaseTask {

    Runnable runnable;

    public RunnableTask(Runnable runnable) {
        this.runnable = runnable;
    }    

    @Override
    public void run(InventoryClickEvent event) {
        runnable.run();
    }
    
}
