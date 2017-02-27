package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
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
import ca.poutineqc.base.data.values.PUUID;
import ca.poutineqc.base.data.values.SValue;
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
	// Fields
	// =========================================================================

	private final PUUID uuid;

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

		this.uuid = new PUUID(uuid);
		this.player = Bukkit.getOfflinePlayer(uuid);

		Map<SavableParameter, String> parameters = data.getIndividualData(Data.UUID, this.uuid, getParameters());
		
		this.language = plugin.getLanguageManager().getLanguage(parameters.get(Data.LANGUAGE));

	}

	/**
	 * Parameter Constructor used when the player not yet stored in the
	 * DataStorage.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @param player
	 *            the player which this instance is created for.
	 * @see PPlugin
	 * @see Player
	 */
	public PPlayer(PPlugin plugin, Player player) {

		checkDataStorage(plugin);

		this.uuid = new PUUID(player.getUniqueId());
		this.player = player;
		this.language = plugin.getLanguageManager().getDefault();

		List<Pair<SavableParameter, SValue>> toSave = new ArrayList<Pair<SavableParameter, SValue>>();
		toSave.add(new Pair<SavableParameter, SValue>(Data.UUID, this.uuid));
		toSave.add(new Pair<SavableParameter, SValue>(Data.LANGUAGE, this.language));
		
		data.newInstance(Data.UUID, uuid, toSave);
	}

	// =========================================================================
	// Accessors
	// =========================================================================

	@Override
	public UUID getUUID() {
		return uuid.getUUID();
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
	public List<SavableParameter> getParameters() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();

		for (Data parameter : Data.values())
			returnValue.add(parameter);

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
		UUID("uuid", ""),

		LANGUAGE("language", "");

		private String key;
		private String defaultValue;

		private Data(String dataName, String defaultValue) {
			this.key = dataName;
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

	public void setLanguage(Language language) {
		this.language = language;
		
		data.set(Data.UUID, uuid, Data.LANGUAGE, language);
	}
}
