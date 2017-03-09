package ca.poutineqc.base.datastorage.serializable;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.UniversalSerializable;
import ca.poutineqc.base.utils.Utils;

public class SLocation implements UniversalSerializable {

	public static final int MAX_STRING_LENGTH = SUUID.MAX_STRING_LENGTH + (3 * SDouble.MAX_STRING_LENGTH)
			+ (2 * SFloat.MAX_STRING_LENGTH);
	private static final String WORLD_KEY = "world";
	private static final String X_KEY = "x";
	private static final String Y_KEY = "y";
	private static final String Z_KEY = "z";
	private static final String PITCH_KEY = "pitch";
	private static final String YAW_KEY = "yaw";

	private SUUID worldUUID;
	private SDouble x;
	private SDouble y;
	private SDouble z;
	private SFloat pitch;
	private SFloat yaw;

	public SLocation(World world, double x, double y, double z) {
		this(world, x, y, z, 0, 0);
	}

	public SLocation(World world, double x, double y, double z, float pitch, float yaw) {
		this.worldUUID = new SUUID(world == null ? UUID.randomUUID() : world.getUID());
		this.x = new SDouble(x);
		this.y = new SDouble(y);
		this.z = new SDouble(z);
		this.pitch = new SFloat(pitch);
		this.yaw = new SFloat(yaw);
	}

	public SLocation(Location location) {
		this(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getPitch(),
				location.getYaw());
	}

	public SLocation(String value) throws IllegalArgumentException {

		if (value.length() != getMaxToStringLength())
			throw new IllegalArgumentException("The String passed does not represent a Location.");

		int i = 0;
		int j = SUUID.MAX_STRING_LENGTH;
		this.worldUUID = new SUUID(value.substring(i, j));

		i = j;
		j += SDouble.MAX_STRING_LENGTH;
		this.x = new SDouble(value.substring(i, j));

		i = j;
		j += SDouble.MAX_STRING_LENGTH;
		this.y = new SDouble(value.substring(i, j));

		i = j;
		j += SDouble.MAX_STRING_LENGTH;
		this.z = new SDouble(value.substring(i, j));

		i = j;
		j += SFloat.MAX_STRING_LENGTH;
		this.pitch = new SFloat(value.substring(i, j));

		this.yaw = new SFloat(value.substring(j));
	}

	public SLocation(ConfigurationSection cs) {
		this(Bukkit.getWorld(UUID.fromString(cs.getString(WORLD_KEY))), cs.getDouble(X_KEY), cs.getDouble(Y_KEY),
				cs.getDouble(Z_KEY), (float) cs.getDouble(PITCH_KEY), (float) cs.getDouble(YAW_KEY));
	}

	public SLocation(JsonObject json) {
		this(Bukkit.getWorld(UUID.fromString(json.get(WORLD_KEY).getAsString())), json.get(X_KEY).getAsDouble(),
				json.get(Y_KEY).getAsDouble(), json.get(Z_KEY).getAsDouble(), json.get(PITCH_KEY).getAsFloat(),
				json.get(YAW_KEY).getAsFloat());
	}

	@Override
	public String toSString() {
		StringBuilder sb = new StringBuilder();

		sb.append(worldUUID.toSString());
		sb.append(x.toSString());
		sb.append(y.toSString());
		sb.append(z.toSString());
		sb.append(pitch.toSString());
		sb.append(yaw.toSString());

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Bukkit.getWorld(worldUUID.getUUID()) + ", ");
		sb.append(x.getDouble() + ", ");
		sb.append(y.getDouble() + ", ");
		sb.append(z.getDouble() + ", ");
		sb.append(pitch.getFloat() + ", ");
		sb.append(yaw.getFloat());
		return sb.toString();
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(WORLD_KEY, worldUUID.toString());
		cs.set(X_KEY, x.toString());
		cs.set(Y_KEY, y.toString());
		cs.set(Z_KEY, z.toString());
		cs.set(PITCH_KEY, pitch.toString());
		cs.set(YAW_KEY, yaw.toString());
		return cs;
	}

	@Override
	public JsonObject toJsonObject() {

		JsonObject json = new JsonObject();
		json.addProperty(WORLD_KEY, worldUUID.toString());
		json.addProperty(X_KEY, x.getDouble());
		json.addProperty(Y_KEY, y.getDouble());
		json.addProperty(Z_KEY, z.getDouble());
		json.addProperty(PITCH_KEY, pitch.getFloat());
		json.addProperty(YAW_KEY, yaw.getFloat());
		return json;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public Location getLocation() {
		World world = Bukkit.getWorld(worldUUID.getUUID());

		return world == null ? null
				: new Location(world, x.getDouble(), y.getDouble(), z.getDouble(), yaw.getFloat(), pitch.getFloat());
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
