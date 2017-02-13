package me.poutineqc.instantiable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.poutineqc.data.DataStorage;
import me.poutineqc.data.Database;
import me.poutineqc.data.FlatFile;
import me.poutineqc.data.MySQL;
import me.poutineqc.lang.Language;
import me.poutineqc.plugin.PoutinePlugin;

public abstract class BasePlayer implements Savable {

	public static final String TABLE_NAME = "Player";

	private DataStorage data;
	
	//private Map<SavableParameter, ?> parameters = new HashMap<SavableParameter, String>();
	
	protected OfflinePlayer player;
	protected Language language;
	
	
	public BasePlayer(PoutinePlugin plugin, DataStorage data, UUID uuid, boolean hasRow) {
		
		if (data == null)
			data = openDataStorage(plugin);
		
		this.player = Bukkit.getOfflinePlayer(uuid);
		this.language = hasRow ? plugin.getLanguageManager().getLanguage(data.getString(getUUID(), Data.LANGUAGE)) : plugin.getLanguageManager().getDefault();
	}
	
	private enum Data implements SavableParameter {
		UUID("uuid", DataValue.STRING),
		LANGUAGE("language", DataValue.STRING);

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
	
	@Override
	public List<SavableParameter> getParameters() {
		List<SavableParameter> returnValue = new ArrayList<SavableParameter>();
		
		for (Data parameter : Data.values())
			returnValue.add(parameter);
		
		returnValue.addAll(getChildParameters());
		
		return returnValue;
	}
	
	protected abstract List<SavableParameter> getChildParameters();

	@Override
	public DataStorage openDataStorage(PoutinePlugin plugin) {

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

	public Player getPlayer() {
		return player.getPlayer();
	}

	public void sendMessage(String key) {
		language.sendMessage(getPlayer(), key);;
	}
}
