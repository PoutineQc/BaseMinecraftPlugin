package ca.poutineqc.base.data;

import org.bukkit.configuration.ConfigurationSection;

public interface YAMLSavableValue {
	
	ConfigurationSection toConfigurationSection();
	
}
