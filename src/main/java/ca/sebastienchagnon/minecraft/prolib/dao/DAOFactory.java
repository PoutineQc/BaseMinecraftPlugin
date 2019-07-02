package ca.sebastienchagnon.minecraft.prolib.dao;

public abstract class DAOFactory {

// List of DAO types supported by the factory
	public static final int MYSQL = 1;
	public static final int SQLITE = 2;

// There will be a method for each DAO that can be 
// created. The concrete factories will have to 
// implement these methods.
	public abstract ProPlayerDAO getCustomerDAO();

	public static DAOFactory getDAOFactory(int whichFactory) {

		switch (whichFactory) {
		case MYSQL:
			return new MysqlDAOFactory();
		case SQLITE:
			return new SqliteDAOFactory();
		default:
			return null;
		}
	}
}