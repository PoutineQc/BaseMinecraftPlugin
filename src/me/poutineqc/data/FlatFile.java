package me.poutineqc.data;

import java.util.List;

import me.poutineqc.base.Plugin;

public class FlatFile extends DataStorage {
	
	private PluginYAMLFile file;

	public FlatFile(Plugin plugin, String fileName, boolean builtIn, String... folders) {
		super(plugin);
		file = new PluginYAMLFile(fileName, builtIn, folders);
	}

	@Override
	public String getString(String capsule, String key) {
		return file.getString(capsule + "." + key);
	}

	@Override
	public void setString(String capsule, String key, String value) {
		file.set(capsule + "." + key, value);
	}

	@Override
	public int getInt(String capsule, String key) {
		return file.getInt(capsule + "." + key);
	}

	@Override
	public void setInt(String capsule, String key, int value) {
		file.set(capsule + "." + key, value);
	}

	@Override
	public double getDouble(String capsule, String key) {
		return file.getDouble(capsule + "." + key);
	}

	@Override
	public void setDouble(String capsule, String key, double value) {
		file.set(capsule + "." + key, value);
	}

	@Override
	public long getLong(String capsule, String key) {
		return file.getLong(capsule + "." + key);
	}

	@Override
	public void setLong(String capsule, String key, long value) {
		file.set(capsule + "." + key, value);
	}

	@Override
	public boolean getBoolean(String capsule, String key) {
		return file.getBoolean(capsule + "." + key);
	}

	@Override
	public void setBoolean(String capsule, String key, boolean value) {
		file.set(capsule + "." + key, value);
	}

	@Override
	public float getFloat(String capsule, String key) {
		return (float) file.getDouble(capsule + "." + key);
	}

	@Override
	public void setFloat(String capsule, String key, float value) {
		file.set(capsule + "." + key, value);
	}

	public List<?> getList(String capsule, String key) {
		return file.getList(capsule + "." + key);
	}

	public void setList(String capsule, String key, List<?> value) {
		file.set(capsule + "." + key, value);
	}
}
