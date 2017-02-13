package me.poutineqc.instantiable;

import java.util.List;
import java.util.UUID;

import me.poutineqc.data.DataStorage;
import me.poutineqc.plugin.PoutinePlugin;

public interface Savable {
	
	public String getTableName();
	public DataStorage openDataStorage(PoutinePlugin plugin);
	public DataStorage getDataStorage();
	public List<SavableParameter> getParameters();
	
	public UUID getUUID();
	public String getName();
	
	
	
//	protected DataStorage data;
//	
//	public Savable(DataStorage data) {
//		this.data = data;
//		
//		addColumns();
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static void addColumn(Column<?> column, boolean identifier) {
//		if (!columns.contains(column))
//			columns.add(column);
//		
//		if (identifier && column.getDefaultValue() instanceof String)
//			this.identifier = (Column<String>) column;
//	}
//
//	public abstract void addColumns();
//	public abstract String getTableName();

}
