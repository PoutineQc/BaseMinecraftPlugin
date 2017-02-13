package me.poutineqc.lang;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.poutineqc.data.PluginYAMLFile;
import me.poutineqc.plugin.ConfigurationKey;
import me.poutineqc.plugin.PoutinePlugin;

public class Language {

	public static final String FOLDER_NAME = "languageFiles";

	PluginYAMLFile yamlFile;
	private boolean prefixBeforeEveryMessage;
	private HashMap<String, String> messages;
	private String prefixKey;

	public Language(PoutinePlugin plugin, String fileName, boolean builtIn) {
		prefixBeforeEveryMessage = plugin.getConfig().getBoolean(ConfigurationKey.PREFIX.getKey(), true);

		yamlFile = new PluginYAMLFile(fileName, builtIn, FOLDER_NAME);
	}

	public void sendMessage(Player player, String key) {
		if (prefixBeforeEveryMessage)
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', getMessage(prefixKey) + " " + getMessage(key)));
		else
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage(key)));
	}

	public void addMessage(String key, String defaultValue) {
		messages.put(key, yamlFile.getString(key, defaultValue));
	}

	public void setPrefixKey(String prefixKey) {
		this.prefixKey = prefixKey;
	}

	public String getMessage(String key) {
		return messages.get(key);
	}
}
