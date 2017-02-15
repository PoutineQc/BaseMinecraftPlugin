package ca.poutineqc.base.instantiable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * A manager that contains an array of Object that extend the interface Savable.
 * From this manager, it is possible to add or remove items, and get them based
 * on their UUID and name that can be accessed from the Savable insterface.
 *
 * @author Sébastien Chagnon
 * @see Savable
 */
public abstract class SavableManager<T extends Savable> {

	private List<T> instances;
	private Collection<UUID> savedInstances;

	/**
	 * Creates an empty manager.
	 */
	public SavableManager() {
		this.instances = new LinkedList<T>();
		this.savedInstances = getAllSavedUUIDs();
	}

	/**
	 * Returns true if this manager's instances contains the specified element.
	 *
	 * @param instance
	 *            the instance to check for
	 * @return true if this manager's instances contains the specified element
	 */
	public boolean contains(T instance) {
		return instances.contains(instance);
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
		for (T instance : instances)
			if (instance.getUUID().compareTo(identification) == 0)
				return instance;

		return null;
	}

	/**
	 * Adds all of the supplied instances to this manager's instances
	 *
	 * @param instances
	 *            the list of instances that is to be added to this manager's
	 *            instances
	 */
	public void addAll(List<T> instances) {
		for (T instance : instances)
			this.add(instance);
	}

	/**
	 * Get one of this manager's instance base on the name of the instance.
	 *
	 * @param name
	 *            the string that will be compared to this manager's
	 *            instance's names
	 * @return the required instance if it is contained in this manager's
	 *         instances, null if it is not
	 * @see Savable#getName
	 */
	public T get(String name) {
		for (T instance : instances)
			if (instance.getName().equalsIgnoreCase(name))
				return instance;

		return null;
	}

	/**
	 * Adds the required instance in this manager's instances.
	 *
	 * @param instance
	 *            element to be added from this manager's instances
	 * @throws IllegalArgumentException
	 *             if the instance to add to this manager's instances is already
	 *             in them
	 */
	public void add(T instance) throws IllegalArgumentException {
		if (this.contains(instance))
			throw new IllegalArgumentException("This instance is already handled by the manager");

		instances.add(instance);
	}

	/**
	 * Removes the required instance of this manager's instances.
	 *
	 * @param instance
	 *            element to be removed from this manager's instances
	 * @return true if this list contained the specified element
	 */
	public boolean remove(T instance) {
		return instances.remove(instance);
	}

	abstract Collection<UUID> getAllSavedUUIDs();

	public boolean isSaved(UUID uuid) {
		return savedInstances.contains(uuid);
	}

}
