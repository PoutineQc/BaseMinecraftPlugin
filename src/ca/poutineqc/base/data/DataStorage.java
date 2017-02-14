package ca.poutineqc.base.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.utils.Pair;

public interface DataStorage {

//	String getString(Pair<SavableParameter, UUID> identification, SavableParameter parameter);

	void setString(Pair<SavableParameter, UUID> identification, SavableParameter parameter, String value);

//	int getInt(Pair<SavableParameter, UUID> identification, SavableParameter parameter);

	void setInt(Pair<SavableParameter, UUID> identification, SavableParameter parameter, int value);

//	double getDouble(Pair<SavableParameter, UUID> identification, SavableParameter parameter);

	void setDouble(Pair<SavableParameter, UUID> identification, SavableParameter parameter, double value);

//	long getLong(Pair<SavableParameter, UUID> identification, SavableParameter parameter);

	void setLong(Pair<SavableParameter, UUID> identification, SavableParameter parameter, long value);

//	boolean getBoolean(Pair<SavableParameter, UUID> identification, SavableParameter parameter);

	void setBoolean(Pair<SavableParameter, UUID> identification, SavableParameter parameter, boolean value);

//	float getFloat(Pair<SavableParameter, UUID> identification, SavableParameter parameter);

	void setFloat(Pair<SavableParameter, UUID> identification, SavableParameter parameter, float value);

	List<UUID> getAllIdentifications(SavableParameter parameter);

	void newInstance(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> createParameters);

	Map<SavableParameter, String> getIndividualData(Pair<SavableParameter, UUID> identification, List<SavableParameter> parameters);

}
