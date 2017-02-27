package ca.poutineqc.base.instantiable;

public interface SavableParameter {

	/**
	 * Returns the default value for this SavableParameter
	 * 
	 * @return the default value for this SavableParameter
	 * @throws ClassCastException
	 *             if the true value of this SavableParameter can't be cast to
	 *             the required value
	 */
	String getDefaultValue();

	/**
	 * Returns the key of this SavableParameter. This key is the name of the
	 * column as it is stored in a DataStorage
	 * 
	 * @return the key of this SavableParameter as it is stored in the
	 *         DataStorage
	 */
	public String getKey();

}
