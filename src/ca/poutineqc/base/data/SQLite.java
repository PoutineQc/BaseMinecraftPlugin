package ca.poutineqc.base.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import ca.poutineqc.base.plugin.Library;

/**
 * A SQLite connection to a Database. Used to store data on it and read data
 * from it. Can only be accessed locally. All the data is stored on a .sql file.
 * 
 * @author Sébastien Chagnon
 * @see Database
 * @see DataStorage
 * @see Connection
 */
public class SQLite extends Database {

	/**
	 * Parameter constructor
	 * 
	 * @param plugin
	 *            - the main class of the plugin
	 * @param table
	 *            - the name of the table that is going to be used to store data
	 *            on
	 */
	public SQLite(Library plugin, String table) {
		super(plugin, table);
	}

	@Override
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