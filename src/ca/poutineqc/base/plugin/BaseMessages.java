package ca.poutineqc.base.plugin;

public enum BaseMessages implements Messages {

	LANGUAGE_NAME("languageName", "english"),

	PREFIX("prefix", "[pl]"),

	NOBODY("keyWordNoOne", "no one yet");

	private String key;
	private String defaultValue;

	private BaseMessages(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDefaultMessage() {
		return defaultValue;
	}

}
