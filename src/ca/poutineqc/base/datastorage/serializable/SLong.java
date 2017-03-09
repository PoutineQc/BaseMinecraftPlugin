package ca.poutineqc.base.datastorage.serializable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.UniversalSerializable;
import ca.poutineqc.base.utils.Utils;

public class SLong implements UniversalSerializable {

	public static final int MAX_STRING_LENGTH = 16;
	private static final String PRIMAL_KEY = "value";
	
	private Long value;

	public SLong(long value) {
		this.value = value;
	};

	public SLong(String value) {
		this.value = Long.parseUnsignedLong(unpad(value), 16);
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
