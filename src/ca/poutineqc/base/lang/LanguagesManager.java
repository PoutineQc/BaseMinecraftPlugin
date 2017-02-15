package ca.poutineqc.base.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ca.poutineqc.base.plugin.PConfigKey;
import ca.poutineqc.base.plugin.PPlugin;

public abstract class LanguagesManager {

	public String[] builtIn;

	private String defaultLanguage;
	private Map<String, Language> languages;

	public abstract Collection<Message> getAdditionnalMessages();

	public LanguagesManager(PPlugin plugin, String[] builtIn) {
		this.builtIn = builtIn;

		languages = new HashMap<String, Language>();
		for (String fileName : builtIn)
			languages.put(fileName, new Language(plugin, fileName, true, PMessages.PREFIX));

		defaultLanguage = plugin.getConfig().getString(PConfigKey.LANGUAGE.getKey(), builtIn[0]);
		if (!isLanguage(defaultLanguage)) {
			Language temp = new Language(plugin, defaultLanguage, false, PMessages.PREFIX);
			if (temp.yamlFile.isLoaded())
				languages.put(defaultLanguage, temp);
		}

		Collection<Message> messages = new ArrayList<Message>();

		for (Message message : PMessages.values())
			messages.add(message);

		messages.addAll(getAdditionnalMessages());

		for (Entry<String, Language> entry : languages.entrySet())
			entry.getValue().addMessages(messages);
	}

	public boolean isLanguage(String fileName) {
		for (String fn : languages.keySet())
			if (fn.equals(fileName))
				return true;

		return false;
	}

	public Language getLanguage(String name) {
		for (String fn : languages.keySet())
			if (fn.equals(name))
				return languages.get(fn);

		for (Entry<String, Language> language : languages.entrySet())
			if (language.getValue().getLanguageName().equalsIgnoreCase(name))
				return language.getValue();
			
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

	public Map<String, Language> getLanguages() {
		return languages;
	}

}
