package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.Database;
import ca.poutineqc.base.data.FlatFile;
import ca.poutineqc.base.data.MySQL;
import ca.poutineqc.base.plugin.PoutinePlugin;
import ca.poutineqc.base.utils.ColorManager;
import ca.poutineqc.base.utils.Pair;

public abstract class BaseArena implements Savable {

	/*******************************************************
	 * * Static Fields * *
	 *******************************************************/

	private static final String TABLE_NAME = "ARENA";

	protected static DataStorage data;

	/*******************************************************
	 * * Static Methods * *
	 *******************************************************/

	public static void checkDataStorage(PoutinePlugin plugin) {
		if (data == null)
			data = openDataStorage(plugin);
	}
	
	public static List<UUID> getAllIdentifications(PoutinePlugin plugin) {
		checkDataStorage(plugin);
		return data.getAllIdentifications(Data.UUID);
	}

	public static List<SavableParameter> getParameters() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();
		for (Data parameter : Data.values())
			returnValue.add(parameter);

		return returnValue;
	}

	public static DataStorage openDataStorage(PoutinePlugin plugin) {

		switch (plugin.getConfig().getString("dataStorage").toLowerCase()) {
		case "mysql":

			Database mysql = new MySQL(plugin, getTableName());
			mysql.load(Data.UUID, getParameters());
			data = mysql;

			break;
		case "flatfiles":

			data = new FlatFile(plugin, getTableName().toLowerCase(), false);

			break;
		case "sqlite":
		default:

			Database sqlite = new MySQL(plugin, getTableName());
			sqlite.load(Data.UUID, getParameters());
			data = sqlite;

			break;
		}

		return data;
	}

	/*******************************************************
	 * * Fields * *
	 *******************************************************/

	Pair<SavableParameter, UUID> uuid;
	private String name;

	private Location minPoint;
	private Location maxPoint;
	private Location start;
	private Location lobby;

	private int highestScore;
	private UUID highestPlayer;
	private int minPlayer;
	private int maxPlayer;

	private ColorManager colorManager;

	/*******************************************************
	 * * Constuctors * *
	 *******************************************************/

	public BaseArena(PoutinePlugin plugin, UUID uuid) {

		checkDataStorage(plugin);
		this.uuid = new Pair<SavableParameter, UUID>(Data.UUID, uuid);
		
		try {
			Map<SavableParameter, String> parameters = data.getIndividualData(this.uuid, getParameters());
			
			this.name = parameters.get(Data.NAME);

			World world = Bukkit.getWorld(UUID.fromString(parameters.get(Data.WORLD)));
			{
				int x = Integer.parseInt(parameters.get(Data.MIN_POINT_X));
				int y = Integer.parseInt(parameters.get(Data.MIN_POINT_Y));
				int z = Integer.parseInt(parameters.get(Data.MIN_POINT_Z));
				minPoint = new Location(world, x, y, z);
			}
			{
				int x = Integer.parseInt(parameters.get(Data.MAX_POINT_X));
				int y = Integer.parseInt(parameters.get(Data.MAX_POINT_Y));
				int z = Integer.parseInt(parameters.get(Data.MAX_POINT_Z));
				maxPoint = new Location(world, x, y, z);
			}
			{
				int x = Integer.parseInt(parameters.get(Data.LOBBY_X));
				int y = Integer.parseInt(parameters.get(Data.LOBBY_Y));
				int z = Integer.parseInt(parameters.get(Data.LOBBY_Z));
				float pitch = Float.parseFloat(parameters.get(Data.LOBBY_PITCH));
				float yaw = Float.parseFloat(parameters.get(Data.LOBBY_YAW));
				lobby = new Location(world, x, y, z, pitch, yaw);
			}
			{
				int x = Integer.parseInt(parameters.get(Data.START_X));
				int y = Integer.parseInt(parameters.get(Data.START_Y));
				int z = Integer.parseInt(parameters.get(Data.START_Z));
				float pitch = Float.parseFloat(parameters.get(Data.START_PITCH));
				float yaw = Float.parseFloat(parameters.get(Data.START_YAW));
				start = new Location(world, x, y, z, pitch, yaw);
			}

			highestScore = Integer.parseInt(parameters.get(Data.HIGHEST_SCORE));
			highestPlayer = UUID.fromString(parameters.get(Data.HIGHEST_PLAYER));

			minPlayer = Integer.parseInt(parameters.get(Data.MIN_PLAYER));
			maxPlayer = Integer.parseInt(parameters.get(Data.MAX_PLAYER));

			colorManager = new ColorManager(Long.parseLong(parameters.get(Data.COLOR_INDICE)));
			
		} catch (NumberFormatException ex) {
			plugin.getLogger().severe("Could not load arena " + uuid.toString() + " : " + ex);
		}
	}

	public BaseArena(PoutinePlugin plugin, String name, World world) {

		checkDataStorage(plugin);
		
		this.uuid = new Pair<SavableParameter, UUID>(Data.UUID, UUID.randomUUID());
		this.name = name;

		List<Pair<SavableParameter, String>> createParameters = new ArrayList<Pair<SavableParameter, String>>();
		createParameters.add(new Pair<SavableParameter, String>(Data.NAME, name));
		createParameters.add(new Pair<SavableParameter, String>(Data.WORLD, world.getName()));

		data.newInstance(uuid, createParameters);

		{
			int x = Data.MIN_POINT_X.<Integer>getDefaultValue();
			int y = Data.MIN_POINT_Y.<Integer>getDefaultValue();
			int z = Data.MIN_POINT_Z.<Integer>getDefaultValue();
			minPoint = new Location(world, x, y, z);
		}
		{
			int x = Data.MAX_POINT_X.<Integer>getDefaultValue();
			int y = Data.MAX_POINT_Y.<Integer>getDefaultValue();
			int z = Data.MAX_POINT_Z.<Integer>getDefaultValue();
			maxPoint = new Location(world, x, y, z);
		}
		{
			int x = Data.LOBBY_X.<Integer>getDefaultValue();
			int y = Data.LOBBY_Y.<Integer>getDefaultValue();
			int z = Data.LOBBY_Z.<Integer>getDefaultValue();
			float pitch = Data.LOBBY_PITCH.<Float>getDefaultValue();
			float yaw = Data.LOBBY_YAW.<Float>getDefaultValue();
			lobby = new Location(world, x, y, z, pitch, yaw);
		}
		{
			int x = Data.START_X.<Integer>getDefaultValue();
			int y = Data.START_Y.<Integer>getDefaultValue();
			int z = Data.START_Z.<Integer>getDefaultValue();
			float pitch = Data.START_PITCH.<Float>getDefaultValue();
			float yaw = Data.START_YAW.<Float>getDefaultValue();
			start = new Location(world, x, y, z, pitch, yaw);
		}

		highestScore = Data.HIGHEST_SCORE.<Integer>getDefaultValue();
		highestPlayer = UUID.fromString(Data.HIGHEST_PLAYER.<String>getDefaultValue());

		minPlayer = Data.MIN_PLAYER.<Integer>getDefaultValue();
		maxPlayer = Data.MAX_PLAYER.<Integer>getDefaultValue();

		colorManager = new ColorManager(Data.COLOR_INDICE.<Long>getDefaultValue());
	}

	/*******************************************************
	 * * Getters and Setters * *
	 *******************************************************/

	public static String getTableName() {
		return TABLE_NAME;
	}

	public DataStorage getDataStorage() {
		return data;
	}

	public UUID getUUID() {
		return uuid.getValue();
	}

	public String getName() {
		return name;
	}

	public Location getMinPoint() {
		return minPoint;
	}

	public void setMinPoint(Location location) {
		data.setInt(uuid, Data.MIN_POINT_X, location.getBlockX());
		data.setInt(uuid, Data.MIN_POINT_Y, location.getBlockY());
		data.setInt(uuid, Data.MIN_POINT_Z, location.getBlockZ());

		minPoint = location;
	}

	public Location getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(Location location) {
		data.setInt(uuid, Data.MAX_POINT_X, location.getBlockX());
		data.setInt(uuid, Data.MAX_POINT_Y, location.getBlockY());
		data.setInt(uuid, Data.MAX_POINT_Z, location.getBlockZ());

		maxPoint = location;
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location location) {
		data.setDouble(uuid, Data.LOBBY_X, location.getX());
		data.setDouble(uuid, Data.LOBBY_Y, location.getY());
		data.setDouble(uuid, Data.LOBBY_Z, location.getZ());
		data.setFloat(uuid, Data.LOBBY_PITCH, location.getPitch());
		data.setFloat(uuid, Data.LOBBY_YAW, location.getYaw());

		lobby = location;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location location) {
		data.setDouble(uuid, Data.START_X, location.getX());
		data.setDouble(uuid, Data.START_Y, location.getY());
		data.setDouble(uuid, Data.START_Z, location.getZ());
		data.setFloat(uuid, Data.START_PITCH, location.getPitch());
		data.setFloat(uuid, Data.START_YAW, location.getYaw());

		start = location;
	}

	public int getHighestScore() {
		return highestScore;
	}

	public UUID getHighestPlayer() {
		return highestPlayer;
	}

	public void setHighest(UUID player, int score) {
		data.setString(uuid, Data.HIGHEST_PLAYER, player.toString());
		data.setInt(uuid, Data.HIGHEST_SCORE, score);

		highestPlayer = player;
		highestScore = score;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int amount) {
		data.setInt(uuid, Data.MAX_PLAYER, amount);

		maxPlayer = amount;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public void setMinPlayer(int amount) {
		data.setInt(uuid, Data.MIN_PLAYER, amount);

		minPlayer = amount;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public void setColor(long colorIndice) {
		data.setLong(uuid, Data.COLOR_INDICE, colorIndice);
		colorManager.setColorIndice(colorIndice);
	}


	/*******************************************************
	 * * Data Enumeration * *
	 *******************************************************/

	private enum Data implements SavableParameter {
		UUID("uuid", DataValue.STRING),

		NAME("name", DataValue.STRING),

		WORLD("world", DataValue.STRING),

		MIN_POINT_X("minPointX", DataValue.INTEGER, 0),

		MIN_POINT_Y("minPointY", DataValue.INTEGER, 0),

		MIN_POINT_Z("minPointZ", DataValue.INTEGER, 0),

		MAX_POINT_X("maxPointX", DataValue.INTEGER, 0),

		MAX_POINT_Y("maxPointY", DataValue.INTEGER, 0),

		MAX_POINT_Z("maxPointZ", DataValue.INTEGER, 0),

		LOBBY_X("lobbyX", DataValue.DOUBLE, 0),

		LOBBY_Y("lobbyY", DataValue.DOUBLE, 0),

		LOBBY_Z("lobbyZ", DataValue.DOUBLE, 0),

		LOBBY_PITCH("lobbyPitch", DataValue.FLOAT, 0),

		LOBBY_YAW("lobbyYaw", DataValue.FLOAT, 0),

		START_X("startX", DataValue.DOUBLE, 0),

		START_Y("startY", DataValue.DOUBLE, 0),

		START_Z("startZ", DataValue.DOUBLE, 0),

		START_PITCH("startPitch", DataValue.FLOAT, 0),

		START_YAW("startYaw", DataValue.FLOAT, 0),

		HIGHEST_SCORE("highestScore", DataValue.INTEGER, 0),

		HIGHEST_PLAYER("highestPlayer", DataValue.STRING),

		MIN_PLAYER("minPlayer", DataValue.INTEGER, 1),

		MAX_PLAYER("maxPlayer", DataValue.INTEGER, 10),

		COLOR_INDICE("colorIndice", DataValue.LONG, 1);

		private String key;
		private DataValue dataValue;
		private String defaultValue;

		private Data(String key, DataValue dataValue, Object defaultValue) {
			this.key = key;
			this.dataValue = dataValue;
			this.defaultValue = String.valueOf(defaultValue);
		}

		private Data(String key, DataValue dataValue) {
			this.key = key;
			this.dataValue = dataValue;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getDefaultValue() throws ClassCastException {
			return (T) defaultValue;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public String getCreateQuerryPart() {
			StringBuilder builder = new StringBuilder();

			builder.append("`" + key + "` ");

			builder.append(dataValue.getSqlName());

			if (defaultValue != null)
				builder.append(" DEFAULT '" + defaultValue.toString() + "'");

			return builder.toString();
		}

		@Override
		public DataValue getType() {
			return dataValue;
		}
	}
}
