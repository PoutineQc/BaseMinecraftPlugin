package me.poutineqc.base;

import java.util.HashMap;

import org.bukkit.ChatColor;

import me.poutineqc.data.PluginYAMLFile;

public class Language {

	public static final String[] BUILT_IN = { "en", "fr" };
	public static final String FOLDER_NAME = "languageFiles";

	private static HashMap<String, Language> languages;

	private PluginYAMLFile yamlFile;

	public static void loadLanguages() {
		languages = new HashMap<String, Language>();
		for (String fileName : BUILT_IN)
			languages.put(fileName, new Language(fileName, true));

		String defaultLanguage = getDefaultName();
		if (!isLanguage(defaultLanguage)) {
			Language temp = new Language(defaultLanguage, false);
			if (temp.yamlFile.isLoaded())
				languages.put(defaultLanguage, temp);
		}
	}

	public static boolean isLanguage(String fileName) {
		for (String fn : languages.keySet())
			if (fn.equals(fileName))
				return true;

		return false;
	}

	public static Language getLanguage(String fileName) {
		for (String fn : languages.keySet())
			if (fn.equals(fileName))
				return languages.get(fn);

		return getDefault();
	}

	public static Language getDefault() {
		String defaultLanguage = getDefaultName();

		for (String fn : languages.keySet())
			if (fn.equals(defaultLanguage))
				return languages.get(fn);

		return null;
	}

	public static String getDefaultName() {
		return Plugin.get().getConfig().getString("language", "en");
	}
	
	public void sendMessage(PluginPlayer player, Messages message) {
			if (Plugin.get().getConfig().getBoolean("prefix", true))
				player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
						getMessage(Messages.PREFIX) + " " + getMessage(message)));
			else
				player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage(message)));
	}

	public Language(String fileName, boolean builtIn) {
		yamlFile = new PluginYAMLFile(fileName, builtIn, FOLDER_NAME);
	}

	public String getMessage(Messages message) {
		return yamlFile.getString(message.key, message.defaultValue);
	}

	public enum Messages {

		LANGUAGE_NAME("languageName", "english"), PREFIX("prefix", "[pl]"), NOBODY("keyWordNoOne", "no one yet");

		private String key;
		private String defaultValue;

		private Messages(String key, String defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
	}
}
