package ca.sebastienchagnon.minecraft.prolib.lang;

public enum PMessages implements Message {
	// NO
	LANGUAGE_NAME("languageName", "english"),
	// NO
	NOBODY("keyWordNoOne", "no one yet"),
	// NO
	HELP_HELP("helpHelp", "You already know how to do that don't you? ;)"),
	// NO
	KEYWORD_HELP("keywordHelp", "help"),
	// NO
	HELP_DESCRIPTION_GENERAL("helpDescriptionGeneral", "General commands"),
	// NO
	HELP_DESCRIPTION_PLAYER("helpDescriptionPlayer", "Usual player commands"),
	// NO
	HELP_DESCRIPTION_SETUP("helpDescriptionSetup", "Commands to setup the plugin"),
	// NO
	HELP_DESCRIPTION_ADMIN("helpDescriptionAdmin", "Plugin's maintenance commands"),
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
	RELOAD("reload", "%s%Plugin reload"),
	// YES
	HELP("help", "&7Type %s%/%cmd% help &7to get some help with the plugin's commands"),
	// YES
	DEVELOPPER("developper", "&7Developped by: %s%%developper%"),
	// YES
	VERSION("version", "&7Plugin version: %s%%version%"),
	// YES
	ERROR_COMMAND("errorCommand", "&cThere is no such command. Type &8/%cmd% help &cto get some help"),
	// NO
	KEYWORD_SERVER("keyWordServer", "server"),
	// NO
	COMMAND_NO_PERMISSION("commandNoPermission", "You don't have the permission to do this."),
	// YES
	KEYWORD_GUI_INSTRUCTIONS("keywordGuiInstructions", "&6Instructions"),
	// NO
	KEYWORD_PLAYERS("keywordPlayers", "Players"),
	// NO
	KEYWORD_GUI_PAGE("keywordGuiPage", "Page %number%"),
	
	HELP_LAG("helpLag", "Toggle the lag reduction in minigames");

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

}
