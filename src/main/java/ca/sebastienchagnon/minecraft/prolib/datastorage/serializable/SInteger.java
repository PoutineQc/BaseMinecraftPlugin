package ca.sebastienchagnon.minecraft.prolib.datastorage.serializable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.sebastienchagnon.minecraft.prolib.datastorage.UniversalSerializable;
import ca.sebastienchagnon.minecraft.prolib.utils.Utils;

public class SInteger implements UniversalSerializable {

	public static final int MAX_STRING_LENGTH = 8;
	private static final String PRIMAL_KEY = "value";
	private Integer value;

	public SInteger(int value) {
		this.value = value;
	}

	public SInteger(String value) {
		this.value = Integer.parseUnsignedInt(unpad(value), 16);
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
