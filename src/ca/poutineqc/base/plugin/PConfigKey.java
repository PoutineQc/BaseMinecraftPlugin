package ca.poutineqc.base.plugin;

public enum PConfigKey {
	PREFIX("prefixInFrontOfMessages"), LANGUAGE("language"), PRIMARY_COLOR("primaryColor"), SECONDARY_COLOR("secondaryColor");
	
	private String key;
	
	private PConfigKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
