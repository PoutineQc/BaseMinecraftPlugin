package me.poutineqc.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.poutineqc.data.Column;
import me.poutineqc.data.DataStorage;
import me.poutineqc.data.Database;
import me.poutineqc.data.FlatFile;
import me.poutineqc.data.MySQL;
import me.poutineqc.data.ValueType;

public class Arena {
	private enum Data {
		NAME("name"),
		WORLD("world"),
		
		MIN_POINT_X("minPointX"),
		MIN_POINT_Y("minPointY"),
		MIN_POINT_Z("minPointZ"),
		MAX_POINT_X("maxPointX"),
		MAX_POINT_Y("maxPointY"),
		MAX_POINT_Z("maxPointZ"),
		
		LOBBY_X("lobbyX"),
		LOBBY_Y("lobbyY"),
		LOBBY_Z("lobbyZ"),
		LOBBY_PITCH("lobbyPitch"),
		LOBBY_YAW("lobbyYaw"),
		
		START_X("startX"),
		START_Y("startY"),
		START_Z("startZ"),
		START_PITCH("startPitch"),
		START_YAW("startYaw"),
		
		HIGHEST_SCORE("highestScore"),
		HIGHEST_PLAYER("highestPlayer"),
		MIN_PLAYER("minPlayer"),
		MAX_PLAYER("maxPlayer"),
		
		COLOR_INDICE("colorIndice");

		private String key;

		private Data(String dataName) {
			this.key = dataName;
		}
	}

	private static Column primary;
	private static List<Column> columns;

	static {
		primary = new Column(Data.NAME.key, ValueType.STRING);

		columns = new ArrayList<Column>();

		columns.add(new Column(Data.WORLD.key, ValueType.STRING));

		columns.add(new Column(Data.MIN_POINT_X.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.MIN_POINT_Y.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.MIN_POINT_Z.key, ValueType.INTEGER, String.valueOf(0)));

		columns.add(new Column(Data.MAX_POINT_X.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.MAX_POINT_Y.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.MAX_POINT_Z.key, ValueType.INTEGER, String.valueOf(0)));

		columns.add(new Column(Data.LOBBY_X.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.LOBBY_Y.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.LOBBY_Z.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.LOBBY_PITCH.key, ValueType.FLOAT, String.valueOf(0)));
		columns.add(new Column(Data.LOBBY_YAW.key, ValueType.FLOAT, String.valueOf(0)));

		columns.add(new Column(Data.START_X.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.START_Y.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.START_Z.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.START_PITCH.key, ValueType.FLOAT, String.valueOf(0)));
		columns.add(new Column(Data.START_YAW.key, ValueType.FLOAT, String.valueOf(0)));

		columns.add(new Column(Data.HIGHEST_SCORE.key, ValueType.INTEGER, String.valueOf(0)));
		columns.add(new Column(Data.HIGHEST_PLAYER.key, ValueType.STRING, "null"));
		columns.add(new Column(Data.MIN_PLAYER.key, ValueType.INTEGER, String.valueOf(1)));
		columns.add(new Column(Data.MAX_PLAYER.key, ValueType.INTEGER, String.valueOf(10)));

		columns.add(new Column(Data.COLOR_INDICE.key, ValueType.INTEGER, String.valueOf(0)));
	}

	private static DataStorage data;

	private String name;

	private Location minPoint;
	private Location maxPoint;
	private Location lobby;
	private Location start;

	private int highestScore;
	private String highestPlayer;
	private int minPlayer;
	private int maxPlayer;

	private ColorManager colorManager;

