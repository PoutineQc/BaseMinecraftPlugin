package ca.poutineqc.base.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.Playable;

public class PlayerLeaveGameEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private PPlayer player;
	private Playable game;
	
	public PlayerLeaveGameEvent(PPlayer player, Playable game) {
		this.player = player;
		this.game = game;
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
