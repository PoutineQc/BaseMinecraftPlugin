package ca.poutineqc.base.lang;

public interface Message {
	public String getKey();
	public String getDefaultMessage();
	public Message getPrefixMessage();
}
