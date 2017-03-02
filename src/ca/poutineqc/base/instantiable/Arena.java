package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.JSON;
import ca.poutineqc.base.data.MySQL;
import ca.poutineqc.base.data.SQLite;
import ca.poutineqc.base.data.YAML;
import ca.poutineqc.base.data.values.SInteger;
import ca.poutineqc.base.data.values.SList;
import ca.poutineqc.base.data.values.SLocation;
import ca.poutineqc.base.data.values.SString;
import ca.poutineqc.base.data.values.SUUID;
import ca.poutineqc.base.data.values.StringSavableValue;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.ColorManager;
import ca.poutineqc.base.utils.Pair;
import ca.poutineqc.base.utils.PowerOfTwo;

/**
 * A base class for an Arena. An arena is a zone where players may play games
 * and it has many different parameters.
 * 
 * @author Sébastien Chagnon
 *
 */
public class Arena implements Savable {

	// =========================================================================
	// Static Fields
	// =========================================================================

	/**
	 * Name of the DataStorage table
	 */
	public static final String TABLE_NAME = "ARENA";

	protected static DataStorage data;

	// =========================================================================
	// Static Methods
	// =========================================================================

	/**
	 * Checks if the DataStorage is instantiated. If it is not, instantiate it.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @see Library
	 */
	public static void checkDataStorage(PPlugin plugin) {
		if (data == null) {
			data = openDataStorage(plugin);

			data.createTable(new ArrayList<SavableParameter>(Arrays.asList(Data.values())));
		}
	}

	/**
	 * Returns a list of all the different identifications saved in the
	 * DataStorage. Returns null if there are no Savables saved in the
	 * DataStorage.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @return a list of all the different identifications saved in the
	 *         DataStorage
	 * @see Library
	 */
	public static List<SUUID> getAllIdentifications(Library plugin) {
		checkDataStorage(plugin);

		return data.getAllIdentifications(Data.UUID);
	}

	/**
	 * Returns an instance of a Data Storage. It's child class is determined by
	 * a setting saved in the config.yml file.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @return an instance of a DataStorage
	 * @see DataStorage
	 * @see Library
	 */
	public static DataStorage openDataStorage(PPlugin plugin) {

		switch (plugin.getConfig().getString("dataStorage").toLowerCase()) {
		case "mysql":

			data = new MySQL(plugin, getTableName());

			break;
		case "json":

			data = new JSON(plugin, TABLE_NAME.toLowerCase(), false);

			break;
		case "yaml":
		case "yml":

			data = new YAML(plugin, TABLE_NAME.toLowerCase(), false);

			break;
		case "sqlite":
		default:

			data = new SQLite(plugin, getTableName());

			break;
		}

		return data;
	}

	// =========================================================================
	// Fields
	// =========================================================================

	final private SUUID uuid;
	private SString name;

	private SLocation minPoint;
	private SLocation maxPoint;
	private SLocation lobby;
	private SList<SLocation> start;

	private SInteger highestScore;
	private SString highestPlayer;
	private SInteger minPlayer;
	private SInteger maxPlayer;

	private ColorManager colorManager;

	// =========================================================================
	// Constructors
	// =========================================================================

	/**
	 * Parameter Constructor. Used for creating a instance that is already saved
	 * in the database.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @param uuid
	 *            the UUID of the new PArena
	 * @see UUID
	 */
	public Arena(PPlugin plugin, UUID uuid) {

		checkDataStorage(plugin);

		this.uuid = new SUUID(uuid);

		Map<SavableParameter, String> parameters = data.getIndividualData(Data.UUID, this.uuid, getParameters());
		this.name = new SString(parameters.get(Data.NAME), PowerOfTwo.POWER_32);

		this.minPoint = new SLocation(parameters.get(Data.MIN_POINT_X));
		this.maxPoint = new SLocation(parameters.get(Data.MAX_POINT_X));
		this.lobby = new SLocation(parameters.get(Data.LOBBY_X));
		this.start = new SList<SLocation>(parameters.get(Data.START_X), PowerOfTwo.POWER_16) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2712212492748079359L;

			@Override
			public int getElementMaxStringLength() {
				return SLocation.MAX_STRING_LENGTH;
			}

			@Override
			public SLocation convert(String value) {
				return new SLocation(value);
			}

			@Override
			public SLocation convert(JsonObject value) {
				return new SLocation(value);
			}
		};

		this.highestScore = new SInteger(parameters.get(Data.HIGHEST_SCORE));
		this.highestPlayer = new SString(parameters.get(Data.HIGHEST_PLAYER), PowerOfTwo.POWER_64);

		this.minPlayer = new SInteger(parameters.get(Data.MIN_PLAYER));
		this.maxPlayer = new SInteger(parameters.get(Data.MAX_PLAYER));

