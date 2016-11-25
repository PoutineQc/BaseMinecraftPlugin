package me.poutineqc.data;

import me.poutineqc.base.Plugin;

public abstract class DataStorage {
	
	protected Plugin plugin;
	
	public DataStorage(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public abstract String getString(String capsule, String key);
	public abstract void setString(String capsule, String key, String value);

	public abstract int getInt(String capsule, String key);
	public abstract void setInt(String capsule, String key, int value);

	public abstract double getDouble(String capsule, String key);
	public abstract void setDouble(String capsule, String key, double value);

	public abstract long getLong(String capsule, String key);
	public abstract void setLong(String capsule, String key, long value);

	public abstract boolean getBoolean(String capsule, String key);
	public abstract void setBoolean(String capsule, String key, boolean value);

	public abstract float getFloat(String capsule, String key);
	public abstract void setFloat(String capsule, String key, float value);
	
	
}
