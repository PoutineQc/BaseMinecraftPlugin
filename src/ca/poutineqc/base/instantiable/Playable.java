package ca.poutineqc.base.instantiable;

public interface Playable<T extends BasePlayer> {
	void start();
	void end();
	void reset();
	
	void eliminate(T player);
	void nextTurn();
}
