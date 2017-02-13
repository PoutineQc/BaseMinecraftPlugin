package me.poutineqc.data;

import java.util.List;
import java.util.UUID;

import me.poutineqc.instantiable.SavableParameter;
import me.poutineqc.plugin.PoutinePlugin;

public class FlatFile implements DataStorage {
	
	private PluginYAMLFile file;

	public FlatFile(PoutinePlugin plugin, String fileName, boolean builtIn, String... folders) {
		file = new PluginYAMLFile(fileName, builtIn, folders);
	}

	@Override
	public String getString(UUID identification, SavableParameter parameter) {
		return file.getString(identification.toString() + "." + parameter.getKey());
	}

	@Override
	public void setString(UUID identification, SavableParameter parameter, String value) {
		file.set(identification.toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public int getInt(UUID identification, SavableParameter parameter) {
		return file.getInt(identification.toString() + "." + parameter.getKey());
	}

	@Override
	public void setInt(UUID identification, SavableParameter parameter, int value) {
		file.set(identification.toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public double getDouble(UUID identification, SavableParameter parameter) {
		return file.getDouble(identification.toString() + "." + parameter.getKey());
	}

	@Override
	public void setDouble(UUID identification, SavableParameter parameter, double value) {
		file.set(identification.toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public long getLong(UUID identification, SavableParameter parameter) {
		return file.getLong(identification.toString() + "." + parameter.getKey());
	}

	@Override
	public void setLong(UUID identification, SavableParameter parameter, long value) {
		file.set(identification.toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public boolean getBoolean(UUID identification, SavableParameter parameter) {
		return file.getBoolean(identification.toString() + "." + parameter.getKey());
	}

	@Override
	public void setBoolean(UUID identification, SavableParameter parameter, boolean value) {
		file.set(identification.toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public float getFloat(UUID identification, SavableParameter parameter) {
		return (float) file.getDouble(identification.toString() + "." + parameter.getKey());
	}

	@Override
	public void setFloat(UUID identification, SavableParameter parameter, float value) {
		file.set(identification.toString() + "." + parameter.getKey(), value);
		file.save();
	}

	public List<?> getList(UUID identification, String key) {
		return file.getList(identification.toString() + "." + key);
	}

	public void setList(UUID identification, String key, List<?> value) {
		file.set(identification.toString() + "." + key, value);
		file.save();
	}
}
