package ca.poutineqc.base.data.values;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

public class SDouble implements UniversalSavableValue {

	public static final int MAX_STRING_LENGTH = 16;
	private static final String PRIMAL_KEY = "value";

	private Double value;

	public SDouble(double value) {
		this.value = value;
	}

	public SDouble(String value) {
		this.value = Double.longBitsToDouble(Long.parseUnsignedLong(StringSavableValue.unpad(value), 16));
	}

	public SDouble(ConfigurationSection cs) {
		this.value = cs.getDouble(PRIMAL_KEY);
	}

	public SDouble(JsonObject json) {
		this.value = json.get(PRIMAL_KEY).getAsDouble();
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public double getDouble() {
		return value;
	}

	public void setDouble(double value) {
		this.value = value;
	}

	@Override
	public String toSString() {
		return pad(Long.toHexString(Double.doubleToLongBits(value)));
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
