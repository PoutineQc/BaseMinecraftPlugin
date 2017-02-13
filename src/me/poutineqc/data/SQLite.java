package me.poutineqc.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import me.poutineqc.plugin.PoutinePlugin;

public class SQLite extends Database {
	public SQLite(PoutinePlugin plugin, String table) {
		super(plugin, table);
	}

	public Connection getSQLConnection() {
		File dataFolder = new File(plugin.getDataFolder(), plugin.getConfig().getString("database") + ".db");
		if (!dataFolder.exists()) {
			try {
				dataFolder.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE,
						"File write error: " + plugin.getConfig().getString("database") + ".db");
			}
		}

		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}

			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
			return connection;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
		} catch (ClassNotFoundException ex) {
			plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
		}
		return null;
	}
}