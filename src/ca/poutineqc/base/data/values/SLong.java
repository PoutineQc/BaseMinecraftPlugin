package ca.poutineqc.base.data.values;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.StringSavableValue;
import ca.poutineqc.base.data.UniversalSavableValue;

public class SLong implements UniversalSavableValue {

	public static final int MAX_STRING_LENGTH = 16;
	private static final String PRIMAL_KEY = "value";
	
	private Long value;

	public SLong(long value) {
		this.value = value;
	};

	public SLong(String value) {
		this.value = Long.parseUnsignedLong(StringSavableValue.unpad(value), 16);
	}

	public SLong(ConfigurationSection cs) {
		this.value = cs.getLong(PRIMAL_KEY);
	}

	public SLong(JsonObject json) {
		this.value = json.get(PRIMAL_KEY).getAsLong();
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public long getLong() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(Long.toHexString(value));
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