		this.colorManager = new ColorManager(parameters.get(Data.COLOR_INDICE));

	}

	public Arena(Library plugin, String name, World world) {

		checkDataStorage(plugin);

		this.uuid = new SUUID(UUID.randomUUID());
		this.name = new SString(name, PowerOfTwo.POWER_32);

		this.minPoint = new SLocation(Data.MIN_POINT_X.getDefaultValue());
		this.maxPoint = new SLocation(Data.MAX_POINT_X.getDefaultValue());
		this.lobby = new SLocation(Data.LOBBY_X.getDefaultValue());
		this.start = new SList<SLocation>(Data.START_X.getDefaultValue(), PowerOfTwo.POWER_16) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2712212492748079359L;

			@Override
			public int getElementMaxStringLength() {
				return SLocation.MAX_STRING_LENGTH;
			}

			@Override
			public SLocation convert(String value) {
				return new SLocation(value);
			}

			@Override
			public SLocation convert(JsonObject value) {
				return new SLocation(value);
			}
		};

		this.highestScore = new SInteger(Data.HIGHEST_SCORE.getDefaultValue());
		this.highestPlayer = new SString(Data.HIGHEST_PLAYER.getDefaultValue(), PowerOfTwo.POWER_64);

		this.minPlayer = new SInteger(Data.MIN_PLAYER.getDefaultValue());
		this.maxPlayer = new SInteger(Data.MAX_PLAYER.getDefaultValue());

		this.colorManager = new ColorManager(Data.COLOR_INDICE.getDefaultValue());

		List<Pair<SavableParameter, StringSavableValue>> createParameters = new ArrayList<Pair<SavableParameter, StringSavableValue>>();
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.NAME, this.name));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.MIN_POINT_X, this.minPoint));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.MAX_POINT_X, this.maxPoint));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.LOBBY_X, this.lobby));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.START_X, this.start));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.HIGHEST_SCORE, this.highestScore));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.HIGHEST_PLAYER, this.highestPlayer));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.MIN_PLAYER, this.minPlayer));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.MAX_PLAYER, this.maxPlayer));
		createParameters.add(new Pair<SavableParameter, StringSavableValue>(Data.COLOR_INDICE, this.colorManager));
		data.newInstance(Data.UUID, uuid, createParameters);

	}

	/*
	 * * Getters and Setters * *
	 *******************************************************/

	public static String getTableName() {
		return TABLE_NAME;
	}

	public DataStorage getDataStorage() {
		return data;
	}

	public UUID getUUID() {
		return uuid.getUUID();
	}

	public String getName() {
		return name.toString();
	}

	public Location getMinPoint() {
		return minPoint;
	}

	public void setMinPoint(Location location) {
		minPoint = new SLocation(location);
		data.setStringSavableValue(Data.UUID, uuid, Data.MIN_POINT_X, minPoint);
	}

	public Location getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(Location location) {
		maxPoint = new SLocation(location);
		data.setStringSavableValue(Data.UUID, uuid, Data.MAX_POINT_X, maxPoint);
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location location) {
		lobby = new SLocation(location);

		data.setStringSavableValue(Data.UUID, uuid, Data.LOBBY_X, lobby);
	}

	public Location getStart(int index) {
		return start.get(index);
	}

	public void addStart(Location location) {
		start.add(new SLocation(location));

		data.setStringSavableValue(Data.UUID, uuid, Data.START_X, start);
	}

	public Location removeStart(int index) {
		return start.get(index);
	}

	public int getHighestScore() {
		return highestScore.getInt();
	}

	public UUID getHighestPlayer() {
		return UUID.fromString(highestPlayer.toString());
	}

	public void setHighest(UUID player, int score) {
		highestPlayer.setString(player.toString());
		highestScore.setInt(score);

		data.setStringSavableValue(Data.UUID, uuid, Data.HIGHEST_PLAYER, highestPlayer);
		data.setStringSavableValue(Data.UUID, uuid, Data.HIGHEST_SCORE, highestScore);
	}

	public int getMaxPlayer() {
		return maxPlayer.getInt();
	}

	public void setMaxPlayer(int amount) {
		maxPlayer.setInt(amount);

		data.setStringSavableValue(Data.UUID, uuid, Data.MAX_PLAYER, maxPlayer);
	}

	public int getMinPlayer() {
		return minPlayer.getInt();
	}

	public void setMinPlayer(int amount) {
		minPlayer.setInt(amount);

		data.setStringSavableValue(Data.UUID, uuid, Data.MIN_PLAYER, minPlayer);
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public void setColor(long colorIndice) {
		colorManager.setColorIndice(colorIndice);
		data.setStringSavableValue(Data.UUID, uuid, Data.COLOR_INDICE, this.colorManager);
	}

	/*
	 * * Data Enumeration * *
	 *******************************************************/

	@Override
	public List<SavableParameter> getParameters() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();
		for (Data parameter : Data.values())
			returnValue.add(parameter);

		return returnValue;
	}

	/**
	 * Represents all the Parameters from a BaseArena that may be saved in a
	 * DataStorage.
	 * 
	 * @author Sébastien Chagnon
	 * @see SavableParameter
	 */
	private enum Data implements SavableParameter {
		UUID("uuid", "00000000-0000-0000-0000-000000000000"),

		NAME("name", "·······················arenaName"),

		MIN_POINT_X("minPoint",
				"00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"),

		MAX_POINT_X("maxPoint",
				"00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"),

		LOBBY_X("lobby",
				"00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"),

		SPECTATE("spectate",
				"00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"),

		START_X("start",
			         	  "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"
						+ "00000000-0000-0000-0000-000000000000···············0···············0···············0·······0·······0"),

		HIGHEST_SCORE("highestScore", "·······0"),

		HIGHEST_PLAYER("highestPlayer", "····························null"),

		MIN_PLAYER("minPlayer", "·······1"),

		MAX_PLAYER("maxPlayer", "·······c"),

		COLOR_INDICE("colorIndice", "···············1");

		private String key;
		private String defaultValue;

		private Data(String key, String defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public String getDefaultValue() {
			return defaultValue;
		}
	}
}
