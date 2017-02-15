package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.FlatFile;
import ca.poutineqc.base.data.MySQL;
import ca.poutineqc.base.data.SQLite;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Pair;

public abstract class PPlayer implements Savable {

	// =========================================================================
	// Static Fields
	// =========================================================================

	public static final String TABLE_NAME = "Player";

	private static DataStorage data;

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

			data = new MySQL(plugin, TABLE_NAME);

			break;
		case "flatfiles":

			data = new FlatFile(plugin, TABLE_NAME.toLowerCase());

			break;
		case "sqlite":
		default:

			data = new SQLite(plugin, TABLE_NAME);

			break;
		}

		return data;
	}

	// =========================================================================
	// Abstract Methods
	// =========================================================================

	/**
	 * Returns a Collection of all the SavableParameters that are used by a
	 * child class of BasePlayer.
	 * 
	 * @return a Collection of SavableParameters
	 */
	public abstract Collection<SavableParameter> getParameters();

	// =========================================================================
	// Fields
	// =========================================================================

	private Pair<SavableParameter, UUID> uuid;

	private OfflinePlayer player;
	private Language language;

	// =========================================================================
	// Constructors
	// =========================================================================

	/**
	 * Parameter Constructor used when the player is already stored in the
	 * DataStorage.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @param uuid
	 *            the UUID of the player
	 * @see PPlugin
	 * @see UUID
	 */
	public PPlayer(PPlugin plugin, UUID uuid) {

		checkDataStorage(plugin);
		data.createTableIfNotExists(Data.UUID, getBaseParameters());

		this.uuid = new Pair<SavableParameter, UUID>(Data.UUID, uuid);
		this.player = Bukkit.getOfflinePlayer(uuid);

		try {
			Map<SavableParameter, String> parameters = data.getIndividualData(this.uuid, getBaseParameters());

			this.language = plugin.getLanguageManager().getLanguage(parameters.get(Data.LANGUAGE));

		} catch (NumberFormatException ex) {
			this.language = plugin.getLanguageManager().getDefault();
			plugin.getLogger().severe("Could not load player " + uuid.toString() + " : " + ex);
		}
	}

	/**
	 * Parameter Constructor used when the player not yet stored in the
	 * DataStorage.
	 * 
	 * @param plugin
	 *          the main class of the plugin
	 * @param player
	 *            the player which this instance is created for.
	 * @see PPlugin
	 * @see Player
	 */
	public PPlayer(PPlugin plugin, Player player) {

		checkDataStorage(plugin);
		data.createTableIfNotExists(Data.UUID, getBaseParameters());

		this.uuid = new Pair<SavableParameter, UUID>(Data.UUID, player.getUniqueId());
		this.player = player;
		this.language = plugin.getLanguageManager().getDefault();

		data.newInstance(uuid, new ArrayList<Pair<SavableParameter, String>>());
	}

	// =========================================================================
	// Accessors
	// =========================================================================

	@Override
	public UUID getUUID() {
		return uuid.getValue();
	}

	@Override
	public String getName() {
		return getPlayer().getDisplayName();
	}

	public Language getLanguage() {
		return language;
	}

	/**
	 * Returns the Player managed by this instance
	 * 
	 * @return the Player managed by this instance
	 * @see Player
	 */
	public Player getPlayer() {
		return player.getPlayer();
	}

	/*
	 * * Custom Methods * *
	 *******************************************************/

	/**
	 * Sends a Message to a player. The message is translated based on the
	 * player's choice.
	 * 
	 * @param message
	 *            the Message to send
	 */
	public void sendMessage(Message message) {
		language.sendMessage(getPlayer(), message);
	}

	/*
	 * * Data Enumeration * *
	 *******************************************************/

	@Override
	public List<SavableParameter> getBaseParameters() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();

		for (Data parameter : Data.values())
			returnValue.add(parameter);

		returnValue.addAll(getParameters());

		return returnValue;
	}

	/**
	 * Represents all the Parameters from a BasePlayer that may be saved in a
	 * DataStorage.
	 * 
	 * @author Sébastien Chagnon
	 * @see SavableParameter
	 */
	private enum Data implements SavableParameter {
		UUID("uuid", DataValue.STRING), LANGUAGE("language", DataValue.STRING);

		private String key;
		private DataValue dataValue;
		private String defaultValue;

		private Data(String dataName, DataValue dataValue, Object defaultValue) {
			this.key = dataName;
			this.dataValue = dataValue;
			this.defaultValue = String.valueOf(defaultValue);
		}

		private Data(String dataName, DataValue dataValue) {
			this.key = dataName;
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

	public void setLanguage(Language language) {
		this.language = language;
		data.setString(uuid, Data.LANGUAGE, language.getLanguageKey());
	}
}
