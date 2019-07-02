package ca.sebastienchagnon.minecraft.prolib.datastorage.serializable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.sebastienchagnon.minecraft.prolib.datastorage.UniversalSerializable;
import ca.sebastienchagnon.minecraft.prolib.utils.PowerOfTwo;
import ca.sebastienchagnon.minecraft.prolib.utils.Utils;

public class SString implements UniversalSerializable {

	private static final String PRIMAL_KEY = "value";
	private static final String LENGTH_KEY = "length";
	private int exponent;
	
	private String value;

	public SString(String value, PowerOfTwo pt) {
		if (value.length() > pt.getPower())
			throw new OutOfMemoryError("The String does not fit in the required space");

		this.exponent = pt.getExponent();
		this.value = unpad(value);
	}

	public SString(ConfigurationSection cs) throws IllegalArgumentException {
		exponent = cs.getInt(LENGTH_KEY);
		this.value = cs.getString(PRIMAL_KEY);
		
		if (value.length() > PowerOfTwo.getPower(exponent));
			throw new OutOfMemoryError("The String does not fit in the required space");

	}

	public SString(JsonObject json) {
		this.exponent = json.get(LENGTH_KEY).getAsInt();
		this.value = json.get(PRIMAL_KEY).getAsString();
	}

	public void setString(String value) throws IllegalArgumentException {
		if (value.length() > PowerOfTwo.getPower(exponent))
			throw new OutOfMemoryError("The String does not fit in the required space");

		this.value = unpad(value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(value);
	}

	@Override
	public int getMaxToStringLength() {
		return PowerOfTwo.getPower(exponent);
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(LENGTH_KEY, exponent);
		cs.set(PRIMAL_KEY, value);
		return cs;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(PRIMAL_KEY, value);
		json.addProperty(LENGTH_KEY, exponent);
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
