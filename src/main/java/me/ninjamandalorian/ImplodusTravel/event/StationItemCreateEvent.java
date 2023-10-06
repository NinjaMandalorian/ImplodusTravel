package me.ninjamandalorian.ImplodusTravel.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/** Event that runs right before creating a station item
 * @author NinjaMandalorian
 */
public class StationItemCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player; // Player creating item
    private Block block; // Block of station
    private ItemStack itemStack; // Item turning into station item

    /** Constructor
     * @param player - Player making item
     * @param block - Block for creation
     * @param itemStack - Item for creation
     */
    public StationItemCreateEvent(Player player, Block block, ItemStack itemStack) {
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
    
    /** Get player of creation
     * @return Player creating item
     */
    public Player getPlayer() {
        return player;
    }
    
    /** Get block of creation
     * @return Block of creation
     */
    public Block getBlock() {
        return block;
    }

    /** Get item being modified
     * @return ItemStack being modified
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
}
