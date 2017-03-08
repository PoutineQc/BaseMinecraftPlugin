package ca.poutineqc.base.data.values;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.UniversalSavableValue;

public class SBoolean implements UniversalSavableValue {

	public static final int MAX_STRING_LENGTH = 1;
	private static final String PRIMAL_KEY = "value";

	private Boolean value;

	public SBoolean(boolean value) {
		this.value = value;
	}

	public SBoolean(String value) {
		this.value = value.equals("1");
	}

	public SBoolean(ConfigurationSection cs) {
		this.value = cs.getBoolean(PRIMAL_KEY);
	}

	public SBoolean(JsonObject json) {
		this.value = json.get(PRIMAL_KEY).getAsBoolean();
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public boolean getBoolean() {
		return value;
	}

	@Override
	public String toSString() {
		return value ? "1" : "0";
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(PRIMAL_KEY, value);
		return cs;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(PRIMAL_KEY, value);
		return json;
	}

}
