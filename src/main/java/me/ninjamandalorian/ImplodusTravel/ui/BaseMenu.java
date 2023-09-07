package me.ninjamandalorian.ImplodusTravel.ui;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.ninjamandalorian.ImplodusTravel.ui.tasks.BaseTask;
import net.md_5.bungee.api.ChatColor;

public class BaseMenu implements InventoryHolder {
    
    // Menu fields
    protected Inventory inventory;
    protected HashMap<Integer,BaseButton> menuButtons;
    protected String openMessage = null;
    
    // MENU FUNCTIONS //
    
    /**
     * Opens the menu for a specific player.
     * @param player - Player to have menu
     */
    public void open(Player player) {
        player.openInventory(inventory);
        if (this.openMessage != null) player.sendMessage(ChatColor.translateAlternateColorCodes('&', openMessage));
    }
    
    /**
     * Gets the inventory of the menu
     */
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
    
    
    /**
     * Gets button, indexed from 0:(size-1)
     * @param slotNum - Slot number
     * @return Button of slot or null if empty.
     */
    public BaseButton getButton(int slotNum) {
        if (slotNum > this.inventory.getSize() - 1) return null;
        return this.menuButtons.get(slotNum);
    }
    
    // BUILDERS FOR MENUS //
    
    /**
     * Created a non-paged builder
     * @return Builder
     */
    public static Builder createBuilder() {
        return new Builder();
    }
    
    /**
     * Menu Builder
     */
    public static class Builder {
        private String menuTitle = "ip-title";
        private int menuSize = 54;
        private HashMap<Integer,BaseButton> menuButtons = new HashMap<Integer,BaseButton>();
        private String openMsg = null;
        
        public Builder() {}
        
        public Builder title(String title) {
            this.menuTitle = ChatColor.translateAlternateColorCodes('&', title);
            return this;
        }
        
        public Builder rows(int num) {
            this.menuSize = 9 * Math.min(num, 6);
            return this;
        }
        
        public Builder setButton(int slot, BaseButton button) {
            this.menuButtons.put(slot, button);
            return this;
        }
        
        // So you don't have to do a useless instance creation in the building phase.
        public Builder setButton(int slot, ItemStack itemStack, BaseTask task) {
            this.menuButtons.put(slot, BaseButton.create().itemStack(itemStack).task(task));
            return this;
        }
        
        public BaseMenu build() {
            return new BaseMenu(this);
        }

        public Builder fillRow(int row) {
            
            for (int i = 0; i < 9; i++) {
                int slot = 9*row + i;
                if (!this.menuButtons.containsKey(slot)) {
                    this.menuButtons.put(slot, BaseButton.background());
                }
            }
            
            return this;
        }
        
        public Builder fillColumn(int column) {
            
            for (int i = 0; i < (this.menuSize/9); i++) {
                int slot = 9*i +column;
                if (!this.menuButtons.containsKey(slot)) {
                    this.menuButtons.put(slot, BaseButton.background());
                }
            }
            
            return this;
        }
        
        public Builder fillOutline() {
            return this.fillColumn(0).fillColumn(8).fillRow(0).fillRow((this.menuSize/9)-1);
        }
        
        public Builder openMsg(String msg) {
            this.openMsg = msg;
            return this;
        }
    }
    
    /**
     * Builds menu using non-paged builder
     * @param builder - Builder used
     */
    private BaseMenu(Builder builder) {
        this.inventory = Bukkit.createInventory(this, builder.menuSize, builder.menuTitle);
        
        // Creates all items in inventory
        for (Entry<Integer, BaseButton> buttonEntry : builder.menuButtons.entrySet()) {
            if (buttonEntry.getKey() >= builder.menuSize) continue;
            inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItemStack() );
        }
        
        this.menuButtons = builder.menuButtons;
        this.openMessage = builder.openMsg;
    }

    /**
     * Constructor for extensions (PagedMenu)
     */
    protected BaseMenu() {}    
        
    // }
}
