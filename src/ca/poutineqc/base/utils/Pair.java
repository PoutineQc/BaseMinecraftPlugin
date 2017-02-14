package ca.poutineqc.base.utils;

public class Pair<S, T> {
	
	S key;
	T value;
	
	public Pair(S key, T value) {
		this.key = key;
		this.value = value;
	}
	
	public S getKey() {
		return key;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
}
