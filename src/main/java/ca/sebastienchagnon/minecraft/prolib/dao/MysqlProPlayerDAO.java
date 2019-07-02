package ca.sebastienchagnon.minecraft.prolib.dao;

import java.sql.Connection;

public class MysqlProPlayerDAO extends SQLDatabaseProPlayerDAO {

	@Override
	protected Connection getConnection() {
		// TODO Return a MySQL connection from a database pool
		return null;
	}
	
}
