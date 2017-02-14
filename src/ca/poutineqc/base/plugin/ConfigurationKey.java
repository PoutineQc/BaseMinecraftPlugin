package ca.poutineqc.base.plugin;

public enum ConfigurationKey {
	PREFIX("prefixInFrontOfMessages"), LANGUAGE("language");
	
	private String key;
	
	private ConfigurationKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
