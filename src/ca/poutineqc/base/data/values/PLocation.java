package ca.poutineqc.base.data.values;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PLocation extends Location implements SValue {

	public static final int MAX_STRING_LENGTH = BinaryValue.BINARY_64.getValue() + (3 * SDouble.MAX_STRING_LENGTH)
			+ (2 * SFloat.MAX_STRING_LENGTH);

	private SString worldUUID;
	private SDouble x;
	private SDouble y;
	private SDouble z;
	private SFloat pitch;
	private SFloat yaw;

	public PLocation(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public PLocation(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public PLocation(Location location) {
		this(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getPitch(),
				location.getYaw());
	}

	public PLocation(String value) throws IllegalArgumentException {
		super(null, 0, 0, 0);

		if (value.length() != getMaxToStringLength())
			throw new IllegalArgumentException("The String passed does not represent a Location.");

		int i = 0;
		int j = BinaryValue.BINARY_64.getValue();
		this.worldUUID = new SString(value.substring(i, j), BinaryValue.BINARY_64);
		this.setWorld(Bukkit.getWorld(UUID.fromString(worldUUID.toString())));

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

	@Override
	public String toSString() {
		StringBuilder sb = new StringBuilder();

		try {
			worldUUID.setString(this.getWorld().getUID().toString());
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

}
