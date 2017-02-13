package me.poutineqc.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import me.poutineqc.instantiable.SavableParameter;
import me.poutineqc.plugin.PoutinePlugin;

public abstract class Database implements DataStorage {
	
	protected PoutinePlugin plugin;
	protected static Connection connection;
	
	protected String table;

	public Database(PoutinePlugin plugin, String table) {
		this.plugin = plugin;
		this.table = plugin.getConfig().getString("tablePrefix") + table;
	}

	public abstract Connection getSQLConnection();

	public void load(SavableParameter identification, List<SavableParameter> parameters) {
		connection = getSQLConnection();

		try {
			Statement s = connection.createStatement();
			s.executeUpdate(getCreateTableQuerry(identification, parameters));
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

	public String getCreateTableQuerry(SavableParameter identification, List<SavableParameter> parameters) {
		StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

		builder.append(table);
		builder.append(" (");

		for (SavableParameter parameter : parameters) {
			builder.append(",");
			builder.append(parameter.getCreateQuerryPart());
		}

		builder.append("PRIMARY KEY (`" + identification.getKey() + "`)");
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
	public String getString(UUID identification, SavableParameter parameter) {
		return getValue(identification, parameter);
	}

	@Override
	public void setString(UUID identification, SavableParameter parameter, String value) {
		setValue(identification, parameter, value);
	}

	@Override
	public int getInt(UUID identification, SavableParameter parameter) {
		return getValue(identification, parameter);
	}

	@Override
	public void setInt(UUID identification, SavableParameter parameter, int value) {
		setValue(identification, parameter, value);
	}

	@Override
	public double getDouble(UUID identification, SavableParameter parameter) {
		return getValue(identification, parameter);
	}

	@Override
	public void setDouble(UUID identification, SavableParameter parameter, double value) {
		setValue(identification, parameter, value);
	}

	@Override
	public long getLong(UUID identification, SavableParameter parameter) {
		return getValue(identification, parameter);
	}

	@Override
	public void setLong(UUID identification, SavableParameter parameter, long value) {
		setValue(identification, parameter, value);
	}

	@Override
	public boolean getBoolean(UUID identification, SavableParameter parameter) {
		return getValue(identification, parameter);
	}

	@Override
	public void setBoolean(UUID identification, SavableParameter parameter, boolean value) {
		setValue(identification, parameter, value);
	}

	@Override
	public float getFloat(UUID identification, SavableParameter parameter) {
		return getValue(identification, parameter);
	}

	@Override
	public void setFloat(UUID identification, SavableParameter parameter, float value) {
		setValue(identification, parameter, value);
	}
	
	public <T> T getValue(UUID identification, SavableParameter parameter) throws ClassCastException {
		T value = null;

		try {
			ResultSet rs = query("SELECT `" + parameter.getKey() + "` FROM " + table + " WHERE player = '" + identification.toString() + "';");
			while (rs.next()) {
				if (rs.getString("uuid").equalsIgnoreCase(identification.toString())) {
					value = (T) rs.getObject(parameter.getKey());
				}
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
		}

		return value;
	}
	
	public <T> void setValue(UUID identification, SavableParameter parameter, T value) {
		update("UPDATE " + table + " SET `" + parameter.getKey() + "`='" + value.toString() + "' WHERE UUID='" + identification.toString() + "';");
	}
	
}
