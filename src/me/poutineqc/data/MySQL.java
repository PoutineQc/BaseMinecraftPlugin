package me.poutineqc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import me.poutineqc.plugin.PoutinePlugin;

public class MySQL extends Database {

	public MySQL(PoutinePlugin plugin, String table) {
		super(plugin, table);
	}

	@Override
    public Connection getSQLConnection() {
		String host = plugin.getConfig().getString("host", "127.0.0.1");
		int port = plugin.getConfig().getInt("port", 3306);
		String user = plugin.getConfig().getString("user", "root");
		String password = plugin.getConfig().getString("password");
        
        try {
        	return connection = DriverManager.getConnection(
					"jdbc:mysql://" + host + ":" + port + "/" + plugin.getConfig().getString("database") + "?autoReconnect=true", user, password);
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"MySQL exception on initialize", ex);
        }
        return null;
    }
}