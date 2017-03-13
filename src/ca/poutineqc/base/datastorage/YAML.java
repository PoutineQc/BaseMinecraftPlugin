package ca.poutineqc.base.datastorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import ca.poutineqc.base.datastorage.serializable.SUUID;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Pair;

/**
 * A FlatFile that is used to store data on. It can read or write Data. It
 * implements DataStorage.
 * 
 * @author Sebastien Chagnon
 * @see DataStorage
 */
public class YAML extends FlatFile {

	// =========================================================================
	// Fields
	// =========================================================================

	private YamlConfiguration yaml;

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
	 * @param folderName
	 *            - the folders in which the file should be stored
	 * @see Library
	 */
	public YAML(PPlugin plugin, String fileName, boolean buildIn, String... folderName) {
		super(plugin, fileName.replace(".yml", "") + ".yml", buildIn, folderName);
		
		yaml = YamlConfiguration.loadConfiguration(file);
		
	}

	// =========================================================================
	// Data Accessors
	// =========================================================================

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification, List<SavableParameter> columns) {
		List<UUID> identifications = new ArrayList<UUID>();

		for (String key : yaml.getKeys(false))
			identifications.add(new SUUID(key).getUUID());

		return identifications;
	}

	@Override
	public void newInstance(SavableParameter identification, SUUID uuid,
			List<Pair<SavableParameter, StringSerializable>> createParameters) {
		for (Pair<SavableParameter, StringSerializable> entry : createParameters)
			setStringSavableValue(identification, uuid, entry.getKey(), entry.getValue());
		save();
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, SUUID uuid,
			SavableParameter[] parameters) {

		Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

		ConfigurationSection cs = yaml.getConfigurationSection(uuid.toSString());

		for (SavableParameter parameter : parameters) {
			user.put(parameter, cs.getString(parameter.getKey()));
		}

		return user;
	}

	@Override
	public void setString(SavableParameter identification, SUUID uuid, SavableParameter parameter, String value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setInt(SavableParameter identification, SUUID uuid, SavableParameter parameter, int value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setDouble(SavableParameter identification, SUUID uuid, SavableParameter parameter, double value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setLong(SavableParameter identification, SUUID uuid, SavableParameter parameter, long value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setBoolean(SavableParameter identification, SUUID uuid, SavableParameter parameter, boolean value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setFloat(SavableParameter identification, SUUID uuid, SavableParameter parameter, float value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setStringSavableValue(SavableParameter identifier, SUUID uuid, SavableParameter parameter, StringSerializable value) {
		yaml.set(uuid.toSString() + "." + parameter.getKey(), value.toSString());
		save();
	}
	
	private void save() {
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationSection getYAML() {
		return yaml;
	}
}
