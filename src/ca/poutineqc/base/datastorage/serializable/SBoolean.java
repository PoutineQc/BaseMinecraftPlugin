package ca.poutineqc.base.datastorage.serializable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.UniversalSerializable;
import ca.poutineqc.base.utils.Utils;

public class SBoolean implements UniversalSerializable {

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
