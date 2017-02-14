package ca.poutineqc.base.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.utils.Pair;

/**
 * Represents a DataStorage that can store different types of values and change
 * them individually or in bunk. The values may only be read several at a time,
 * and should only be used for creation of new Objects at the beginning of the
 * program. Instances may be added to the DataStorage but not removed.
 * 
 * @author Sébastien Chagnon
 * @see Savable
 * @see Pair
 * @see SavableParameter
 * @see UUID
 */
public interface DataStorage {
	
	/**
	 * Sets the value of the parameter to the required value for the savable
	 * instance defined in the value variable of the Pair object.
	 * 
	 * @param identification
	 *            - a Pair containing as the key the SavableParameter that will
	 *            be used to select the column of this DataStorage and a UUID as
	 *            it's value. The UUID is the one of the instance to be
	 *            modified.
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter to
	 *            modify if this DataStorage.
	 * @param value
	 *            - the String value that will be saved in the DataStorage at
	 *            the selected parameter location.
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void setString(Pair<SavableParameter, UUID> identification, SavableParameter parameter, String value);

	/**
	 * Sets the value of the parameter to the required value for the savable
	 * instance defined in the value variable of the Pair object.
	 * 
	 * @param identification
	 *            - a Pair containing as the key the SavableParameter that will
	 *            be used to select the column of this DataStorage and a UUID as
	 *            it's value. The UUID is the one of the instance to be
	 *            modified.
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter to
	 *            modify if this DataStorage.
	 * @param value
	 *            - the int value that will be saved in the DataStorage at the
	 *            selected parameter location.
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void setInt(Pair<SavableParameter, UUID> identification, SavableParameter parameter, int value);

	/**
	 * Sets the value of the parameter to the required value for the savable
	 * instance defined in the value variable of the Pair object.
	 * 
	 * @param identification
	 *            - a Pair containing as the key the SavableParameter that will
	 *            be used to select the column of this DataStorage and a UUID as
	 *            it's value. The UUID is the one of the instance to be
	 *            modified.
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter to
	 *            modify if this DataStorage.
	 * @param value
	 *            - the double value that will be saved in the DataStorage at
	 *            the selected parameter location.
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void setDouble(Pair<SavableParameter, UUID> identification, SavableParameter parameter, double value);

	/**
	 * Sets the value of the parameter to the required value for the savable
	 * instance defined in the value variable of the Pair object.
	 * 
	 * @param identification
	 *            - a Pair containing as the key the SavableParameter that will
	 *            be used to select the column of this DataStorage and a UUID as
	 *            it's value. The UUID is the one of the instance to be
	 *            modified.
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter to
	 *            modify if this DataStorage.
	 * @param value
	 *            - the long value that will be saved in the DataStorage at the
	 *            selected parameter location.
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void setLong(Pair<SavableParameter, UUID> identification, SavableParameter parameter, long value);

	/**
	 * Sets the value of the parameter to the required value for the savable
	 * instance defined in the value variable of the Pair object.
	 * 
	 * @param identification
	 *            - a Pair containing as the key the SavableParameter that will
	 *            be used to select the column of this DataStorage and a UUID as
	 *            it's value. The UUID is the one of the instance to be
	 *            modified.
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter to
	 *            modify if this DataStorage.
	 * @param value
	 *            - the boolean value that will be saved in the DataStorage at
	 *            the selected parameter location.
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void setBoolean(Pair<SavableParameter, UUID> identification, SavableParameter parameter, boolean value);

	/**
	 * Sets the value of the parameter to the required value for the savable
	 * instance defined in the value variable of the Pair object.
	 * 
	 * @param identification
	 *            - a Pair containing as the key the SavableParameter that will
	 *            be used to select the column of this DataStorage and a UUID as
	 *            it's value. The UUID is the one of the instance to be
	 *            modified.
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter to
	 *            modify if this DataStorage.
	 * @param value
	 *            - the float value that will be saved in the DataStorage at the
	 *            selected parameter location.
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void setFloat(Pair<SavableParameter, UUID> identification, SavableParameter parameter, float value);

	/**
	 * Returns a list of all the identifications in the DataStorage. The
	 * identification factor is defined as the SavableParameter parameter and
	 * the instances are returned as a List of UUID.
	 * 
	 * @param parameter
	 *            - the SavableParameter that indicates which parameter
	 *            represents the UUID column.
	 * @return an ArrayList of all the UUIDs of the saved instances
	 * @see SavableParameter
	 * @see UUID
	 */
	List<UUID> getAllIdentifications(SavableParameter parameter);

	/**
	 * Saves a new instance of a Savable in this DataStorage. The UUID of the
	 * instance is saved, as well as a list of Pair containing the values to
	 * save in the DataStorage at the same time as it's creation. The UUID is
	 * also stored in a Pair with it's SavableParameter.
	 * 
	 * @param identification
	 *            - a Pair, which contain as it's key a SavableParameter
	 *            representing the UUID and the UUID itself as it's value
	 * @param createParameters
	 *            - a List of Pair, which all contain as their keys a
	 *            SavableParameter representing the column to save the value
	 *            stored as a String in it's value
	 * @see Savable
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	void newInstance(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> createParameters);

	/**
	 * Returns all the data requested in the parameters parameter's List from
	 * the instance with the required UUID.
	 * 
	 * @param identification
	 *            - a Pair, which contain as it's key a SavableParameter
	 *            representing the UUID and the UUID itself as it's value, which
	 *            represents the instance's identification to get the
	 *            appropriate data
	 * @param parameters
	 *            - a List of SavableParameter; it's values are the ones
	 *            requested in the return Map
	 * @return a Map that has as it's keys all the parameters requested in the
	 *         parameters parameter and as the requested values as the Map
	 *         values
	 * @see Pair
	 * @see SavableParameter
	 * @see UUID
	 */
	Map<SavableParameter, String> getIndividualData(Pair<SavableParameter, UUID> identification,
			List<SavableParameter> parameters);

}
