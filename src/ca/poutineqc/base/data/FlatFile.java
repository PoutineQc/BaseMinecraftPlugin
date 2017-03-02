package ca.poutineqc.base.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bukkit.Bukkit;

import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.PPlugin;

public abstract class FlatFile implements DataStorage {

	protected File file;

	public FlatFile(PPlugin plugin, String fileName, boolean buildIn, String[] folders) {
		this.file = getFile(plugin, fileName, buildIn, folders);
	}

	public String getFileName() {
		return file.getName();
	}

	public boolean isLoaded() {
		return file.exists();
	}

	public static File getFile(PPlugin plugin, String fileName, boolean buildIn, String[] folders) {
		String completeFileName = fileName;
		File folder = getFolder(plugin, folders);
		File file = new File(folder, completeFileName);

		if (file.exists())
			return file;

		if (buildIn) {
			InputStream local = plugin.get().getResource(getFolderPath(folders) + completeFileName);
			if (local != null) {
				plugin.get().saveResource(getFolderPath(folders) + completeFileName, false);
			} else {
				plugin.get().getLogger().severe("Could not find " + completeFileName);
				plugin.get().getLogger().severe("Contact the developper as fast as possible as this should not happend.");
				Bukkit.getServer().getLogger().severe("Disabling " + plugin.getName() + "...");
				plugin.get().getPluginLoader().disablePlugin(plugin.get());
			}
		} else {
			try {
				file.createNewFile();
				return file;
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe("Could not create " + completeFileName);
				Bukkit.getServer().getLogger().severe("Review your minecraft server's permissions"
						+ " to write and edit files in it's plugin directory");
				Bukkit.getServer().getLogger().severe("Disabling " + plugin.getName() + "...");
				plugin.get().getPluginLoader().disablePlugin(plugin.get());
			}
		}

		return file;
	}

	private static File getFolder(PPlugin plugin, String... folderName2) {

		File folder = plugin.get().getDataFolder();
		for (String folderName : folderName2) {
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

	@Override
	public void createTable(List<SavableParameter> createParameters) {
		// Does nothing
	}


}
