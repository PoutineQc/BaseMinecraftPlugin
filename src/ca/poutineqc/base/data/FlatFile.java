package ca.poutineqc.base.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.PoutinePlugin;
import ca.poutineqc.base.utils.Pair;

public class FlatFile implements DataStorage {

	private PluginYAMLFile file;

	public FlatFile(PoutinePlugin plugin, String fileName, boolean builtIn, String... folders) {
		file = new PluginYAMLFile(fileName, builtIn, folders);
	}

	@Override
	public void setString(Pair<SavableParameter, UUID> identification, SavableParameter parameter, String value) {
		file.set(identification.getValue().toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setInt(Pair<SavableParameter, UUID> identification, SavableParameter parameter, int value) {
		file.set(identification.getValue().toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setDouble(Pair<SavableParameter, UUID> identification, SavableParameter parameter, double value) {
		file.set(identification.getValue().toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setLong(Pair<SavableParameter, UUID> identification, SavableParameter parameter, long value) {
		file.set(identification.getValue().toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setBoolean(Pair<SavableParameter, UUID> identification, SavableParameter parameter, boolean value) {
		file.set(identification.getValue().toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public void setFloat(Pair<SavableParameter, UUID> identification, SavableParameter parameter, float value) {
		file.set(identification.getValue().toString() + "." + parameter.getKey(), value);
		file.save();
	}

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification) {
		List<UUID> identifications = new ArrayList<UUID>();

		for (String key : file.getKeys(false))
			identifications.add(UUID.fromString(key));

		return identifications;
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(Pair<SavableParameter, UUID> identification,
			List<SavableParameter> parameters) {

		Map<SavableParameter, String> user = new HashMap<SavableParameter, String>();

		ConfigurationSection cs = file.getConfigurationSection(identification.getValue().toString());

		for (SavableParameter parameter : parameters) {

			switch (parameter.getType()) {
			case BOOLEAN:
				user.put(parameter, String.valueOf(cs.getBoolean(parameter.getKey())));
				break;
			case DOUBLE:
			case FLOAT:
				user.put(parameter, String.valueOf(cs.getDouble(parameter.getKey())));
				break;
			case INTEGER:
				user.put(parameter, String.valueOf(cs.getInt(parameter.getKey())));
				break;
			case LONG:
				user.put(parameter, String.valueOf(cs.getLong(parameter.getKey())));
				break;
			case STRING:
				user.put(parameter, cs.getString(parameter.getKey()));
				break;

			}
		}

		return user;
	}

	@Override
	public void newInstance(Pair<SavableParameter, UUID> identification,
			List<Pair<SavableParameter, String>> createParameters) {
		for (Pair<SavableParameter, String> entry : createParameters)
			setString(identification, entry.getKey(), entry.getValue());
	}
}
