package me.poutineqc.data;

import java.util.UUID;

import me.poutineqc.instantiable.SavableParameter;

public interface DataStorage {
	
	public String getString(UUID identification, SavableParameter parameter);
	public void setString(UUID identification, SavableParameter parameter, String value);

	public int getInt(UUID identification, SavableParameter parameter);
	public void setInt(UUID identification, SavableParameter parameter, int value);

	public double getDouble(UUID identification, SavableParameter parameter);
	public void setDouble(UUID identification, SavableParameter parameter, double value);

	public long getLong(UUID identification, SavableParameter parameter);
	public void setLong(UUID identification, SavableParameter parameter, long value);

	public boolean getBoolean(UUID identification, SavableParameter parameter);
	public void setBoolean(UUID identification, SavableParameter parameter, boolean value);

	public float getFloat(UUID identification, SavableParameter parameter);
	public void setFloat(UUID identification, SavableParameter parameter, float value);
	
	
}
