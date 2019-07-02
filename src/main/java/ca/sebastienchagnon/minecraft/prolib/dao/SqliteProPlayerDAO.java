package ca.sebastienchagnon.minecraft.prolib.dao;

import java.sql.Connection;

public class SqliteProPlayerDAO extends SQLDatabaseProPlayerDAO {

	@Override
	protected Connection getConnection() {
		// TODO Return a SQLite connection fromm a database pool
		return null;
	}

}
