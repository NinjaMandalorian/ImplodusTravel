package me.ninjamandalorian.ImplodusTravel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.ninjamandalorian.ImplodusTravel.ui.object.BaseButton;
import me.ninjamandalorian.ImplodusTravel.ui.object.BaseMenu;

/** Listens for inventory updates (for UI)
 *  @author NinjaMandalorian
 */
public class InventoryListener implements Listener{

    /** Responds to inventory click
     * @param e - Event
     */
    @EventHandler
    public void onInventoryCLick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof BaseMenu menu) { // Ignores if not a BaseMenu
            e.setCancelled(true); // Cancels the item pick-up

            int slotNum = e.getRawSlot();
            if (slotNum + 1 > e.getInventory().getSize()) return;

            BaseButton button = menu.getButton(slotNum);
            if (button != null) button.run(e); // Runs relevant button slot
        }
    }
}
