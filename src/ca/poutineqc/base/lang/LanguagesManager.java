package ca.poutineqc.base.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PConfigKey;
import ca.poutineqc.base.plugin.PPlugin;

public abstract class LanguagesManager extends HashMap<String, Language> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8876668613284932805L;

	public String[] builtIn;

	private String defaultLanguage;

	public abstract Collection<Message> getMessages();

	public LanguagesManager(PPlugin plugin, String[] builtIn) {
		this.builtIn = builtIn;

		for (String fileName : builtIn)
			this.put(fileName, new Language(plugin, fileName, true, PMessages.PREFIX));

		defaultLanguage = plugin.getConfig().getString(PConfigKey.LANGUAGE.getKey(), builtIn[0]);
		if (!isLanguage(defaultLanguage)) {
			Language temp = new Language(plugin, defaultLanguage, false, PMessages.PREFIX);
			if (temp.yamlFile.isLoaded())
				this.put(defaultLanguage, temp);
		}

		Collection<Message> messages = new ArrayList<Message>();

		for (Message message : PMessages.values())
			messages.add(message);

		messages.addAll(getMessages());

		for (Entry<String, Language> entry : this.entrySet())
			entry.getValue().addMessages(messages);
	}

	public boolean isLanguage(String fileName) {
		for (String fn : this.keySet())
			if (fn.equals(fileName))
				return true;

		return false;
	}

	public Language getLanguage(String name) {
		for (String fn : this.keySet())
			if (fn.equals(name))
				return this.get(fn);

		for (Entry<String, Language> language : this.entrySet())
			if (language.getValue().getLanguageName().equalsIgnoreCase(name))
				return language.getValue();
			
		return getDefault();
	}

	public Language getDefault() {
		for (String fn : this.keySet())
			if (fn.equals(defaultLanguage))
				return this.get(fn);

		return getMain();
	}

	public Language getMain() {
		for (String fn : this.keySet())
			if (fn.equals(builtIn[0]))
				return this.get(fn);

		return null;
	}

	public void append(LanguagesManager languageManager) {
		
	}

}
