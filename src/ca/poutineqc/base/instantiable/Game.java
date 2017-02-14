package ca.poutineqc.base.instantiable;

import java.security.InvalidParameterException;
import java.util.List;

public abstract class Game<T extends BasePlayer, S extends BaseArena> implements Playable<T> {
	
	S arena;
	List<T> players;

	public void add(T player) throws InvalidParameterException {
		if (players.contains(player))
			throw new InvalidParameterException("This player is already in the game");
		
		players.add(player);
	}
	
	public boolean remove(T player) {
		return players.remove(player);
	}
	
	public boolean isInGame(T player) {
		return players.contains(player);
	}
	
	public S getArena () {
		return arena;
	}
	
}
