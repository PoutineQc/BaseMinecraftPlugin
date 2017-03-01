package ca.poutineqc.base.data.values;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

public class SInteger implements UniversalSavableValue {

	public static final int MAX_STRING_LENGTH = 8;
	private static final String PRIMAL_KEY = "value";
	private Integer value;

	public SInteger(int value) {
		this.value = value;
	}

	public SInteger(String value) {
		this.value = Integer.parseUnsignedInt(StringSavableValue.unpad(value), 16);
	}

	public SInteger(ConfigurationSection cs) {
		this.value = cs.getInt(PRIMAL_KEY);
	}

	public SInteger(JsonObject json) {
		this.value = json.get(PRIMAL_KEY).getAsInt();
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public int getInt() {
		return value;
	}

	public void setInt(int value) {
		this.value = value;
	}
	
	@Override
	public String toSString() {
		return pad(Integer.toHexString(value));
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
