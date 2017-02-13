package me.poutineqc.plugin;

public enum Message {

	LANGUAGE_NAME("languageName", "english"), PREFIX("prefix", "[pl]"), NOBODY("keyWordNoOne", "no one yet");

	private String key;
	private String defaultValue;

	private Message(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

}
