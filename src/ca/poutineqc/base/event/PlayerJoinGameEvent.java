package ca.poutineqc.base.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.Playable;

public class PlayerJoinGameEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	
	private PPlayer player;
	private Playable game;

	public PlayerJoinGameEvent(PPlayer player, Playable game) {
		this.player = player;
		this.game = game;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}
	
	public PPlayer getPlayer() {
		return player;
	}
	
	public Playable getGame() {
		return game;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
