package ca.poutineqc.base.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import ca.poutineqc.base.data.values.PUUID;
import ca.poutineqc.base.data.values.SValue;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.PYAMLFile;
import ca.poutineqc.base.utils.Pair;

/**
 * A FlatFile that is used to store data on. It can read or write Data. It
 * implements DataStorage.
 * 
 * @author Sébastien Chagnon
 * @see DataStorage
 */
public class FlatFile implements DataStorage {
	
	// =========================================================================
	// Fields
	// =========================================================================

	private PYAMLFile file;

	
	// =========================================================================
	// Constructor(s)
	// =========================================================================

	/**
	 * Default constructor.
	 * 
	 * @param plugin
	 *            - the main class of the plugin
	 * @param fileName
	 *            - the file name to be used for the creation of the file with
	 *            the PYAMLFile
	 * @param folders
	 *            - the folders in which the file should be stored
	 * @see PPlugin
	 */
	public FlatFile(PPlugin plugin, String fileName, String... folders) {
		file = new PYAMLFile(fileName, false, folders);
	}


	// =========================================================================
	// Data Accessors
	// =========================================================================

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification) {
		List<UUID> identifications = new ArrayList<UUID>();

		for (String key : file.getKeys(false))
			identifications.add(UUID.fromString(key));

		return identifications;
	}

	@Override
	public void newInstance(SavableParameter identification, PUUID uuid, List<Pair<SavableParameter, SValue>> createParameters) {
		for (Pair<SavableParameter, SValue> entry : createParameters)
			set(identification, uuid, entry.getKey(), entry.getValue());
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, PUUID uuid,
			Collection<SavableParameter> parameters) {

		Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

		ConfigurationSection cs = file.getConfigurationSection(uuid.toSString());

		for (SavableParameter parameter : parameters) {
			user.put(parameter, cs.getString(parameter.getKey()));
		}

		return user;
	}
	
	@Override
	public void setString(SavableParameter identification, PUUID uuid, SavableParameter parameter, String value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setInt(SavableParameter identification, PUUID uuid, SavableParameter parameter, int value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setDouble(SavableParameter identification, PUUID uuid, SavableParameter parameter, double value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setLong(SavableParameter identification, PUUID uuid, SavableParameter parameter, long value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setBoolean(SavableParameter identification, PUUID uuid, SavableParameter parameter, boolean value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setFloat(SavableParameter identification, PUUID uuid, SavableParameter parameter, float value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setValues(SavableParameter identification, PUUID uuid, List<Pair<SavableParameter, String>> entries)
			throws InvalidParameterException {
		throw new InvalidParameterException("A flat file can't write multiple values at once");
	}


	@Override
	public void set(SavableParameter identifier, PUUID uuid, SavableParameter parameter, SValue value) {
		file.set(uuid.toSString() + "." + parameter.getKey(), value.toSString());
		
	}
}
