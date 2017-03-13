package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ca.poutineqc.base.commands.inventories.PInventory;
import ca.poutineqc.base.datastorage.DataStorage;
import ca.poutineqc.base.datastorage.StringSerializable;
import ca.poutineqc.base.datastorage.serializable.SUUID;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Pair;

public final class PPlayer implements Savable {

	// =========================================================================
	// Static Fields
	// =========================================================================

	public static final String TABLE_NAME = "Player";

	private DataStorage data;

	// =========================================================================
	// Fields
	// =========================================================================

	private final SUUID uuid;

	private OfflinePlayer player;
	private Language language;
	private PInventory openedInventory;

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
	public PPlayer(PPlugin plugin, DataStorage data, UUID uuid) {

		this.data = data;

		this.uuid = new SUUID(uuid);
		this.player = Bukkit.getOfflinePlayer(uuid);

		Map<SavableParameter, String> parameters = data.getIndividualData(Data.UUID, this.uuid, Data.values());

		this.language = plugin.getLanguages().getLanguage(parameters.get(Data.LANGUAGE));

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
	public PPlayer(PPlugin plugin, DataStorage data, Player player) {

		this.data = data;

		this.uuid = new SUUID(player.getUniqueId());
		this.player = player;
		this.language = plugin.getLanguages().getDefault();

		List<Pair<SavableParameter, StringSerializable>> toSave = new ArrayList<Pair<SavableParameter, StringSerializable>>();
		toSave.add(new Pair<SavableParameter, StringSerializable>(Data.LANGUAGE, this.language));

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

	@Override
	public String toString() {
		return "PPlayer:{uuid:" + uuid + ",language:" + language + "}";
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
	public void sendMessage(PPlugin plugin, Message message) {
		language.sendMessage(plugin, getPlayer(), message);
	}

	/*
	 * * Data Enumeration * *
	 *******************************************************/

	public static SavableParameter getIdentifications() {
		return Data.UUID;
	}

	public static List<SavableParameter> getColumns() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();

		for (Data parameter : Data.values())
			returnValue.add(parameter);

		return returnValue;
	}

	/**
	 * Represents all the Parameters from a BasePlayer that may be saved in a
	 * DataStorage.
	 * 
	 * @author Sօbastien Chagnon
	 * @see SavableParameter
	 */
	private enum Data implements SavableParameter {
		UUID("uuid", "00000000-0000-0000-0000-000000000000"),

		LANGUAGE("language", "օօօօօօօօօօօօօօen");

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

	public void setCurrentInventory(PInventory pInventory) {
		openedInventory = pInventory;
	}

	public PInventory getCurrentInventory() {
		return openedInventory;
	}
}
