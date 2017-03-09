package ca.poutineqc.base.datastorage.serializable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.UniversalSerializable;
import ca.poutineqc.base.utils.Utils;

public class SDouble implements UniversalSerializable {

	public static final int MAX_STRING_LENGTH = 16;
	private static final String PRIMAL_KEY = "value";

	private Double value;

	public SDouble(double value) {
		this.value = value;
	}

	public SDouble(String value) {
		this.value = Double.longBitsToDouble(Long.parseUnsignedLong(unpad(value), 16));
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

	@Override
	public String getSqlDataTypeName() {
		return "VARCHAR(" + getMaxToStringLength() + ")";
	}

	@Override
	public String pad(String toPad) {
		return Utils.padLeft(toPad, getMaxToStringLength()).replace(' ', PAD_CHAR);
	}

	@Override
	public String unpad(String toUnpad) {
		return (toUnpad.replace(PAD_CHAR, ' ')).trim();
	}

	@Override
	public boolean isSame(UniversalSerializable o) {
		return toSString().equals(o.toSString());
	}

}
