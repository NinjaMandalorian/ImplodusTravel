package me.ninjamandalorian.ImplodusTravel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.object.BaseButton;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;

public class InventoryListener implements Listener{


    @EventHandler
    public void onInventoryCLick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof BaseMenu menu) {
            e.setCancelled(true);

            int slotNum = e.getRawSlot();
            if (slotNum + 1 > e.getInventory().getSize()) return;

            BaseButton button = menu.getButton(slotNum);
            if (button != null) button.run(e);
        }
    }
}
