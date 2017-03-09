package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.DataStorage;
import ca.poutineqc.base.datastorage.JSONSerializable;
import ca.poutineqc.base.datastorage.StringSerializable;
import ca.poutineqc.base.datastorage.serializable.SInteger;
import ca.poutineqc.base.datastorage.serializable.SList;
import ca.poutineqc.base.datastorage.serializable.SLocation;
import ca.poutineqc.base.datastorage.serializable.SString;
import ca.poutineqc.base.datastorage.serializable.SUUID;
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
public class Arena implements Savable, JSONSerializable {

	// =========================================================================
	// Static Fields
	// =========================================================================

	/**
	 * Name of the DataStorage table
	 */
	public static final String TABLE_NAME = "ARENA";


	// =========================================================================
	// Fields
	// =========================================================================

	private DataStorage data;
	
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
	public Arena(PPlugin plugin, DataStorage data, UUID uuid) {
		
		this.data = data;

		this.uuid = new SUUID(uuid);

		Map<SavableParameter, String> parameters = data.getIndividualData(Data.UUID, this.uuid, Data.values());
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

	public Arena(PPlugin plugin, DataStorage data, String name, World world) {
		
		this.data = data;

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

		List<Pair<SavableParameter, StringSerializable>> createParameters = new ArrayList<Pair<SavableParameter, StringSerializable>>();
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.NAME, this.name));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.MIN_POINT_X, this.minPoint));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.MAX_POINT_X, this.maxPoint));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.LOBBY_X, this.lobby));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.START_X, this.start));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.HIGHEST_SCORE, this.highestScore));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.HIGHEST_PLAYER, this.highestPlayer));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.MIN_PLAYER, this.minPlayer));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.MAX_PLAYER, this.maxPlayer));
		createParameters.add(new Pair<SavableParameter, StringSerializable>(Data.COLOR_INDICE, this.colorManager));
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
		return minPoint.getLocation();
	}

	public void setMinPoint(Location location) {
		minPoint = new SLocation(location);
		data.setStringSavableValue(Data.UUID, uuid, Data.MIN_POINT_X, minPoint);
	}

	public Location getMaxPoint() {
		return maxPoint.getLocation();
	}

	public void setMaxPoint(Location location) {
		maxPoint = new SLocation(location);
		data.setStringSavableValue(Data.UUID, uuid, Data.MAX_POINT_X, maxPoint);
	}

	public Location getLobby() {
		return lobby.getLocation();
	}

	public void setLobby(Location location) {
		lobby = new SLocation(location);

		data.setStringSavableValue(Data.UUID, uuid, Data.LOBBY_X, lobby);
	}

	public Location getStart(int index) {
		return start.get(index).getLocation();
	}

	public void addStart(Location location) {
		start.add(new SLocation(location));

		data.setStringSavableValue(Data.UUID, uuid, Data.START_X, start);
	}

	public Location removeStart(int index) {
		return start.get(index).getLocation();
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

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.add(Data.UUID.key, uuid.toJsonObject());
		json.add(Data.NAME.key, name.toJsonObject());
		json.add(Data.MIN_POINT_X.key, minPoint.toJsonObject());
		json.add(Data.MAX_POINT_X.key, maxPoint.toJsonObject());
		json.add(Data.LOBBY_X.key, uuid.toJsonObject());
		json.add(Data.START_X.key, uuid.toJsonObject());
		json.add(Data.HIGHEST_PLAYER.key, highestPlayer.toJsonObject());
		json.add(Data.HIGHEST_SCORE.key, highestScore.toJsonObject());
		json.add(Data.MIN_PLAYER.key, minPlayer.toJsonObject());
		json.add(Data.MAX_PLAYER.key, maxPlayer.toJsonObject());
		json.add(Data.COLOR_INDICE.key, colorManager.toJsonObject());
		return json;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(Data.UUID.key + ":" + uuid.toString() + ", ");
		sb.append(Data.NAME.key + ":" + name.toString() + ", ");
		sb.append(Data.MIN_POINT_X.key + ":" + minPoint.toString() + ", ");
		sb.append(Data.MAX_POINT_X.key + ":" + maxPoint.toString() + ", ");
		sb.append(Data.LOBBY_X.key + ":" + uuid.toString() + ", ");
		sb.append(Data.START_X.key + ":" + uuid.toString() + ", ");
		sb.append(Data.HIGHEST_PLAYER.key + ":" + highestPlayer.toString() + ", ");
		sb.append(Data.HIGHEST_SCORE.key + ":" + highestScore.toString() + ", ");
		sb.append(Data.MIN_PLAYER.key + ":" + minPlayer.toString() + ", ");
		sb.append(Data.MAX_PLAYER.key + ":" + maxPlayer.toString() + ", ");
		sb.append(Data.COLOR_INDICE.key + ":" + colorManager.toString());
		
		
		return "Arena:{}";
	}

	/*
	 * * Data Enumeration * *
	 *******************************************************/
	
	public static SavableParameter getIdentifier() {
		return Data.UUID;
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
