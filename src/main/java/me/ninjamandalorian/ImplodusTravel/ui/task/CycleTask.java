package me.ninjamandalorian.ImplodusTravel.ui.task;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ninjamandalorian.ImplodusTravel.Logger;
import net.md_5.bungee.api.ChatColor;

/*
 *  Cycles between two or more options.
 */
public class CycleTask  implements BaseTask{

    private int counter = 0;
    private int ceiling = 0; // Maximum value (Math.min(materials, values))
    private List<Material> cycleMaterials;
    private List<Object> cycleValues;
    private int stackNum;
    private Runnable onRun;
    private String title;

    public CycleTask(int stackNum, List<Object> values, List<Material> materials) {
        this.stackNum = stackNum;
        this.cycleValues = values;
        this.cycleMaterials = materials;
        ceiling = Math.min(cycleMaterials.size(), cycleValues.size());
    }

    public void runTask(Runnable runnable) {
        onRun = runnable;
    }

    @Override
    public void run(InventoryClickEvent event) {
        Logger.log("RUN CYCLE TASK" + (counter+1));
        counter++; 
        counter = counter % ceiling;

        ItemStack stack = event.getInventory().getContents()[stackNum];
        stack.setType(cycleMaterials.get(counter));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title + " " + cycleValues.get(counter)));
        stack.setItemMeta(meta);
        event.getInventory().setItem(stackNum, stack);

        if (onRun != null) onRun.run();
    }
    
}
