package me.poutineqc.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.poutineqc.data.DataStorage;
import me.poutineqc.data.Database;
import me.poutineqc.data.FlatFile;
import me.poutineqc.data.MySQL;
import me.poutineqc.plugin.PoutinePlugin;
import me.poutineqc.utils.ColorManager;

public abstract class BaseArena implements Savable {

	private static final String TABLE_NAME = "ARENA";
	
	protected DataStorage data;

	private UUID uuid;
	private String name;

	private Location minPoint;
	private Location maxPoint;
	private Location start;
	private Location lobby;

	private int highestScore;
	private String highestPlayer;
	private int minPlayer;
	private int maxPlayer;

	private ColorManager colorManager;


	public BaseArena(UUID uuid) {

		this.uuid = uuid;
		this.name = data.getString(uuid, Data.NAME);

		World world = Bukkit.getWorld(UUID.fromString(data.getString(uuid, Data.WORLD)));
		{
			int x = data.getInt(uuid, Data.MIN_POINT_X);
			int y = data.getInt(uuid, Data.MIN_POINT_Y);
			int z = data.getInt(uuid, Data.MIN_POINT_Z);
			minPoint = new Location(world, x, y, z);
		}
		{
			int x = data.getInt(uuid, Data.MAX_POINT_X);
			int y = data.getInt(uuid, Data.MAX_POINT_Y);
			int z = data.getInt(uuid, Data.MAX_POINT_Z);
			maxPoint = new Location(world, x, y, z);
		}
		{
			int x = data.getInt(uuid, Data.LOBBY_X);
			int y = data.getInt(uuid, Data.LOBBY_Y);
			int z = data.getInt(uuid, Data.LOBBY_Z);
			float pitch = data.getFloat(uuid, Data.LOBBY_PITCH);
			float yaw = data.getFloat(uuid, Data.LOBBY_YAW);
			lobby = new Location(world, x, y, z, pitch, yaw);
		}
		{
			int x = data.getInt(uuid, Data.START_X);
			int y = data.getInt(uuid, Data.START_Y);
			int z = data.getInt(uuid, Data.START_Z);
			float pitch = data.getFloat(uuid, Data.START_PITCH);
			float yaw = data.getFloat(uuid, Data.START_YAW);
			start = new Location(world, x, y, z, pitch, yaw);
		}

		highestScore = data.getInt(uuid, Data.HIGHEST_SCORE);
		highestPlayer = data.getString(uuid, Data.HIGHEST_PLAYER);

		minPlayer = data.getInt(uuid, Data.MIN_PLAYER);
		maxPlayer = data.getInt(uuid, Data.MAX_PLAYER);

		colorManager = new ColorManager(data.getLong(uuid, Data.COLOR_INDICE));
	}

	public void updateMinPoint(Location location) {
		data.setInt(uuid, Data.MIN_POINT_X, location.getBlockX());
		data.setInt(uuid, Data.MIN_POINT_Y, location.getBlockY());
		data.setInt(uuid, Data.MIN_POINT_Z, location.getBlockZ());

		minPoint = location;
	}

	public void updateMaxPoint(Location location) {
		data.setInt(uuid, Data.MAX_POINT_X, location.getBlockX());
		data.setInt(uuid, Data.MAX_POINT_Y, location.getBlockY());
		data.setInt(uuid, Data.MAX_POINT_Z, location.getBlockZ());

		maxPoint = location;
	}

	public void updateLobby(Location location) {
		data.setDouble(uuid, Data.LOBBY_X, location.getX());
		data.setDouble(uuid, Data.LOBBY_Y, location.getY());
		data.setDouble(uuid, Data.LOBBY_Z, location.getZ());
		data.setFloat(uuid, Data.LOBBY_PITCH, location.getPitch());
		data.setFloat(uuid, Data.LOBBY_YAW, location.getYaw());

		lobby = location;
	}

	public void updateStart(Location location) {
		data.setDouble(uuid, Data.START_X, location.getX());
		data.setDouble(uuid, Data.START_Y, location.getY());
		data.setDouble(uuid, Data.START_Z, location.getZ());
		data.setFloat(uuid, Data.START_PITCH, location.getPitch());
		data.setFloat(uuid, Data.START_YAW, location.getYaw());

		start = location;
	}

	public void updateHighest(String player, int score) {
		data.setString(uuid, Data.HIGHEST_PLAYER, player);
		data.setInt(uuid, Data.HIGHEST_SCORE, score);

		highestPlayer = player;
		highestScore = score;
	}

	public void updateMaxPlayer(int amount) {
		data.setInt(uuid, Data.MAX_PLAYER, amount);

		maxPlayer = amount;
	}

	public void updateMinPlayer(int amount) {
		data.setInt(uuid, Data.MIN_PLAYER, amount);

		minPlayer = amount;
	}

	public void updateColor(long colorIndice) {
		data.setLong(uuid, Data.COLOR_INDICE, colorIndice);
		colorManager.setColorIndice(colorIndice);
	}

	public String getName() {
		return name;
	}

	public Location getMinPoint() {
		return minPoint;
	}

	public Location getMaxPoint() {
		return maxPoint;
	}

	public Location getLobby() {
		return lobby;
	}

	public Location getStart() {
		return start;
	}

	public int getHighestScore() {
		return highestScore;
	}

	public String getHighestPlayer() {
		return highestPlayer;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	private enum Data implements SavableParameter {
		UUID("uuid", DataValue.STRING), NAME("name", DataValue.STRING), WORLD("world", DataValue.STRING),

		MIN_POINT_X("minPointX", DataValue.INTEGER, 0), MIN_POINT_Y("minPointY", DataValue.INTEGER, 0), MIN_POINT_Z(
				"minPointZ", DataValue.INTEGER, 0), MAX_POINT_X("maxPointX", DataValue.INTEGER, 0), MAX_POINT_Y(
						"maxPointY", DataValue.INTEGER, 0), MAX_POINT_Z("maxPointZ", DataValue.INTEGER, 0),

		LOBBY_X("lobbyX", DataValue.DOUBLE, 0), LOBBY_Y("lobbyY", DataValue.DOUBLE, 0), LOBBY_Z("lobbyZ",
				DataValue.DOUBLE,
				0), LOBBY_PITCH("lobbyPitch", DataValue.FLOAT, 0), LOBBY_YAW("lobbyYaw", DataValue.FLOAT, 0),

		START_X("startX", DataValue.DOUBLE, 0), START_Y("startY", DataValue.DOUBLE, 0), START_Z("startZ",
				DataValue.DOUBLE,
				0), START_PITCH("startPitch", DataValue.FLOAT, 0), START_YAW("startYaw", DataValue.FLOAT, 0),

		HIGHEST_SCORE("highestScore", DataValue.INTEGER, 0), HIGHEST_PLAYER("highestPlayer", DataValue.STRING,
				"null"), MIN_PLAYER("minPlayer", DataValue.INTEGER, 1), MAX_PLAYER("maxPlayer", DataValue.INTEGER, 10),

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
	}

	public String getTableName() {
		return TABLE_NAME;
	}
	
	public DataStorage getDataStorage() {
		return data;
	}
	
	public List<SavableParameter> getParameters() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();
		
		for (Data parameter : Data.values())
			returnValue.add(parameter);
		
		returnValue.addAll(getChildParameters());
		
		return returnValue;
	}
	
	public abstract List<SavableParameter> getChildParameters();
	
	public UUID getUUID() {
		return uuid;
	}

	public DataStorage openDataStorage(PoutinePlugin plugin, String tableName) {

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

}
