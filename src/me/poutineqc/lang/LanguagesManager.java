package me.poutineqc.lang;

import java.util.HashMap;
import java.util.Map.Entry;

import me.poutineqc.plugin.ConfigurationKey;
import me.poutineqc.plugin.PoutinePlugin;

public abstract class LanguagesManager {

	public String[] builtIn;
	
	private String defaultLanguage;
	private HashMap<String, Language> languages;

	public abstract void addMessages(Language language);
	public abstract void setPrefixKey(Language language);
	
	public LanguagesManager(PoutinePlugin plugin, String[] builtIn) {
		this.builtIn = builtIn;
		
		languages = new HashMap<String, Language>();
		for (String fileName : builtIn)
			languages.put(fileName, new Language(plugin, fileName, true));

		defaultLanguage = plugin.getConfig().getString(ConfigurationKey.LANGUAGE.getKey(), builtIn[0]);
		if (!isLanguage(defaultLanguage)) {
			Language temp = new Language(plugin, defaultLanguage, false);
			if (temp.yamlFile.isLoaded())
				languages.put(defaultLanguage, temp);
		}
		
		for (Entry<String, Language> entry : languages.entrySet()) {
			addMessages(entry.getValue());
			setPrefixKey(entry.getValue());
		}
	}

	public boolean isLanguage(String fileName) {
		for (String fn : languages.keySet())
			if (fn.equals(fileName))
				return true;

		return false;
	}

	public Language getLanguage(String fileName) {
		for (String fn : languages.keySet())
			if (fn.equals(fileName))
				return languages.get(fn);

		return getDefault();
	}

	public Language getDefault() {
		for (String fn : languages.keySet())
			if (fn.equals(defaultLanguage))
				return languages.get(fn);

		return getMain();
	}

	public Language getMain() {
		for (String fn : languages.keySet())
			if (fn.equals(builtIn[0]))
				return languages.get(fn);

		return null;
	}
	
}
