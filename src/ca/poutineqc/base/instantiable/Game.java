package ca.poutineqc.base.instantiable;

import java.security.InvalidParameterException;
import java.util.List;

public abstract class Game<T extends PPlayer, S extends PArena> implements Playable<T> {
	
	S arena;
	List<T> players;

	@Override
	public void add(T player) throws InvalidParameterException {
		if (players.contains(player))
			throw new InvalidParameterException("This player is already in the game");
		
		players.add(player);
	}

	@Override
	public boolean remove(T player) {
		return players.remove(player);
	}

	@Override
	public boolean isInGame(T player) {
		return players.contains(player);
	}
	
	public S getArena () {
		return arena;
	}
	
}
