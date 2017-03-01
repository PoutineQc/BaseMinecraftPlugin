package ca.poutineqc.base.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class PYAMLFile extends YamlConfiguration {

	private File file;

	public PYAMLFile(File file) {
		if (!file.exists())
			return;

		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isLoaded() {
		return file.exists();
	}

}
