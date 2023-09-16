package me.ninjamandalorian.ImplodusTravel.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class StationCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private Block block;
    private ItemStack itemStack;

    public StationCreateEvent(Player player, Block block, ItemStack itemStack) {
        this.player = player;
        this.block = block;
        this.itemStack = itemStack;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
       return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Block getBlock() {
        return block;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
