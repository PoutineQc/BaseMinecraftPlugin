package ca.poutineqc.base;

public enum PConfigKey {
	PREFIX("prefixInFrontOfMessages"),
	
	LANGUAGE("language"),
	
	STORAGE("dataStorage"), DB_HOST("host"), DB_PORT("port"), DB_USER("user"), DB_PASS("password"), DB_DB("database"), DB_TABLE_PREFIX("tablePrefix");
	
	private String key;
	
	private PConfigKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
