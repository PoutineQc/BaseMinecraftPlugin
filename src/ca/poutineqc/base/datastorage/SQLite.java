package ca.poutineqc.base.datastorage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import ca.poutineqc.base.PConfigKey;
import ca.poutineqc.base.PPlugin;

/**
 * A SQLite connection to a Database. Used to store data on it and read data
 * from it. Can only be accessed locally. All the data is stored on a .sql file.
 * 
 * @author Sebastien Chagnon
 * @see Database
 * @see DataStorage
 * @see Connection
 */
public class SQLite extends Database {

	private File dataFile;

	/**
	 * Parameter constructor
	 * 
	 * @param plugin
	 *            - the main class of the plugin
	 * @param table
	 *            - the name of the table that is going to be used to store data
	 *            on
	 */
	public SQLite(PPlugin plugin, String table) {
		super(plugin, table);

		dataFile = new File(plugin.get().getDataFolder(),
				plugin.getConfig().getString(PConfigKey.DB_DB.getKey(), "minecraft") + ".db");
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				plugin.get().getLogger().log(Level.SEVERE, "File write error: "
						+ plugin.getConfig().getString(PConfigKey.DB_DB.getKey(), "minecraft") + ".db");
			}
		}
	}

	@Override
	public Connection getSQLConnection() {

		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dataFile);
			return connection;
		} catch (SQLException ex) {
			plugin.get().getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
		} catch (ClassNotFoundException ex) {
			plugin.get().getLogger().log(Level.SEVERE,
					"You need the SQLite JBDC library. Google it. Put it in /lib folder.");
		}
		return null;
	}
}