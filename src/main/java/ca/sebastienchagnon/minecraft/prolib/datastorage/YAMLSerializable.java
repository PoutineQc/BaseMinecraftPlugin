package ca.sebastienchagnon.minecraft.prolib.datastorage;

import org.bukkit.configuration.ConfigurationSection;

public interface YAMLSerializable {
	
	ConfigurationSection toConfigurationSection();
	
}
