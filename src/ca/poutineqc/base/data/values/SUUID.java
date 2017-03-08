package ca.poutineqc.base.data.values;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.StringSavableValue;
import ca.poutineqc.base.data.UniversalSavableValue;

public class SUUID implements UniversalSavableValue {

	public static final int MAX_STRING_LENGTH = 36;
	private static final String PRIMAL_KEY = "value";

	UUID value;
	
	public SUUID(UUID value) {
		this.value = value;
	}
	
	public SUUID(String value) {
		this.value = UUID.fromString(StringSavableValue.unpad(value));
	}
	
	public SUUID(ConfigurationSection cs) {
		this.value = UUID.fromString(cs.getString(PRIMAL_KEY));
	}

	public SUUID(JsonObject json) {
		this.value = UUID.fromString(json.get(PRIMAL_KEY).getAsString());
	}

	public UUID getUUID() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(this.value.toString());
	}

	@Override
	public String toString() {
		return value.toString();
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
		json.addProperty(PRIMAL_KEY, value.toString());
		return json;
	}

}
