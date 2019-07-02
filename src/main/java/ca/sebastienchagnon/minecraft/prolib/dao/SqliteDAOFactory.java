package ca.sebastienchagnon.minecraft.prolib.dao;

public class SqliteDAOFactory extends DAOFactory {

	@Override
	public ProPlayerDAO getCustomerDAO() {
		return new SqliteProPlayerDAO();
	}

}
