package me.ninjamandalorian.ImplodusTravel.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PagedMenu extends BaseMenu {
    
    // Paged menu fields
    private ArrayList<ArrayList<BaseButton>> pages = null; // If null, menu is not paged
    private int currentPage = 0;
    


    private PagedMenu(PagedBuilder builder) {
        this.inventory = Bukkit.createInventory(this, builder.menuSize, builder.menuTitle);
        
        this.openMessage = builder.openMsg;

        // Creates all items in inventory
        for (Entry<Integer, BaseButton> buttonEntry : builder.menuButtons.entrySet()) {
            if (buttonEntry.getKey() >= builder.menuSize) continue;
            inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItemStack());
        }
        
        this.menuButtons = builder.menuButtons;
        this.openMessage = builder.openMsg;
        
        this.pages = builder.pageButtons;
        this.loadPage(0);
    }

    public void changePage(Player player, int direction) {
        if (pages == null) return;
        currentPage += direction;
        currentPage = Math.max(currentPage, 0);
        currentPage = Math.min(currentPage, pages.size() -1);
        
        loadPage(currentPage);
    }
    
    private void loadPage(int pageNum) {
        // TODO Logger.debug("LOADING PAGE " + pageNum);
        List<Integer> slotList = Arrays.asList(
                10,11,12,13,14,15,16,
                19,20,21,22,23,24,25,
                28,29,30,31,32,33,34,
                37,38,39,40,41,42,43);
        
        ArrayList<BaseButton> pageButtons = pages.get(pageNum);
        for (int i = 0; i < 28; i++) {
            if (i < pageButtons.size()) {
                inventory.setItem(slotList.get(i), pageButtons.get(i).getItemStack());
                menuButtons.put(slotList.get(i), pageButtons.get(i));
            } else {
                inventory.setItem(slotList.get(i), null);
                menuButtons.remove(slotList.get(i));
            }
        }
    }
    

    public class PagedBuilder {
        private String menuTitle = "Empty-title";
        private int menuSize = 54;
        private HashMap<Integer,BaseButton> menuButtons = new HashMap<Integer,BaseButton>();
        private ArrayList<ArrayList<BaseButton>> pageButtons = new ArrayList<ArrayList<BaseButton>>();
        private String openMsg = null;
        // private int bounds[] = {0,54};

    }

}
