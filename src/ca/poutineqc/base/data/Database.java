package ca.poutineqc.base.data;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Pair;

/**
 * Represents a SQL Database. Can load and maintain a connection, sent queries
 * and receive ResultSets.
 * 
 * @author Sébastien Chagnon
 */
public abstract class Database implements DataStorage {

	// =========================================================================
	// Abstract Methods
	// =========================================================================

	/**
	 * Creates a SQL connection to a Database which has it's login and access
	 * informations in a config.yml file.
	 * 
	 * @return a opened SQL connection
	 */
	public abstract Connection getSQLConnection();

	// =========================================================================
	// Fields
	// =========================================================================

	protected PPlugin plugin;
	protected static Connection connection;

	private String table;

	// =========================================================================
	// Constructor(s)
	// =========================================================================

	/**
	 * Parameter Constructor. Creates the connection to the Database.
	 * 
	 * @param plugin
	 *            - the main class of the plugin
	 * @param table
	 *            - the name of the table handled by this Database
	 * @see PPlugin
	 */
	public Database(PPlugin plugin, String table) {
		this.plugin = plugin;
		this.table = plugin.getConfig().getString("tablePrefix") + table;

		if (connection == null)
			connection = getSQLConnection();
	}

	// =========================================================================
	// Database Handlers
	// =========================================================================

	/**
	 * To receive data from the Database. The request is specified in the query
	 * and the result is returned as a ResultSet.
	 * 
	 * @param qry
	 *            - the query that will be send to the Database
	 * @return - The ResultSet received from the database as a result from the
	 *         query
	 * @see ResultSet
	 */
	public ResultSet query(String qry) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
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
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Failed to close SQL connection: ", ex);
			}
		}

		return rs;
	}

	/**
	 * To update the Database. The query parameter specifies what values must be
	 * modified.
	 * 
	 * @param qry
	 *            - the query that will be send to the Database
	 */
	public void update(String qry) {
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(qry);
			ps.execute();
		} catch (SQLException ex) {
			connection = getSQLConnection();
			plugin.getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Failed to close SQL connection: ", ex);
			}
		}
	}

	/**
	 * Closes the connection to the Database. Should be used at the end of a
	 * program.
	 */
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Failed to close SQL connection: ", ex);
		}
	}

	// =========================================================================
	// Query Builders
	// =========================================================================

	private String getCreateTableQuery(SavableParameter identification, Collection<SavableParameter> parameters) {
		StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

		builder.append(table);
		builder.append(" (");

		for (SavableParameter parameter : parameters) {
			builder.append(parameter.getCreateQuerryPart());
			builder.append(", ");
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

	private String getMultipleUpdateQuery(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> entries) {
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

	// =========================================================================
	// Database Accessors
	// =========================================================================

	@Override
	public void createTableIfNotExists(SavableParameter identification, Collection<SavableParameter> parameters) {
		update(getCreateTableQuery(identification, parameters));
	}

	@Override
	public void newInstance(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> createParameters) {
		update(getNewInstanceQuery(identification, createParameters));
	}

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification) {
		List<UUID> identifications = new ArrayList<UUID>();

		String query = "SELECT * FROM " + table;
		ResultSet rs = query(query);

		try {
			while (rs.next()) {
				identifications.add(UUID.fromString(rs.getString(identification.getKey())));
			}
		} catch (SQLException ex) {
			return null;
		}

		return identifications;
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(Pair<SavableParameter, UUID> identification,
			Collection<SavableParameter> parameters) {

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
	public void setValues(Pair<SavableParameter, UUID> identification, List<Pair<SavableParameter, String>> entries)
			throws InvalidParameterException {
		update(getMultipleUpdateQuery(identification, entries));
	}

	@Override
	public void setString(Pair<SavableParameter, UUID> identification, SavableParameter parameter, String value) {
		setValue(identification, parameter, value);
	}

	@Override
	public void setInt(Pair<SavableParameter, UUID> identification, SavableParameter parameter, int value) {
		setValue(identification, parameter, value);
	}

	@Override
	public void setDouble(Pair<SavableParameter, UUID> identification, SavableParameter parameter, double value) {
		setValue(identification, parameter, value);
	}

	@Override
	public void setLong(Pair<SavableParameter, UUID> identification, SavableParameter parameter, long value) {
		setValue(identification, parameter, value);
	}

	@Override
	public void setBoolean(Pair<SavableParameter, UUID> identification, SavableParameter parameter, boolean value) {
		setValue(identification, parameter, value);
	}

	@Override
	public void setFloat(Pair<SavableParameter, UUID> identification, SavableParameter parameter, float value) {
		setValue(identification, parameter, value);
	}

	private <T> void setValue(Pair<SavableParameter, UUID> identification, SavableParameter parameter, T value) {
		update("UPDATE " + table + " SET `" + parameter.getKey() + "`='" + value.toString() + "' WHERE UUID='"
				+ identification.toString() + "';");
	}

}
