package ca.poutineqc.base.lang;

import java.util.Collection;
import java.util.HashMap;

import ca.poutineqc.base.plugin.PConfigKey;
import ca.poutineqc.base.plugin.PPlugin;

public abstract class LanguagesManager extends HashMap<String, Language> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8876668613284932805L;
	static final String DEFAULT = "default";

	public abstract Collection<Message> getMessages();

	public LanguagesManager(PPlugin plugin, String[] builtIn) {
		for (String fileName : builtIn) {
			this.put(fileName, new Language(plugin, fileName, true, false));
		}

		String defaultLanguageKey = plugin.getConfig().getString(PConfigKey.LANGUAGE.getKey(), builtIn[0]);
		Language defaultLanguage = this.get(defaultLanguageKey);
		if (defaultLanguage == null) {
			defaultLanguage = new Language(plugin, defaultLanguageKey, false, false);
			if (!defaultLanguage.yamlFile.isLoaded())
				this.put(defaultLanguageKey, defaultLanguage);
			else
				defaultLanguage = this.getMain();
		}
		this.put(DEFAULT, new Language(defaultLanguage));

		for (Entry<String, Language> entry : this.entrySet())
			entry.getValue().addMessages(plugin, getMessages());

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

		for (Entry<String, Language> language : this.entrySet())
			if (language.getKey().equals(language.getValue().unpad(name)))
				return language.getValue();

		return null;
	}

	public Language getDefault() {
		if (this.containsKey(DEFAULT))
			return this.get(DEFAULT);

		return getMain();
	}

	public Language getMain() {
		for (String fn : this.keySet())
			if (fn.equals("en"))
				return this.get(fn);

		return null;
	}

	public void append(LanguagesManager languageManager) {
		for (Entry<String, Language> language : languageManager.entrySet()) {
			if (this.containsKey(language.getKey()))
				this.get(language.getKey()).putAll(language.getValue());
			else
				this.put(language.getKey(), language.getValue());

		}
	}

}
