package ca.sebastienchagnon.minecraft.prolib.datastorage.serializable;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.sebastienchagnon.minecraft.prolib.datastorage.UniversalSerializable;
import ca.sebastienchagnon.minecraft.prolib.utils.Utils;

public class SUUID implements UniversalSerializable {

	public static final int MAX_STRING_LENGTH = 36;
	private static final String PRIMAL_KEY = "value";

	UUID value;

	public SUUID(UUID value) {
		this.value = value;
	}

	public SUUID(String value) {
		this.value = UUID.fromString(unpad(value));
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
