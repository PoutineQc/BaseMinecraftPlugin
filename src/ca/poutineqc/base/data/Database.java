package ca.poutineqc.base.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.PoutinePlugin;
import ca.poutineqc.base.utils.Pair;

public abstract class Database implements DataStorage {

	/*******************************************************
	 * * Abstract Methods * *
	 *******************************************************/
	public abstract Connection getSQLConnection();

	/*******************************************************
	 * * Fields * *
	 *******************************************************/

	protected PoutinePlugin plugin;
	protected static Connection connection;

	protected String table;

	/*******************************************************
	 * * Constructors * *
	 *******************************************************/

	public Database(PoutinePlugin plugin, String table) {
		this.plugin = plugin;
		this.table = plugin.getConfig().getString("tablePrefix") + table;
	}

	/*******************************************************
	 * * Database handlers * *
	 *******************************************************/

	public void load(SavableParameter identification, List<SavableParameter> parameters) {
		connection = getSQLConnection();
		update(getCreateTableQuery(identification, parameters));

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
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Failed to close SQL connection: ", ex);
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
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Failed to close SQL connection: ", ex);
			}
		}
	}

	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Failed to close SQL connection: ", ex);
		}
	}

	/*******************************************************
	 * * Query builders * *
	 *******************************************************/

	private String getCreateTableQuery(SavableParameter identification, List<SavableParameter> parameters) {
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

	private String getNewInstanceQuery(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> createParameters) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");

		sb.append(table);
		sb.append(" (");

		sb.append(identification.getKey().getKey());

		for (int i = 0; i < createParameters.size(); i++) {
			sb.append(", ");
			sb.append(createParameters.get(i).getKey().getKey());
		}

		sb.append(") VALUES (");

		sb.append(identification.getValue().toString());

		for (int i = 0; i < createParameters.size(); i++) {
			sb.append(',');
			sb.append('\'');
			sb.append(createParameters.get(i).getValue());
			sb.append('\'');
		}

		sb.append(");");

		return sb.toString();
	}

	private String getMultipleUpdateQuery(Pair<SavableParameter, UUID> identification, List<Pair<SavableParameter, String>> entries) {
		StringBuilder sb = new StringBuilder("UPDATE ");

		sb.append(table);
		sb.append(" SET ");

		for (Pair<SavableParameter, String> entry : entries) {
			sb.append(entry.getKey().getKey());
			sb.append("='");
			sb.append(entry.getValue());
			sb.append("'");
			sb.append(", ");
		}

		sb.substring(sb.length() - 2);
		sb.append(" WHERE ");

		sb.append(identification.getKey().getKey());
		sb.append("=");
		sb.append(identification.getValue().toString());
		sb.append(";");

		return sb.toString();
	}

	/*******************************************************
	 * * Data getters and setters * *
	 *******************************************************/

	@Override
	public List<UUID> getAllIdentifications(SavableParameter parameter) {
		List<UUID> identifications = new ArrayList<UUID>();

		String query = "SELECT * FROM " + table;
		ResultSet rs = query(query);

		try {
			while (rs.next()) {
				identifications.add(UUID.fromString(rs.getString(parameter.getKey())));
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Failed to read SQL ResultSet: ", ex);
		}

		return null;
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(Pair<SavableParameter, UUID> identification,
			List<SavableParameter> parameters) {

		String query = "SELECT * FROM " + table + " WHERE " + identification.getKey().getKey() + "='"
				+ identification.getValue().toString() + "';";
		ResultSet rs = query(query);

		try {

			while (rs.next()) {
				Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

				for (SavableParameter parameter : parameters) {

					switch (parameter.getType()) {
					case BOOLEAN:
						user.put(parameter, String.valueOf(rs.getBoolean(parameter.getKey())));
						break;
					case DOUBLE:
						user.put(parameter, String.valueOf(rs.getDouble(parameter.getKey())));
						break;
					case FLOAT:
						user.put(parameter, String.valueOf(rs.getFloat(parameter.getKey())));
						break;
					case INTEGER:
						user.put(parameter, String.valueOf(rs.getInt(parameter.getKey())));
						break;
					case LONG:
						user.put(parameter, String.valueOf(rs.getLong(parameter.getKey())));
						break;
					case STRING:
					default:
						user.put(parameter, rs.getString(parameter.getKey()));
						break;

					}
				}

				return user;

			}

		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Failed to read SQL ResultSet: ", ex);
		}

		return null;

	}

	@Override
	public void newInstance(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> createParameters) {
		update(getNewInstanceQuery(identification, createParameters));
	}

	public void setValues(Pair<SavableParameter, UUID> identification, List<Pair<SavableParameter, String>> entries) {
		update(getMultipleUpdateQuery(identification, entries));
	}
	

	// @Override
	// public String getString(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) {
	// return getValue(identification, parameter);
	// }

	@Override
	public void setString(Pair<SavableParameter, UUID> identification, SavableParameter parameter, String value) {
		setValue(identification, parameter, value);
	}

	// @Override
	// public int getInt(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) {
	// return getValue(identification, parameter);
	// }

	@Override
	public void setInt(Pair<SavableParameter, UUID> identification, SavableParameter parameter, int value) {
		setValue(identification, parameter, value);
	}

	// @Override
	// public double getDouble(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) {
	// return getValue(identification, parameter);
	// }

	@Override
	public void setDouble(Pair<SavableParameter, UUID> identification, SavableParameter parameter, double value) {
		setValue(identification, parameter, value);
	}

	// @Override
	// public long getLong(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) {
	// return getValue(identification, parameter);
	// }

	@Override
	public void setLong(Pair<SavableParameter, UUID> identification, SavableParameter parameter, long value) {
		setValue(identification, parameter, value);
	}

	// @Override
	// public boolean getBoolean(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) {
	// return getValue(identification, parameter);
	// }

	@Override
	public void setBoolean(Pair<SavableParameter, UUID> identification, SavableParameter parameter, boolean value) {
		setValue(identification, parameter, value);
	}

	// @Override
	// public float getFloat(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) {
	// return getValue(identification, parameter);
	// }

	@Override
	public void setFloat(Pair<SavableParameter, UUID> identification, SavableParameter parameter, float value) {
		setValue(identification, parameter, value);
	}

	// public <T> T getValue(Pair<SavableParameter, UUID> identification,
	// SavableParameter parameter) throws ClassCastException {
	// T value = null;
	//
	// try {
	// ResultSet rs = query("SELECT `" + parameter.getKey() + "` FROM " + table
	// + " WHERE player = '" + identification.toString() + "';");
	// while (rs.next()) {
	// if (rs.getString("uuid").equalsIgnoreCase(identification.toString())) {
	// value = (T) rs.getObject(parameter.getKey());
	// }
	// }
	// } catch (SQLException ex) {
	// plugin.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ",
	// ex);
	// }
	//
	// return value;
	// }

	private <T> void setValue(Pair<SavableParameter, UUID> identification, SavableParameter parameter, T value) {
		update("UPDATE " + table + " SET `" + parameter.getKey() + "`='" + value.toString() + "' WHERE UUID='"
				+ identification.toString() + "';");
	}

}
