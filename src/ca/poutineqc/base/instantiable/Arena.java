package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.FlatFile;
import ca.poutineqc.base.data.MySQL;
import ca.poutineqc.base.data.SQLite;
import ca.poutineqc.base.data.values.BinaryValue;
import ca.poutineqc.base.data.values.PLocation;
import ca.poutineqc.base.data.values.PUUID;
import ca.poutineqc.base.data.values.SInteger;
import ca.poutineqc.base.data.values.SList;
import ca.poutineqc.base.data.values.SString;
import ca.poutineqc.base.data.values.SValue;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.ColorManager;
import ca.poutineqc.base.utils.Pair;

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
	 * @see PPlugin
	 */
	public static void checkDataStorage(PPlugin plugin) {
		if (data == null) {
			data = openDataStorage(plugin);
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
	 * @see PPlugin
	 */
	public static List<UUID> getAllIdentifications(PPlugin plugin) {
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
	 * @see PPlugin
	 */
	public static DataStorage openDataStorage(PPlugin plugin) {

		switch (plugin.getConfig().getString("dataStorage").toLowerCase()) {
		case "mysql":

			data = new MySQL(plugin, getTableName());

			break;
		case "flatfiles":

			data = new FlatFile(plugin, getTableName().toLowerCase());

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

	final private PUUID uuid;
	private SString name;

	private PLocation minPoint;
	private PLocation maxPoint;
	private PLocation lobby;
	private SList<PLocation> start;

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

		this.uuid = new PUUID(uuid);

		Map<SavableParameter, String> parameters = data.getIndividualData(Data.UUID, this.uuid, getParameters());
		this.name = new SString(parameters.get(Data.NAME), BinaryValue.BINARY_32);

		this.minPoint = new PLocation(parameters.get(Data.MIN_POINT_X));
		this.maxPoint = new PLocation(parameters.get(Data.MAX_POINT_X));
		this.lobby = new PLocation(parameters.get(Data.LOBBY_X));
		this.start = new SList<PLocation>(parameters.get(Data.START_X), BinaryValue.BINARY_16) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2712212492748079359L;

			@Override
			public int getElementMaxStringLength() {
				return PLocation.MAX_STRING_LENGTH;
			}

			@Override
			public PLocation convert(String value) {
				return new PLocation(value);
			}
		};

		this.highestScore = new SInteger(parameters.get(Data.HIGHEST_SCORE));
		this.highestPlayer = new SString(parameters.get(Data.HIGHEST_PLAYER), BinaryValue.BINARY_64);

		this.minPlayer = new SInteger(parameters.get(Data.MIN_PLAYER));
		this.maxPlayer = new SInteger(parameters.get(Data.MAX_PLAYER));

		this.colorManager = new ColorManager(parameters.get(Data.COLOR_INDICE));

	}

	public Arena(PPlugin plugin, String name, World world) {

		checkDataStorage(plugin);

		this.uuid = new PUUID(UUID.randomUUID());
		this.name = new SString(name, BinaryValue.BINARY_32);

		this.minPoint = new PLocation(Data.MIN_POINT_X.getDefaultValue());
		this.maxPoint = new PLocation(Data.MAX_POINT_X.getDefaultValue());
		this.lobby = new PLocation(Data.LOBBY_X.getDefaultValue());
		this.start = new SList<PLocation>(Data.START_X.getDefaultValue(), BinaryValue.BINARY_16) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2712212492748079359L;

			@Override
			public int getElementMaxStringLength() {
				return PLocation.MAX_STRING_LENGTH;
			}

			@Override
			public PLocation convert(String value) {
				return new PLocation(value);
			}
		};

		this.highestScore = new SInteger(Data.HIGHEST_SCORE.getDefaultValue());
		this.highestPlayer = new SString(Data.HIGHEST_PLAYER.getDefaultValue(), BinaryValue.BINARY_64);

		this.minPlayer = new SInteger(Data.MIN_PLAYER.getDefaultValue());
		this.maxPlayer = new SInteger(Data.MAX_PLAYER.getDefaultValue());

		this.colorManager = new ColorManager(Data.COLOR_INDICE.getDefaultValue());
		
		
		List<Pair<SavableParameter, SValue>> createParameters = new ArrayList<Pair<SavableParameter, SValue>>();
		createParameters.add(new Pair<SavableParameter, SValue>(Data.UUID, this.uuid));
		createParameters.add(new Pair<SavableParameter, SValue>(Data.NAME, this.name));
		createParameters.add(new Pair<SavableParameter, SValue>(Data.HIGHEST_SCORE, this.highestScore));
		createParameters.add(new Pair<SavableParameter, SValue>(Data.HIGHEST_PLAYER, this.highestPlayer));
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
		minPoint = new PLocation(location);
		data.set(Data.UUID, uuid, Data.MIN_POINT_X, minPoint);
	}

	public Location getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(Location location) {
		maxPoint = new PLocation(location);
		data.set(Data.UUID, uuid, Data.MAX_POINT_X, maxPoint);
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location location) {
		lobby = new PLocation(location);

		data.set(Data.UUID, uuid, Data.LOBBY_X, lobby);
	}

	public Location getStart(int index) {
		return start.get(index);
	}

	public void addStart(Location location) {
		start.add(new PLocation(location));

		data.set(Data.UUID, uuid, Data.START_X, start);
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

		data.set(Data.UUID, uuid, Data.HIGHEST_PLAYER, highestPlayer);
		data.set(Data.UUID, uuid, Data.HIGHEST_SCORE, highestScore);
	}

	public int getMaxPlayer() {
		return maxPlayer.getInt();
	}

	public void setMaxPlayer(int amount) {
		maxPlayer.setInt(amount);

		data.set(Data.UUID, uuid, Data.MAX_PLAYER, maxPlayer);
	}

	public int getMinPlayer() {
		return minPlayer.getInt();
	}

	public void setMinPlayer(int amount) {
		minPlayer.setInt(amount);

		data.set(Data.UUID, uuid, Data.MIN_PLAYER, minPlayer);
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public void setColor(long colorIndice) {
		colorManager.setColorIndice(colorIndice);
		data.set(Data.UUID, uuid, Data.COLOR_INDICE, this.colorManager);
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
		UUID("uuid", ""),

		NAME("name", "·······················arenaName"),

		MIN_POINT_X("minPoint", ""),

		MAX_POINT_X("maxPoint", ""),

		LOBBY_X("lobby", ""),

		SPECTATE("spectate", ""),

		START_X("start", ""),

		HIGHEST_SCORE("highestScore", ""),

		HIGHEST_PLAYER("highestPlayer", ""),

		MIN_PLAYER("minPlayer", ""),

		MAX_PLAYER("maxPlayer", ""),

		COLOR_INDICE("colorIndice", "");

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
