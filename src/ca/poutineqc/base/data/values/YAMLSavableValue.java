package ca.poutineqc.base.data.values;

import org.bukkit.configuration.ConfigurationSection;

public interface YAMLSavableValue {
	
	ConfigurationSection toConfigurationSection();
	
}
