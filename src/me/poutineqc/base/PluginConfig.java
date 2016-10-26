package me.poutineqc.base;

import java.util.List;

import me.poutineqc.base.utils.PluginYAMLFile;

public class PluginConfig {

	public static final String FILE_NAME = "config";

	private PluginYAMLFile yamlFile;

	public PluginConfig(Plugin plugin) {
		yamlFile = new PluginYAMLFile(FILE_NAME, true);
	}
	
	public boolean isInt(ConfigOptions option) {
		return option.defaultValue instanceof Integer;
	}
	
	public int getInt(ConfigOptions option) {
		if (option.defaultValue instanceof Integer)
			throw new NumberFormatException("Option is not a Int");
		return yamlFile.getInt(option.key, (Integer) option.defaultValue);
	}
	
	public int getInt(ConfigOptions option, int defaultValue) {
		return yamlFile.getInt(option.key, defaultValue);
	}
	
	public boolean isLong(ConfigOptions option) {
		return option.defaultValue instanceof Long;
	}
	
	public long getLong(ConfigOptions option) {
		if (option.defaultValue instanceof Long)
			throw new NumberFormatException("Option is not a Long");
		return yamlFile.getLong(option.key, (Long) option.defaultValue);
	}
	
	public long getLong(ConfigOptions option, long defaultValue) {
		return yamlFile.getLong(option.key, defaultValue);
	}
	
	public boolean isDouble(ConfigOptions option) {
		return option.defaultValue instanceof Double;
	}
	
	public double getDouble(ConfigOptions option) {
		if (option.defaultValue instanceof Double)
			throw new NumberFormatException("Option is not a Double");
		return yamlFile.getDouble(option.key, (Double) option.defaultValue);
	}
	
	public double getDouble(ConfigOptions option, double defaultValue) {
		return yamlFile.getDouble(option.key, defaultValue);
	}
	
	public boolean isBoolean(ConfigOptions option) {
		return option.defaultValue instanceof Boolean;
	}
	
	public boolean getBoolean(ConfigOptions option) {
		if (option.defaultValue instanceof Boolean)
			throw new NumberFormatException("Option is not a Boolean");
		return yamlFile.getBoolean(option.key, (Boolean) option.defaultValue);
	}
	
	public boolean getBoolean(ConfigOptions option, boolean defaultValue) {
		return yamlFile.getBoolean(option.key, defaultValue);
	}
	
	public List<?> getList(ConfigOptions option) {
		return yamlFile.getList(option.key);
	}
	
	public List<?> getList(ConfigOptions option, List<?> defaultValue) {
		return yamlFile.getList(option.key, defaultValue);
	}

	private enum ConfigOptions {

		LANGUAGE("language", "en"), PREFIX("prefixInFrontOfMessages", true);

		private String key;
		private Object defaultValue;

		private ConfigOptions(String key, Object defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
	}
}
