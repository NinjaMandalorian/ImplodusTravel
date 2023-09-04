package me.ninjamandalorian.ImplodusTravel.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ninjamandalorian.ImplodusTravel.ui.tasks.BaseTask;
import me.ninjamandalorian.ImplodusTravel.ui.tasks.MessageTask;
import net.md_5.bungee.api.ChatColor;

/**
 * Represents a button in the Inventory Menu
 * Contains the ItemStack, BaseTask & Any other metadata.
 * @author NinjaMandalorian
 */
public class BaseButton {
    
    private ItemStack itemStack;
    private BaseTask task;
    private HashMap<String,String> metadata;
    
    private BaseButton() {};
    
    /**
     * Gets the button's task
     * @return
     */
    public BaseTask getTask() {
        return this.task;
    }
    
    /**
     * Gets the metadata of the button
     * @return
     */
    public HashMap<String, String> getMetadata() {
        return this.metadata;
    }
    
    /**
     * Sets the metadata of the button
     * @param map
     */
    public void setMetadata(HashMap<String, String> map) {
        this.metadata = map;
    }
    
    /**
     * Gets the item stack of button
     * @return
     */
    public ItemStack getItemStack() { 
        return this.itemStack;
    }
    
    // Constructors for building
    
    public static BaseButton create() {
        BaseButton button = new BaseButton();
        button.itemStack = new ItemStack(Material.GLASS);
        button = button.name(ChatColor.GRAY + "Empty");
        button.task = new MessageTask("Task-Unassigned");
        return button;
    }
    
    public static BaseButton create(Material material) {
        BaseButton button = create();
        button.itemStack = new ItemStack(material);
        return button;
    }
    
    /**
     * Creates a blank button, ideal for empty-areas in a menu.
     * @return Background button
     */
    public static BaseButton background() {
        BaseButton button = new BaseButton();
        button.itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        button = button.name("");
        return button;
    }
    
    /**
     * Makes the Button glow
     * @return button
     */
    public BaseButton glow() {
        this.itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.itemStack.setItemMeta(meta);
        return this;
    }
    
    /**
     * Sets the itemstack of the button
     * @param itemStack
     * @return button
     */
    public BaseButton itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
    
    /**
     * Sets the task
     * @param task
     * @return button
     */
    public BaseButton task(BaseTask task) {
        this.task = task;
        return this;
    }
    
    /**
     * Sets the quantity of the button's stack
     * @param num
     * @return button
     */
    public BaseButton quantity(int num) {
        this.itemStack.setAmount(num);
        return this;
    }
    
    /**
     * Renames the button
     * @param name
     * @return button
     */
    public BaseButton name(String name) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + name);
        this.itemStack.setItemMeta(meta);
        return this;
    }
    
    /**
     * Sets the button's lore
     * @param lore
     * @return button
     */
    public BaseButton lore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
        return this;
    }
    
    /**
     * Sets the button's lore. 
     * <br> Split with '\n'
     * @param lore
     * @return button
     */
    public BaseButton lore(String lore) {
        return lore(Arrays.asList(lore.split("\n")));
    }
    
    /**
     * Runs the button task.
     * @param ClickEvent
     * @return button
     */
    public BaseButton run(InventoryClickEvent e) {
        if (this.task != null) this.task.run(e);
        return this;
    }
    
}
