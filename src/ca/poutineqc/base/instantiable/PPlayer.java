package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.JSON;
import ca.poutineqc.base.data.YAML;
import ca.poutineqc.base.data.MySQL;
import ca.poutineqc.base.data.SQLite;
import ca.poutineqc.base.data.values.SUUID;
import ca.poutineqc.base.data.values.UniversalSavableValue;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.utils.Pair;

public class PPlayer implements Savable {

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
	 * @see Library
	 */
	public static void checkDataStorage(Library plugin) {
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
	public static DataStorage openDataStorage(Library plugin) {

		switch (plugin.getConfig().getString("dataStorage").toLowerCase()) {
		case "mysql":

			data = new MySQL(plugin, TABLE_NAME);

			break;
		case "json":

			data = new JSON(plugin, TABLE_NAME.toLowerCase(), false);

			break;
		case "yaml":

			data = new YAML(plugin, TABLE_NAME.toLowerCase(), false);

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

	private final SUUID uuid;

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
	 * @see Library
	 * @see UUID
	 */
	public PPlayer(Library plugin, UUID uuid) {

		checkDataStorage(plugin);

		this.uuid = new SUUID(uuid);
		this.player = Bukkit.getOfflinePlayer(uuid);

		Map<SavableParameter, String> parameters = data.getIndividualData(Data.UUID, this.uuid, getParameters());

		this.language = plugin.getLanguageManager().getLanguage(Language.getKey(parameters.get(Data.LANGUAGE)));

	}

	/**
	 * Parameter Constructor used when the player not yet stored in the
	 * DataStorage.
	 * 
	 * @param plugin
	 *            the main class of the plugin
	 * @param player
	 *            the player which this instance is created for.
	 * @see Library
	 * @see Player
	 */
	public PPlayer(Library plugin, Player player) {

		checkDataStorage(plugin);

		this.uuid = new SUUID(player.getUniqueId());
		this.player = player;
		this.language = plugin.getLanguageManager().getDefault();

		List<Pair<SavableParameter, UniversalSavableValue>> toSave = new ArrayList<Pair<SavableParameter, UniversalSavableValue>>();
		toSave.add(new Pair<SavableParameter, UniversalSavableValue>(Data.UUID, this.uuid));
		toSave.add(new Pair<SavableParameter, UniversalSavableValue>(Data.LANGUAGE, this.language));

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
		UUID("uuid", "511837cf-ff1b-41ff-a64a-87ab1aba419c"),

		LANGUAGE("language", "··············en");

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

		data.setStringSavableValue(Data.UUID, uuid, Data.LANGUAGE, language);
	}
}
