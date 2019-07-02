package ca.poutineqc.base.datastorage;

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

import ca.poutineqc.base.Library;
import ca.poutineqc.base.PConfigKey;
import ca.poutineqc.base.PPlugin;
import ca.poutineqc.base.datastorage.serializable.SUUID;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.utils.Pair;

/**
 * Represents a SQL Database. Can load and maintain a connection, sent queries
 * and receive ResultSets.
 * 
 * @author Sebastien Chagnon
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
	 * @see Library
	 */
	public Database(PPlugin plugin, String tableName) {

		this.plugin = plugin;
		this.tableName = plugin.getConfig().getString(PConfigKey.DB_TABLE_PREFIX.getKey(),
				plugin.getDescription().getName() + "_") + tableName;

	}

	// =========================================================================
	// Database Handlers
	// =========================================================================

	/**
	 * To update the Database. The query parameter specifies what values must be
	 * modified.
	 * 
	 * @param qry
	 *            - the query that will be send to the Database
	 */
	public void update(final String qry, boolean wait) {

		if (wait) {
			try (Connection connection = getSQLConnection(); PreparedStatement ps = connection.prepareStatement(qry)) {
				ps.execute();
			} catch (SQLException ex) {
				plugin.get().getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", ex);
			}

		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try (Connection connection = getSQLConnection();
							PreparedStatement ps = connection.prepareStatement(qry)) {
						ps.execute();
					} catch (SQLException ex) {
						plugin.get().getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", ex);
					}
				}
			}).start();
		}
	}

	// =========================================================================
	// Query Builders
	// =========================================================================

	private String getCreateTableQuery(List<SavableParameter> createParameters) {
		StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

		builder.append(tableName);
		builder.append(" (");

		for (SavableParameter parameter : createParameters) {
			builder.append(parameter.getKey() + " VARCHAR(" + parameter.getDefaultValue().length() + ") DEFAULT '"
					+ parameter.getDefaultValue() + "'");
			builder.append(", ");
		}

		return builder.toString().substring(0, builder.length() - 2) + ")";
	}

	private String getNewInstanceQuery(SavableParameter identification, SUUID uuid,
			List<Pair<SavableParameter, StringSerializable>> createParameters) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");

		sb.append(tableName);
		sb.append(" (");

		sb.append(identification.getKey());

		for (int i = 0; i < createParameters.size(); i++) {
			sb.append(", ");
			sb.append(createParameters.get(i).getKey().getKey());
		}

		sb.append(") VALUES ('");

		sb.append(uuid.toSString());
		sb.append("'");

		for (int i = 0; i < createParameters.size(); i++) {
			sb.append(',');
			sb.append("'");
			sb.append(createParameters.get(i).getValue().toSString());
			sb.append("'");
		}

		sb.append(");");

		return sb.toString();
	}

	private String getDeleteInstnceQuery(SavableParameter identification, SUUID uuid) {
		StringBuilder sb = new StringBuilder("DELETE FROM ");

		sb.append(tableName);
		sb.append(" WHERE ");
		sb.append(identification.getKey());
		sb.append("='");
		sb.append(uuid.toSString());
		sb.append("';");

		return sb.toString();
	}

	// =========================================================================
	// Database Accessors
	// =========================================================================

	@Override
	public void newInstance(SavableParameter identification, SUUID uuid,
			List<Pair<SavableParameter, StringSerializable>> createParameters) {
		update(getNewInstanceQuery(identification, uuid, createParameters), false);
	}

	@Override
	public void deleteInstance(SavableParameter identification, SUUID uuid) {
		update(getDeleteInstnceQuery(identification, uuid), false);
	}

	private void createTable(List<SavableParameter> createParameters) {
		update(getCreateTableQuery(createParameters), true);
	}

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification, List<SavableParameter> columns) {

		createTable(columns);

		List<UUID> identifications = new ArrayList<UUID>();

		String query = "SELECT * FROM " + tableName;

		try (Connection connection = getSQLConnection();
				PreparedStatement ps = connection.prepareStatement(query);
				ResultSet resultSet = ps.executeQuery()) {

			while (resultSet.next()) {
				identifications.add(new SUUID(resultSet.getString(identification.getKey())).getUUID());
			}

		} catch (SQLException ex) {
			plugin.get().getLogger().log(Level.SEVERE, "Couldn't execute SQL statement: ", ex);
		}

		return identifications;
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, SUUID id,
			List<SavableParameter> columns) {

		String query = "SELECT * FROM " + tableName + " WHERE " + identification.getKey() + "='" + id.toSString()
				+ "';";

		try (Connection connection = getSQLConnection();
				PreparedStatement ps = connection.prepareStatement(query);
				ResultSet resultSet = ps.executeQuery()) {

			while (resultSet.next()) {
				Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

				for (SavableParameter parameter : columns)
					user.put(parameter, resultSet.getString(parameter.getKey()));

				return user;

			}

		} catch (SQLException ex) {
			plugin.get().getLogger().log(Level.SEVERE, "Failed to read SQL ResultSet: ", ex);
		}

		return null;

	}

	// @Override
	// public void setValues(SavableParameter identification, SUUID uuid,
	// List<Pair<SavableParameter, String>> entries)
	// throws InvalidParameterException {
	// update(getMultipleUpdateQuery(identification, uuid, entries));
	// }

	@Override
	public void setString(SavableParameter identification, SUUID uuid, SavableParameter parameter, String value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setInt(SavableParameter identification, SUUID uuid, SavableParameter parameter, int value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setDouble(SavableParameter identification, SUUID uuid, SavableParameter parameter, double value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setLong(SavableParameter identification, SUUID uuid, SavableParameter parameter, long value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setBoolean(SavableParameter identification, SUUID uuid, SavableParameter parameter, boolean value) {
		setValue(identification, uuid, parameter, value);
	}

	@Override
	public void setFloat(SavableParameter identification, SUUID uuid, SavableParameter parameter, float value) {
		setValue(identification, uuid, parameter, value);
	}

	private <T> void setValue(SavableParameter identification, SUUID uuid, SavableParameter parameter, T value) {
		update("UPDATE " + tableName + " SET `" + parameter.getKey() + "`='" + value.toString() + "' WHERE "
				+ identification.getKey() + "='" + uuid.toSString() + "';", false);
	}

	@Override
	public void setStringSavableValue(SavableParameter identifier, SUUID uuid, SavableParameter parameter,
			StringSerializable value) {
		update("UPDATE " + tableName + " SET `" + parameter.getKey() + "`='" + value.toSString() + "' WHERE "
				+ identifier.getKey() + "='" + uuid.toSString() + "';", false);
	}

}
