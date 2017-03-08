package ca.poutineqc.base.data.values;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.StringSavableValue;
import ca.poutineqc.base.data.UniversalSavableValue;

public class SFloat implements UniversalSavableValue {

	public static final int MAX_STRING_LENGTH = 8;
	private static final String PRIMAL_KEY = "value";

	private Float value;

	public SFloat(float value) {
		this.value = value;
	}

	public SFloat(String value) {
		this.value = Float.intBitsToFloat(Integer.parseUnsignedInt(StringSavableValue.unpad(value), 16));
	}

	public SFloat(ConfigurationSection cs) {
		this.value = (float) cs.getDouble(PRIMAL_KEY);
	}

	public SFloat(JsonObject json) {
		this.value = json.get(PRIMAL_KEY).getAsFloat();
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public float getFloat() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(Integer.toHexString(Float.floatToIntBits(value)));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	public void setFloat(float value) {
		this.value = value;	
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
