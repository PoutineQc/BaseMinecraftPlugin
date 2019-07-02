package ca.sebastienchagnon.minecraft.prolib.dao;

public class MysqlDAOFactory extends DAOFactory {

	@Override
	public ProPlayerDAO getCustomerDAO() {
		return new MysqlProPlayerDAO();
	}

}
