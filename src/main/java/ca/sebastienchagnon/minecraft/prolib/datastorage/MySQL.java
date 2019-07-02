package ca.sebastienchagnon.minecraft.prolib.datastorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import ca.sebastienchagnon.minecraft.prolib.Library;
import ca.sebastienchagnon.minecraft.prolib.PConfigKey;
import ca.sebastienchagnon.minecraft.prolib.PPlugin;

/**
 * A MySQL connection to a Database. Used to store data on it and read data from
 * it. Can be accessed locally or through the web with an IP address and a port.
 * The connection informations are stored on a config YAML file.
 * 
 * @author S�bastien Chagnon
 * @see Database
 * @see DataStorage
 * @see Connection
 */
public class MySQL extends Database {

	/**
	 * Parameter constructor
	 * 
	 * @param plugin
	 *            - the main class of the plugin
	 * @param table
	 *            - the name of the table that is going to be used to store data
	 *            on
	 * @see Library
	 */
	public MySQL(PPlugin plugin, String table) {
		super(plugin, table);
	}

	@Override
	public Connection getSQLConnection() {
		String host = plugin.getConfig().getString(PConfigKey.DB_HOST.getKey(), "127.0.0.1");
		int port = plugin.getConfig().getInt(PConfigKey.DB_PORT.getKey(), 3306);
		String user = plugin.getConfig().getString(PConfigKey.DB_USER.getKey(), "root");
		String password = plugin.getConfig().getString(PConfigKey.DB_PASS.getKey());
		String database = plugin.getConfig().getString(PConfigKey.DB_DB.getKey());
		

		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", user, password);
			return conn;
		} catch (SQLException ex) {
			plugin.get().getLogger().log(Level.SEVERE, "MySQL exception on initialize", ex);
		}
		return null;
	}
}