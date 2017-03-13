package ca.poutineqc.base.instantiable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ca.poutineqc.base.datastorage.DataStorage;
import ca.poutineqc.base.datastorage.JSON;
import ca.poutineqc.base.datastorage.MySQL;
import ca.poutineqc.base.datastorage.SQLite;
import ca.poutineqc.base.datastorage.YAML;
import ca.poutineqc.base.plugin.PPlugin;

/**
 * A manager that contains an array of Object that extend the interface Savable.
 * From this manager, it is possible to add or remove items, and get them based
 * on their UUID and name that can be accessed from the Savable insterface.
 *
 * @author Sebastien Chagnon
 * @see Savable
 */
public abstract class SavableManager<T extends Savable> extends LinkedList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5816468850208714331L;

	private DataStorage data;

	private Collection<UUID> savedInstances;

	/**
	 * Creates an empty manager.
	 */
	public SavableManager(PPlugin plugin, String tableName, boolean loadSavedInstances) {

		data = openDataStorage(plugin, tableName);

		this.savedInstances = data.getAllIdentifications(getIdentification(), getColumns());

		if (loadSavedInstances)
			for (UUID id : savedInstances)
				this.add(newInstance(data, id));

	}

	public void loadIfSaved(UUID uuid) {
		if (savedInstances.contains(uuid))
			this.add(newInstance(data, uuid));
	}

	public void removeIfHandled(UUID uuid) {
		T instance = get(uuid);
		this.remove(instance);
	}

	public DataStorage getDataStorage() {
		return data;
	}

	public abstract T newInstance(DataStorage data, UUID uuid);

	@Override
	public boolean add(T e) {
		if (!savedInstances.contains(e.getUUID()))
			savedInstances.add(e.getUUID());
		
		return super.add(e);
	}

	private DataStorage openDataStorage(PPlugin plugin, String tableName) {

		switch (plugin.getConfig().getString("dataStorage").toLowerCase()) {
		case "mysql":

			return new MySQL(plugin, tableName);

		case "json":

			return new JSON(plugin, tableName.toLowerCase(), false);

		case "yaml":
		case "yml":

			return new YAML(plugin, tableName.toLowerCase(), false);

		case "sqlite":
		default:

			return new SQLite(plugin, tableName);
		}
	}

	/**
	 * Get one of this manager's instance base on the UUID of the instance.
	 *
	 * @param identification
	 *            the UUID that will be compared to this manager's instance's
	 *            UUID
	 * @return the required instance if it is contained in this manager's
	 *         instances, null if it is not
	 * @see Savable#getUUID
	 */
	public T get(UUID identification) {
		for (T instance : this)
			if (instance.getUUID().compareTo(identification) == 0)
				return instance;

		return null;
	}

	/**
	 * Get one of this manager's instance base on the name of the instance.
	 *
	 * @param name
	 *            the string that will be compared to this manager's instance's
	 *            names
	 * @return the required instance if it is contained in this manager's
	 *         instances, null if it is not
	 * @see Savable#getName
	 */
	public T get(String name) {
		for (T instance : this)
			if (instance.getName().equalsIgnoreCase(name))
				return instance;

		return null;
	}

	abstract public SavableParameter getIdentification();

	public boolean isSaved(UUID uuid) {
		for (UUID id : savedInstances)
			if (id.equals(uuid))
				return true;

		return false;
	}

	abstract public List<SavableParameter> getColumns();
}
