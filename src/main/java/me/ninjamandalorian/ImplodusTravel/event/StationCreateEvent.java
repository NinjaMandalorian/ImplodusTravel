package me.ninjamandalorian.ImplodusTravel.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/** Event that runs right before creating a station
 * @author NinjaMandalorian
 */
public class StationCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player; // Player creating station
    private Block block; // Block being created
    private ItemStack itemStack; // ItemStack being used to create

    /** Constructor
     * @param player - Player creating station
     * @param block - Block being created
     * @param itemStack - ItemStack being used to create
     */
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
    
    /** Get player creating station
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }
    
    /** Get block of station
     * @return Block for new station
     */
    public Block getBlock() {
        return block;
    }

    /** Get ItemStack creating station
     * @return ItemStack for new station
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
}
