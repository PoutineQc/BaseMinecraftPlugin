package me.poutineqc.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

import me.poutineqc.base.Plugin;

public abstract class Database extends DataStorage {
	protected Connection connection;
	protected String dbname;
	private String table;

	public Database(Plugin plugin, String table) {
		super(plugin);
		dbname = plugin.getConfig().getString("database", "minecraft");
		this.table = table;
	}

	public abstract Connection getSQLConnection();

	public void load(Column primary, List<Column> columns) {
		connection = getSQLConnection();

		try {
			Statement s = connection.createStatement();
			s.executeUpdate(getCreateTableQuerry(primary, columns));
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		initialize();
	}

	public void initialize() {
		connection = getSQLConnection();

		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ResultSet rs = ps.executeQuery();
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();

		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
		}
	}

	public String getCreateTableQuerry(Column primary, List<Column> columns) {
		StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

		builder.append(table);
		builder.append(" (");

		builder.append(primary.getQuerryPart());
		builder.append(" NOT NULL");

		for (Column column : columns) {
			builder.append(",");
			builder.append(column.getQuerryPart());
		}

		builder.append("PRIMARY KEY (`" + primary.getName() + "`)");
		builder.append(");");

		return builder.toString();
	}

	public ResultSet query(String qry) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getSQLConnection();
			ps = connection.prepareStatement(qry);

			rs = ps.executeQuery();

		} catch (SQLException ex) {
			connection = getSQLConnection();
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
			}
		}

		return rs;
	}

	public void update(String qry) {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(qry);
			ps.execute();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
			}
		}
	}

	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
		}
	}

	@Override
	public String getString(String capsule, String key) {
		String value = null;

		try {
			ResultSet rs = query("SELECT `" + key + "` FROM " + table + " WHERE player = '" + capsule + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(capsule)) {
					value = rs.getString(key);
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}

	@Override
	public void setString(String capsule, String key, String value) {
		update("UPDATE " + table + " SET `" + key + "`='" + value + "' WHERE UUID='" + capsule + "';");
	}

	@Override
	public int getInt(String capsule, String key) {
		int value = 0;

		try {
			ResultSet rs = query("SELECT `" + key + "` FROM " + table + " WHERE player = '" + capsule + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(capsule)) {
					value = rs.getInt(key);
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}

	@Override
	public void setInt(String capsule, String key, int value) {
		update("UPDATE " + table + " SET `" + key + "`='" + value + "' WHERE UUID='" + capsule + "';");

	}

	@Override
	public double getDouble(String capsule, String key) {
		double value = 0;

		try {
			ResultSet rs = query("SELECT `" + key + "` FROM " + table + " WHERE player = '" + capsule + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(capsule)) {
					value = rs.getDouble(key);
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}

	@Override
	public void setDouble(String capsule, String key, double value) {
		update("UPDATE " + table + " SET `" + key + "`='" + value + "' WHERE UUID='" + capsule + "';");

	}

	@Override
	public long getLong(String capsule, String key) {
		long value = 0;

		try {
			ResultSet rs = query("SELECT `" + key + "` FROM " + table + " WHERE player = '" + capsule + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(capsule)) {
					value = rs.getLong(key);
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}

	@Override
	public void setLong(String capsule, String key, long value) {
		update("UPDATE " + table + " SET `" + key + "`='" + value + "' WHERE UUID='" + capsule + "';");

	}

	@Override
	public boolean getBoolean(String capsule, String key) {
		boolean value = false;

		try {
			ResultSet rs = query("SELECT `" + key + "` FROM " + table + " WHERE player = '" + capsule + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(capsule)) {
					value = rs.getBoolean(key);
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}

	@Override
	public void setBoolean(String capsule, String key, boolean value) {
		update("UPDATE " + table + " SET `" + key + "`='" + value + "' WHERE UUID='" + capsule + "';");

	}

	@Override
	public float getFloat(String capsule, String key) {
		float value = 0;

		try {
			ResultSet rs = query("SELECT `" + key + "` FROM " + table + " WHERE player = '" + capsule + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(capsule)) {
					value = rs.getFloat(key);
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}

	@Override
	public void setFloat(String capsule, String key, float value) {
		update("UPDATE " + table + " SET `" + key + "`='" + value + "' WHERE UUID='" + capsule + "';");

	}
}
