package ca.poutineqc.base.instantiable;

import java.security.InvalidParameterException;

public interface Playable<T extends PPlayer> {
	
	void start();
	void nextTurn();
	void end();
	void reset();

	void add(T player) throws InvalidParameterException;
	boolean isInGame(T player);
	void eliminate(T player);
	boolean remove(T player);
}
