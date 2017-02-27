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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import ca.poutineqc.base.data.values.PUUID;
import ca.poutineqc.base.data.values.SValue;
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

	private static final AtomicBoolean CONNECTED = new AtomicBoolean();

	protected static Connection connection;

	protected PPlugin plugin;

	private String tableName;

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
	public Database(PPlugin plugin, String tableName) {

		this.plugin = plugin;
		this.tableName = plugin.getConfig().getString("tablePrefix") + tableName;

		if (!CONNECTED.getAndSet(true))
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

	private String getCreateTableQuery(SavableParameter identification, PUUID uuid,
			List<Pair<SavableParameter, SValue>> parameters) {
		StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

		builder.append(tableName);
		builder.append(" (");

		builder.append(identification.getKey() + " VARCHAR(" + uuid.getMaxToStringLength() + ") NOT NULL,");

		for (Pair<SavableParameter, SValue> parameter : parameters) {
			builder.append(parameter.getKey().getKey() + " VARCHAR(" + parameter.getValue().getMaxToStringLength()
					+ ") DEFAULT '" + parameter.getKey().getDefaultValue() + "'");
			builder.append(", ");
		}

		builder.append("PRIMARY KEY (`" + identification.getKey() + "`)");
		builder.append(")");

		return builder.toString();
	}

	private String getNewInstanceQuery(SavableParameter identification, PUUID uuid,
			List<Pair<SavableParameter, SValue>> createParameters) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");

		sb.append(tableName);
		sb.append(" (");

		sb.append(identification.getKey());

		for (int i = 0; i < createParameters.size(); i++) {
			sb.append(", ");
			sb.append(createParameters.get(i).getKey().getKey());
		}

		sb.append(") VALUES (");

		sb.append(uuid.toSString());

		for (int i = 0; i < createParameters.size(); i++) {
			sb.append(',');
			sb.append('\'');
			sb.append(createParameters.get(i).getValue().toSString());
			sb.append('\'');
		}

		sb.append(");");

		return sb.toString();
	}

	private String getMultipleUpdateQuery(SavableParameter identification, PUUID uuid,
			List<Pair<SavableParameter, String>> entries) {
		StringBuilder sb = new StringBuilder("UPDATE ");

		sb.append(tableName);
		sb.append(" SET ");

		for (Pair<SavableParameter, String> entry : entries) {
			sb.append(entry.getKey().getKey());
			sb.append("='");
			sb.append(entry.getValue());
			sb.append("'");
			sb.append(", ");
		}

		sb.replace(sb.length() - 2, sb.length() - 1, "");
		sb.append(" WHERE ");

		sb.append(identification.getKey());
		sb.append("=");
		sb.append(uuid.toSString());
		sb.append(";");

		return sb.toString();
	}

	// =========================================================================
	// Database Accessors
	// =========================================================================

	@Override
	public void newInstance(SavableParameter identification, PUUID uuid,
			List<Pair<SavableParameter, SValue>> createParameters) {
		update(getCreateTableQuery(identification, uuid, createParameters));
		update(getNewInstanceQuery(identification, uuid, createParameters));
	}

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification) {
		List<UUID> identifications = new ArrayList<UUID>();

		String query = "SELECT * FROM " + tableName;
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
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, PUUID id,
			Collection<SavableParameter> parameters) {

		String query = "SELECT * FROM " + tableName + " WHERE " + identification.getKey() + "='" + id.toSString()
				+ "';";
		ResultSet rs = query(query);

		try {

			while (rs.next()) {
				Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

				for (SavableParameter parameter : parameters)
					user.put(parameter, rs.getString(parameter.getKey()));

				return user;

			}

		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Failed to read SQL ResultSet: ", ex);
		}

		return null;

	}

	@Override
	public void setValues(SavableParameter identification, PUUID uuid, List<Pair<SavableParameter, String>> entries)
			throws InvalidParameterException {
		update(getMultipleUpdateQuery(identification, uuid, entries));
	}

	@Override
	public void setString(SavableParameter identification, PUUID uuid, SavableParameter parameter, String value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setInt(SavableParameter identification, PUUID uuid, SavableParameter parameter, int value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setDouble(SavableParameter identification, PUUID uuid, SavableParameter parameter, double value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setLong(SavableParameter identification, PUUID uuid, SavableParameter parameter, long value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setBoolean(SavableParameter identification, PUUID uuid, SavableParameter parameter, boolean value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setFloat(SavableParameter identification, PUUID uuid, SavableParameter parameter, float value) {
		setValue(identification, uuid, parameter, value);
	}

	private <T> void setValue(SavableParameter identification, PUUID uuid, SavableParameter parameter, T value) {
		update("UPDATE " + tableName + " SET `" + parameter.getKey() + "`='" + value.toString() + "' WHERE "
				+ identification.getKey() + "='" + uuid.toSString() + "';");
	}

	@Override
	public void set(SavableParameter identifier, PUUID uuid, SavableParameter parameter, SValue value) {
		update("UPDATE " + tableName + " SET `" + parameter.getKey() + "`='" + value.toSString() + "' WHERE "
				+ identifier.getKey() + "='" + uuid.toSString() + "';");
	}

}
