package me.ninjamandalorian.ImplodusTravel.ui.task;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
 *  Cycles between two or more options.
 */
public class CycleTask  implements BaseTask{

    private int counter = 0;
    private ArrayList<Material> cycleMaterials;
    private ArrayList<String> cycleValues;

    @Override
    public void run(InventoryClickEvent event) {
        // TODO Auto-generated method stub      
    }
    
}
