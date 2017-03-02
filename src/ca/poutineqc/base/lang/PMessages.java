package ca.poutineqc.base.lang;

public enum PMessages implements Message {
	// NO
	LANGUAGE_NAME("languageName", "english"),
	// YES
	PREFIX("prefix", "%p%[%s%%plugin%%p%]"),
	// NO
	NOBODY("keyWordNoOne", "no one yet"),
	// NO
	HELP_HELP("helpHelp", "You already know how to do that don't you? ;)"),
	// NO
	KEYWORD_HELP("keywordHelp", "help"),
	// NO
	HELP_DESCRIPTION_GENERAL("helpDescriptionGeneral", "All the commands not related to the plugin core"),
	// NO
	HELP_DESCRIPTION_PLAYER("helpDescriptionPlayer", "Usual player commands"),
	// NO
	HELP_DESCRIPTION_SETUP("helpDescriptionSetup", "Commands to manage the plugin"),
	// NO
	HELP_DESCRIPTION_ADMIN("helpDescriptionAdmin", "Commands related to the maintenance of the plugin"),
	// NO
	HELP_DESCRIPTION_ALL("helpDescriptionAll", "All the commands of this plugin"),
	// NO
	KEYWORD_HELP_CATEGORY("keywordHelpCategory", "Category"),
	// NO
	KEYWORD_HELP_PAGE("keywordHelpPage", "Page"),
	// NO
	HELP_NO_PERMISSIONS("helpNoPermissions", "You don't have any permissions in this category"),
	// NO
	HELP_LANGUAGE("helpLanguage", "To change the language this plugin's messages will be displayed to you"),
	// NO
	LANGUAGE_LIST("languageList", "Available Languages :"),
	// YES
	LANGUAGE_NOT_FOUND("languageNotFound",
			"%s%Language not found. Type %p%/%cmd% language %s%to see the available list."),
	// YES
	LANGUAGE_CHANGED("languageChanged", "%s%Personnal language updated"), 
	// NO
	HELP_RELOAD("helpReload", "To reload the plugin's parameters"),
	// YES
	RELOAD("reload", "%s%Plugin reload");

	private String key;
	private String defaultValue;

	private PMessages(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDefaultMessage() {
		return defaultValue;
	}

	@Override
	public Message getPrefixMessage() {
		return PREFIX;
	}

}
