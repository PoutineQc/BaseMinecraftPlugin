package ca.poutineqc.base.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import ca.poutineqc.base.plugin.PPlugin;

/**
 * A MySQL connection to a Database. Used to store data on it and read data from
 * it. Can be accessed locally or through the web with an IP address and a port.
 * The connection informations are stored on a config YAML file.
 * 
 * @author Sébastien Chagnon
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
	 * @see PPlugin
	 */
	public MySQL(PPlugin plugin, String table) {
		super(plugin, table);
	}

	@Override
	public Connection getSQLConnection() {
		String host = plugin.getConfig().getString("host", "127.0.0.1");
		int port = plugin.getConfig().getInt("port", 3306);
		String user = plugin.getConfig().getString("user", "root");
		String password = plugin.getConfig().getString("password");

		try {
			return connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/"
					+ plugin.getConfig().getString("database") + "?autoReconnect=true", user, password);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "MySQL exception on initialize", ex);
		}
		return null;
	}
}