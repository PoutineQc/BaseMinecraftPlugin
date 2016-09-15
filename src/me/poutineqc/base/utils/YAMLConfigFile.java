package me.poutineqc.base.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.poutineqc.base.Plugin;

public class YAMLConfigFile {
	File folder;
	File file;
	FileConfiguration config;

	public YAMLConfigFile(String fileName, boolean buildIn, String... folders) {
		getFile(fileName, buildIn, folders);
		if (!file.exists())
			return;

		this.config = YamlConfiguration.loadConfiguration(file);
	}

	public void getFile(String fileName, boolean buildIn, String... folders) {
		Plugin plugin = Plugin.get();
		String completeFileName = fileName + ".yml";
		folder = getFolder(plugin, folders);
		file = new File(folder, completeFileName);

		if (file.exists())
			return;

		if (buildIn) {
			InputStream local = plugin.getResource(getFolderPath(folders) + fileName + ".yml");
			if (local != null) {
				plugin.saveResource(folders + completeFileName, false);
			} else {
				plugin.getLogger().severe("Could not find " + completeFileName);
				plugin.getLogger().severe("Contact the developper as fast as possible as this should not happend.");
				Bukkit.getServer().getLogger().severe("Disabling " + plugin.getName() + "...");
				plugin.getPluginLoader().disablePlugin(plugin);
			}
		} else {
			try {
				file.createNewFile();
				return;
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe("Could not create " + fileName + ".ylm.");
				Bukkit.getServer().getLogger().severe("Review your minecraft server's permissions"
						+ " to write and edit files in it's plugin directory");
				Bukkit.getServer().getLogger().severe("Disabling " + plugin.getName() + "...");
				plugin.getPluginLoader().disablePlugin(plugin);
			}
		}
	}

	private File getFolder(Plugin plugin, String... folders) {

		File folder = plugin.getDataFolder();
		for (String folderName : folders) {
			folder = new File(folder, folderName);
			if (!folder.exists())
				folder.mkdir();
		}

		return folder;
	}

	private String getFolderPath(String... folders) {

		StringBuilder path = new StringBuilder();
		for (String folderName : folders) {
			path.append(folderName);
			path.append("/");
		}

		return path.toString();
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe("Could not save " + ((file == null) ? "the file." : file.getName() + ".yml."));
		}
	}

	public File getFile() {
		return file;
	}

	public FileConfiguration get() {
		return config;
	}
}
