package ca.sebastienchagnon.minecraft.prolib.dao;

import java.sql.Connection;
import java.util.UUID;

import ca.sebastienchagnon.minecraft.prolib.instantiable.PPlayer;

public abstract class SQLDatabaseProPlayerDAO implements ProPlayerDAO {

	protected abstract Connection getConnection();
	
	public PPlayer getProPlayer(UUID uuid) {
		// TODO return a Player instance if it exists, null otherwise
		return null;
	}
	
}
