package me.ninjamandalorian.ImplodusTravel.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.ninjamandalorian.ImplodusTravel.object.Station;

public class PostTransportEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private Station source;
    private Station dest;

    public PostTransportEvent(Player player, Station source, Station dest){
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

    public Player getPlayer() {
        return player;
    }

    public Station getSource() {
        return source;
    }
    
    public Station getDest() {
        return dest;
    }
}