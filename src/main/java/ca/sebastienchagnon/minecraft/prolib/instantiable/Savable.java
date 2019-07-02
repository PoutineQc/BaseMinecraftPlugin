package ca.sebastienchagnon.minecraft.prolib.instantiable;

import java.util.UUID;

import ca.sebastienchagnon.minecraft.prolib.datastorage.DataStorage;

/**
 * Represents an Object that can be saved in a DataStorage. These objects, to be
 * stored in a DataStorage, must have a UUID and a name to ease the handling of
 * the Savable instances.
 * 
 * @author Sebastien Chagnon
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

}
