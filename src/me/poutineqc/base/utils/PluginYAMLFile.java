package me.poutineqc.base.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import me.poutineqc.base.Plugin;

public class PluginYAMLFile extends YamlConfiguration {

	private File file;

	public PluginYAMLFile(String fileName, boolean buildIn, String... folders) {
		file = getFile(fileName, buildIn, folders);
		if (!file.exists())
			return;

		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static File getFile(String fileName, boolean buildIn, String... folders) {
		Plugin plugin = Plugin.get();
		String completeFileName = fileName + ".yml";
		File folder = getFolder(plugin, folders);
		File file = new File(folder, completeFileName);

		if (file.exists())
			return file;

		if (buildIn) {
			InputStream local = plugin.getResource(getFolderPath(folders) + completeFileName);
			if (local != null) {
				plugin.saveResource(getFolderPath(folders) + completeFileName, false);
			} else {
				plugin.getLogger().severe("Could not find " + completeFileName);
				plugin.getLogger().severe("Contact the developper as fast as possible as this should not happend.");
				Bukkit.getServer().getLogger().severe("Disabling " + plugin.getName() + "...");
				plugin.getPluginLoader().disablePlugin(plugin);
			}
		} else {
			try {
				file.createNewFile();
				return null;
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe("Could not create " + completeFileName);
				Bukkit.getServer().getLogger().severe("Review your minecraft server's permissions"
						+ " to write and edit files in it's plugin directory");
				Bukkit.getServer().getLogger().severe("Disabling " + plugin.getName() + "...");
				plugin.getPluginLoader().disablePlugin(plugin);
			}
		}

		return file;
	}

	private static File getFolder(Plugin plugin, String... folders) {

		File folder = new File(".");
		for (String folderName : folders) {
			folder = new File(folder, folderName);
			if (!folder.exists())
				folder.mkdir();
		}

		return folder;
	}

	private static String getFolderPath(String... folders) {

		StringBuilder path = new StringBuilder();
		for (String folderName : folders) {
			path.append(folderName);
			path.append("/");
		}

		return path.toString();
	}

	public void save() {
		try {
			save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