	public Arena(String name) {

		this.name = name;

		World world = Bukkit.getWorld(UUID.fromString(data.getString(name, Data.WORLD.key)));
		{
			int x = data.getInt(name, Data.MIN_POINT_X.key);
			int y = data.getInt(name, Data.MIN_POINT_Y.key);
			int z = data.getInt(name, Data.MIN_POINT_Z.key);
			minPoint = new Location(world, x, y, z);
		}
		{
			int x = data.getInt(name, Data.MAX_POINT_X.key);
			int y = data.getInt(name, Data.MAX_POINT_Y.key);
			int z = data.getInt(name, Data.MAX_POINT_Z.key);
			maxPoint = new Location(world, x, y, z);
		}
		{
			int x = data.getInt(name, Data.LOBBY_X.key);
			int y = data.getInt(name, Data.LOBBY_Y.key);
			int z = data.getInt(name, Data.LOBBY_Z.key);
			float pitch = data.getFloat(name, Data.LOBBY_PITCH.key);
			float yaw = data.getFloat(name, Data.LOBBY_YAW.key);
			lobby = new Location(world, x, y, z, pitch, yaw);
		}
		{
			int x = data.getInt(name, Data.START_X.key);
			int y = data.getInt(name, Data.START_Y.key);
			int z = data.getInt(name, Data.START_Z.key);
			float pitch = data.getFloat(name, Data.START_PITCH.key);
			float yaw = data.getFloat(name, Data.START_YAW.key);
			start = new Location(world, x, y, z, pitch, yaw);
		}

		highestScore = data.getInt(name, Data.HIGHEST_SCORE.key);
		highestPlayer = data.getString(name, Data.HIGHEST_PLAYER.key);
		minPlayer = data.getInt(name, Data.MIN_PLAYER.key);
		maxPlayer = data.getInt(name, Data.MAX_PLAYER.key);

		colorManager = new ColorManager(data.getLong(name, Data.COLOR_INDICE.key));
	}

	public void updateMinPoint(Location location) {
		data.setInt(name, Data.MIN_POINT_X.key, location.getBlockX());
		data.setInt(name, Data.MIN_POINT_Y.key, location.getBlockY());
		data.setInt(name, Data.MIN_POINT_Z.key, location.getBlockZ());

		minPoint = location;
	}

	public void updateMaxPoint(Location location) {
		data.setInt(name, Data.MAX_POINT_X.key, location.getBlockX());
		data.setInt(name, Data.MAX_POINT_Y.key, location.getBlockY());
		data.setInt(name, Data.MAX_POINT_Z.key, location.getBlockZ());

		maxPoint = location;
	}

	public void updateLobby(Location location) {
		data.setDouble(name, Data.LOBBY_X.key, location.getX());
		data.setDouble(name, Data.LOBBY_Y.key, location.getY());
		data.setDouble(name, Data.LOBBY_Z.key, location.getZ());
		data.setFloat(name, Data.LOBBY_PITCH.key, location.getPitch());
		data.setFloat(name, Data.LOBBY_YAW.key, location.getYaw());

		lobby = location;
	}

	public void updateStart(Location location) {
		data.setDouble(name, Data.START_X.key, location.getX());
		data.setDouble(name, Data.START_Y.key, location.getY());
		data.setDouble(name, Data.START_Z.key, location.getZ());
		data.setFloat(name, Data.START_PITCH.key, location.getPitch());
		data.setFloat(name, Data.START_YAW.key, location.getYaw());

		start = location;
	}

	public void updateHighest(String player, int score) {
		data.setString(name, Data.HIGHEST_PLAYER.key, player);
		data.setInt(name, Data.HIGHEST_SCORE.key, score);

		highestPlayer = player;
		highestScore = score;
	}

	public void updateMaxPlayer(int amount) {
		data.setInt(name, Data.MAX_PLAYER.key, amount);

		maxPlayer = amount;
	}

	public void updateMinPlayer(int amount) {
		data.setInt(name, Data.MIN_PLAYER.key, amount);

		minPlayer = amount;
	}

	public void updateColor(long colorIndice) {
		data.setLong(name, Data.COLOR_INDICE.key, colorIndice);
		colorManager.setColorIndice(colorIndice);
	}

	public static DataStorage openDataStorage(Plugin plugin) {

		String className = Arena.class.getSimpleName().toLowerCase();
		switch (plugin.getConfig().getString("dataStorage").toLowerCase()) {
		case "mysql":

			Database mysql = new MySQL(plugin, className);
			mysql.load(primary, columns);
			data = mysql;

			break;
		case "flatfiles":

			data = new FlatFile(plugin, className, false);

			break;
		case "sqlite":
		default:

			Database sqlite = new MySQL(plugin, className);
			sqlite.load(primary, columns);
			data = sqlite;

			break;
		}

		return data;
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

}
