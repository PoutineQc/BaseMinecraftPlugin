package ca.poutineqc.base.datastorage;

import org.bukkit.configuration.ConfigurationSection;

public interface YAMLSerializable {
	
	ConfigurationSection toConfigurationSection();
	
}
