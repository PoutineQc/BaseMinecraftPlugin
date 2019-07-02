package ca.sebastienchagnon.minecraft.prolib.datastorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import ca.sebastienchagnon.minecraft.prolib.Library;
import ca.sebastienchagnon.minecraft.prolib.PPlugin;
import ca.sebastienchagnon.minecraft.prolib.instantiable.SavableParameter;
import ca.sebastienchagnon.minecraft.prolib.utils.Pair;

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
			identifications.add(UUID.fromString(key));

		return identifications;
	}

	@Override
	public void newInstance(SavableParameter identification, UUID uuid,
			List<Pair<SavableParameter, StringSerializable>> createParameters) {
		for (Pair<SavableParameter, StringSerializable> entry : createParameters)
			setStringSavableValue(identification, uuid, entry.getKey(), entry.getValue());
		save();
	}

	@Override
	public void deleteInstance(SavableParameter identification, UUID uuid) {
		this.yaml.set(uuid.toString(), null);
		save();
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, UUID uuid,
			List<SavableParameter> columns) {

		Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

		ConfigurationSection cs = yaml.getConfigurationSection(uuid.toString());

		for (SavableParameter parameter : columns) {
			user.put(parameter, cs.getString(parameter.getKey()));
		}

		return user;
	}

	@Override
	public void setString(SavableParameter identification, UUID uuid, SavableParameter parameter, String value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setInt(SavableParameter identification, UUID uuid, SavableParameter parameter, int value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setDouble(SavableParameter identification, UUID uuid, SavableParameter parameter, double value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setLong(SavableParameter identification, UUID uuid, SavableParameter parameter, long value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setBoolean(SavableParameter identification, UUID uuid, SavableParameter parameter, boolean value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setFloat(SavableParameter identification, UUID uuid, SavableParameter parameter, float value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value);
		save();
	}

	@Override
	public void setStringSavableValue(SavableParameter identifier, UUID uuid, SavableParameter parameter,
			StringSerializable value) {
		yaml.set(uuid.toString() + "." + parameter.getKey(), value.toSString());
		save();
	}

	public void save() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					yaml.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	public ConfigurationSection getYAML() {
		return yaml;
	}
}
