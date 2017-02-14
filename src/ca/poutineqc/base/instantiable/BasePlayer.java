package ca.poutineqc.base.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.Database;
import ca.poutineqc.base.data.FlatFile;
import ca.poutineqc.base.data.MySQL;
import ca.poutineqc.base.data.SQLite;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.plugin.PoutinePlugin;
import ca.poutineqc.base.utils.Pair;

public abstract class BasePlayer implements Savable {


	/*******************************************************
	 * * Static Fields * *
	 *******************************************************/
	
	public static final String TABLE_NAME = "Player";
	
	private static DataStorage data;

	
	/*******************************************************
	 * * Static Methods * *
	 *******************************************************/

	private static void checkDataStorage(PoutinePlugin plugin) {
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

			Database sqlite = new SQLite(plugin, getTableName());
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

	private OfflinePlayer player;
	private Language language;


	/*******************************************************
	 * * Constuctors * *
	 *******************************************************/
	

	public BasePlayer(PoutinePlugin plugin, UUID uuid) {

		checkDataStorage(plugin);

		this.uuid = new Pair<SavableParameter, UUID>(Data.UUID, uuid);
		this.player = Bukkit.getOfflinePlayer(uuid);

		try {
			Map<SavableParameter, String> parameters = data.getIndividualData(this.uuid, getParameters());

			this.language = plugin.getLanguageManager().getLanguage(parameters.get(Data.LANGUAGE));

		} catch (NumberFormatException ex) {
			this.language = plugin.getLanguageManager().getDefault();
			plugin.getLogger().severe("Could not load player " + uuid.toString() + " : " + ex);
		}
	}

	public BasePlayer(PoutinePlugin plugin, Player player) {

		checkDataStorage(plugin);

		this.uuid = new Pair<SavableParameter, UUID>(Data.UUID, player.getUniqueId());
		this.player = player;
		this.language = plugin.getLanguageManager().getDefault();

		data.newInstance(uuid, new ArrayList<Pair<SavableParameter, String>>());
	}


	/*******************************************************
	 * * Getters and Setters * *
	 *******************************************************/

	public UUID getUUID() {
		return uuid.getValue();
	}
	
	public String getName() {
		return getPlayer().getDisplayName();
	}
	
	public static String getTableName() {
		return TABLE_NAME;
	}

	public Player getPlayer() {
		return player.getPlayer();
	}
	

	/*******************************************************
	 * * Custom Methods * *
	 *******************************************************/

	public void sendMessage(String key) {
		language.sendMessage(getPlayer(), key);
	}
	
	
	/*******************************************************
	 * * Data Enumeration * *
	 *******************************************************/
	
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
}
