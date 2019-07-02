package ca.poutineqc.base.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ca.poutineqc.base.instantiable.PPlayer;

public class PlayerUnloadEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private PPlayer player;

	public PlayerUnloadEvent(PPlayer player) {
		this.player = player;
	}
	
	public PPlayer getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
