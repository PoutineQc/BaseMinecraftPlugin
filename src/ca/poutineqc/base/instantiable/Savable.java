package ca.poutineqc.base.instantiable;

import java.util.UUID;

/**
 * Represents an Object that can be saved in a DataStorage. These objects, to be
 * stored in a DataStorage, must have a UUID and a name to ease the handling of
 * the Savable instances.
 * 
 * @author Sébastien Chagnon
 * @see DataStorage
 * @see UUID
 */
public interface Savable {

	/**
	 * Returns the UUID of the Savable Object
	 * 
	 * @return the UUID of the Savable Object
	 * @see UUID
	 */
	public UUID getUUID();

	/**
	 * Returns the name of the Savable Object
	 * 
	 * @return the name of the Savable Object
	 */
	public String getName();

}
