package me.poutineqc.base;

import java.util.List;

import me.poutineqc.data.PluginYAMLFile;

public class PluginConfig {

	public static final String FILE_NAME = "config";

	private PluginYAMLFile yamlFile;

	public PluginConfig(Plugin plugin) {
		yamlFile = new PluginYAMLFile(FILE_NAME, true);
	}

	public boolean isString(ConfigOptions option) {
		return option.defaultValue instanceof String;
	}

	public String getString(ConfigOptions option) throws ObjectTypeException {
		if (option.defaultValue instanceof String)
			throw new ObjectTypeException(option.key + " is not a String");
		return yamlFile.getString(option.key, (String) option.defaultValue);
	}

	public String getString(ConfigOptions option, String defaultValue) {
		return yamlFile.getString(option.key, defaultValue);
	}

	public boolean isInt(ConfigOptions option) {
		return option.defaultValue instanceof Integer;
	}

	public int getInt(ConfigOptions option) throws ObjectTypeException {
		if (option.defaultValue instanceof Integer)
			throw new ObjectTypeException(option.key + " is not a Int");
		return yamlFile.getInt(option.key, (Integer) option.defaultValue);
	}

	public boolean isLong(ConfigOptions option) {
		return option.defaultValue instanceof Long;
	}

	public long getLong(ConfigOptions option) throws ObjectTypeException {
		if (option.defaultValue instanceof Long)
			throw new ObjectTypeException(option.key + " is not a Long");
		return yamlFile.getLong(option.key, (Long) option.defaultValue);
	}

	public boolean isDouble(ConfigOptions option) {
		return option.defaultValue instanceof Double;
	}

	public double getDouble(ConfigOptions option) throws ObjectTypeException {
		if (option.defaultValue instanceof Double)
			throw new ObjectTypeException(option.key + " is not a Double");
		return yamlFile.getDouble(option.key, (Double) option.defaultValue);
	}

	public boolean isBoolean(ConfigOptions option) {
		return option.defaultValue instanceof Boolean;
	}

	public boolean getBoolean(ConfigOptions option) throws ObjectTypeException {
		if (option.defaultValue instanceof Boolean)
			throw new ObjectTypeException(option.key + " is not a Boolean");
		return yamlFile.getBoolean(option.key, (Boolean) option.defaultValue);
	}

	public List<?> getList(ConfigOptions option) {
		return yamlFile.getList(option.key);
	}

	public enum ConfigOptions {

		LANGUAGE("language", "en"),
		PREFIX("prefixInFrontOfMessages", true),
		
		MYSQL("mysql", false),
		HOST("host", "127.0.0.1"),
		PORT("port", 3306),
		DATABASE("database", "minecraft"),
		USER("user", "root"),
		PASSWORD("password", "");

		private String key;
		private Object defaultValue;

		private ConfigOptions(String key, Object defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
	}
}
