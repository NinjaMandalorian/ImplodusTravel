package me.ninjamandalorian.ImplodusTravel.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.ninjamandalorian.ImplodusTravel.object.Station;

/** Event that runs before transporting a player
 * @author NinjaMandalorian
 */
public class PreTransportEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player; // Player of transport
    private Station source; // Station teleporting from
    private Station dest; // Station teleporting to

    /** Constructor
     * @param player - Player
     * @param source - Source station
     * @param dest - Destination station
     */
    public PreTransportEvent(Player player, Station source, Station dest){
        this.player = player;
        this.source = source;
        this.dest = dest;
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

    /** Get the player doing the teleport
     * @return Player teleporting
     */
    public Player getPlayer() {
        return player;
    }

    /** Get the source station of the teleport
     * @return Source station
     */
    public Station getSource() {
        return source;
    }
    
    /** Get the destination station of the teleport
     * @return Destination station
     */
    public Station getDest() {
        return dest;
    }
}