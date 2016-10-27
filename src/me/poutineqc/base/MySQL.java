package me.poutineqc.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.poutineqc.base.PluginConfig.ConfigOptions;

public class MySQL {


	private Connection connection;
	private String table;

	public MySQL(String table) throws ObjectTypeException {
		this.table = table;
		
		if (Plugin.get().getConf().getBoolean(ConfigOptions.MYSQL))
			connect();
	}

	public void connect() {
		try {
			String host = Plugin.get().getConf().getString(ConfigOptions.HOST);
			int port = Plugin.get().getConf().getInt(ConfigOptions.PORT);
			String database = Plugin.get().getConf().getString(ConfigOptions.DATABASE);
			String user = Plugin.get().getConf().getString(ConfigOptions.USER);
			String password = Plugin.get().getConf().getString(ConfigOptions.PASSWORD);
			
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
		} catch (SQLException | ObjectTypeException e) {
			Plugin.get().getLogger().warning("[MySQL] The connection to MySQL couldn't be made! reason: " + e.getMessage());
		}
	}

	public void close() {
		try {
			if (connection != null)
				connection.close();
			
		} catch (SQLException e) {
			Plugin.get().getLogger().warning("[MySQL] The connection couldn't be closed! reason: " + e.getMessage());
		}
	}

	public boolean hasConnection() {
		return connection != null;
	}

	public void update(UUID uuid, String value) {
		
		String qry = "UPDATE `" + table + "` SET `" + "`='" + value + "' WHERE `uuid`='" + uuid.toString() + "'";
		
		try {
			PreparedStatement st = connection.prepareStatement(qry);
			st.execute();
			st.close();
		} catch (SQLException e) {
			connect();
			Plugin.get().getLogger().info(qry);
			Plugin.get().getLogger().warning(e.getMessage());
		}
	}

	public ResultSet query(UUID uuid) {
		
		String qry = "SELECT `" + "` FROM `" + table + "` WHERE `uuid`='" + uuid.toString() + "'";
		
		ResultSet rs = null;
		try {
			PreparedStatement st = connection.prepareStatement(qry);
			rs = st.executeQuery();
		} catch (SQLException e) {
			connect();
			Plugin.get().getLogger().info(qry);
			Plugin.get().getLogger().warning(e.getMessage());
		}
		return rs;
	}
}
