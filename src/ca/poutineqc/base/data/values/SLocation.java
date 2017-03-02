package ca.poutineqc.base.data.values;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.JsonObject;

public class SLocation extends Location implements UniversalSavableValue {

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
		super(world, x, y, z);
	}

	public SLocation(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public SLocation(Location location) {
		this(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getPitch(),
				location.getYaw());
	}

	public SLocation(String value) throws IllegalArgumentException {
		super(null, 0, 0, 0);

		if (value.length() != getMaxToStringLength())
			throw new IllegalArgumentException("The String passed does not represent a Location.");

		int i = 0;
		int j = SUUID.MAX_STRING_LENGTH;
		this.worldUUID = new SUUID(value.substring(i, j));
		this.setWorld(Bukkit.getWorld(UUID.fromString(worldUUID.getUUID().toString())));

		i = j;
		j += SDouble.MAX_STRING_LENGTH;
		this.x = new SDouble(value.substring(i, j));
		this.setX(x.getDouble());

		i = j;
		j += SDouble.MAX_STRING_LENGTH;
		this.y = new SDouble(value.substring(i, j));
		this.setY(y.getDouble());

		i = j;
		j += SDouble.MAX_STRING_LENGTH;
		this.z = new SDouble(value.substring(i, j));
		this.setZ(z.getDouble());

		i = j;
		j += SFloat.MAX_STRING_LENGTH;
		this.pitch = new SFloat(value.substring(i, j));
		this.setPitch(pitch.getFloat());

		this.yaw = new SFloat(value.substring(j));
		this.setYaw(yaw.getFloat());
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

		try {
			if (this.getWorld() != null)
				worldUUID = new SUUID(this.getWorld().getUID());
			
			x.setDouble(this.getX());
			y.setDouble(this.getY());
			z.setDouble(this.getZ());
			pitch.setFloat(this.getPitch());
			yaw.setFloat(this.getYaw());
		} catch (IllegalArgumentException e) {
			Bukkit.getLogger().severe("There was a problem creating a PString with the world UUID");
		}

		sb.append(worldUUID.toSString());
		sb.append(x.toSString());
		sb.append(y.toSString());
		sb.append(z.toSString());
		sb.append(pitch.toSString());
		sb.append(yaw.toSString());

		return sb.toString();
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	@Override
	public ConfigurationSection toConfigurationSection() {
		ConfigurationSection cs = new YamlConfiguration();
		cs.set(WORLD_KEY, this.getWorld().getUID().toString());
		cs.set(X_KEY, this.getX());
		cs.set(Y_KEY, this.getY());
		cs.set(Z_KEY, this.getZ());
		cs.set(PITCH_KEY, this.getPitch());
		cs.set(YAW_KEY, this.getYaw());
		return cs;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(WORLD_KEY, this.getWorld().getUID().toString());
		json.addProperty(X_KEY, this.getX());
		json.addProperty(Y_KEY, this.getY());
		json.addProperty(Z_KEY, this.getZ());
		json.addProperty(PITCH_KEY, this.getPitch());
		json.addProperty(YAW_KEY, this.getYaw());
		return json;
	}

}
