package ca.poutineqc.base.instantiable;

import java.util.List;
import java.util.UUID;

import ca.poutineqc.base.data.DataStorage;

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
	UUID getUUID();

	/**
	 * Returns the name of the Savable Object
	 * 
	 * @return the name of the Savable Object
	 */
	String getName();

	/**
	 * Returns a list of all the SavableParameter from a Savable object that can
	 * be stored in a DataStorage
	 * 
	 * @return a list of all the SavableParameter from a Savable object
	 * @see SavableParameter
	 */
	List<SavableParameter> getParameters();

}
