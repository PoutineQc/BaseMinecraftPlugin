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
	// NO
	HELP_LIST("helpList", "Displays all the available Arenas"),
	// NO
	HELP_JOIN("helpJoin", "Join an arena to play a game"),
	// NO
	HELP_QUIT("helpQuit", "Quit your game if you're in one"),
	// NO
	ERROR_QUIT_NOT_IN_GAME("errorQuitNotInGame", "You are not in a game."),
	// YES
	JOIN_GUI_TITLE("joinGuiTitle", "&2Join a Game"),
	// YES
	KEYWORD_GUI_INSTRUCTIONS("keywordGuiInstructions", "&6Instructions"),
	// YES
	JOIN_GUI_INFO("joinGuiInfo", "&eClick the name of the arena you wish to join.\n&eRight click to get the arena's informations."),
	// NO
	KEYWORD_GAMESTATE_UNSET("keywordGameUnset", "Game Unset"),
	// NO
	KEYWORD_GAMESTATE_ACTIVE("keywordGameStateAvtive", "Active"),
	// NO
	KEYWORD_GAMESTATE_READY("keywordGameStateAvtive", "Ready"),
	// NO
	KEYWORD_PLAYERS("keywordPlayers", "Players"),
	// NO
	KEYWORD_GUI_PAGE("keywordGuiPage", "Page %number%"),
	// NO
	HELP_INFO("helpInfo", "Display all the information an arena"),
	// NO
	ERROR_MISSING_ARENA("errorMissingArena", "This arena %arena% does not exist"),
	// YES
	INFO_TIP("infoTip", "%s%You may also do the command %p%/%cmd% list %s%and right-click an arena to display it's information."),
	// NO
	ERROR_MISSING_ARENA_PARAMETER("errorMissingArenaParameter", "You must also put the name of an arena."),
	// NO
	HELP_NEW("helpNew", "Create a new arena"),
	// NO
	EDIT_NONAME("editCreateNoName", "You must provide a arena name as the second argument."),
	// NO
	EDIT_NEW_EXISTS("editNewExists", "An arena named %arena% already exists."),
	// NO
	EDIT_NEW_LONG_NAME("editNewLongName", "The name provided is too long. It may be maximum 32 characters long."),
	// YES
	EDIT_NEW_SUCCESS("editNewSuccess", "&aNew arena %arena% created successfully"),
	// NO
	HELP_DELETE("helpDelete", "Delete an arena"),
	// NO
	ERROR_DELETE_PLAYERS_IN_GAME("errorDeletePlayersInGame", "You can't delete an arena while there are players playing a game in it."),
	// NO
	HELP_SETZONE("heloSetzone", "Sets the playing zone of the arena"),
	// YES
	EDIT_DELETE("editDelete", "&aArena %arena% successfully deleted."),
	
	ERROR_WORLD_EDIT_MISSING("errorWorldeditMissing", "Plugin WorldEdit not found. Add it to your plugin folder and restart your server."),
	
	ERROR_WORLD_EDIT_SELECTION_MISSING("errorWorldeditSelectionMissing", "You must selet a WorldEdit zone first."),
	
	EDIT_ZONE("editZone", "&aZone for the arena %arena% was selected successfully."),
	
	HELP_SETLOBBY("helpSetlobby", "Sets the lobby of the arena"),
	
	EDIT_LOBBY("editLobby", "&aLobby for the arena %arena% was set successfully."),
	
	SETSTART_GUI_TITLE("setstartGuiTitle", "&9Edit start points"),
	
	ARENA_COLOR_GUI_INFO("arenaColorGuiInfo", "&eClick to set the start point to your location.\n&eRight click to unregister the start point."),
	
	KEYWORD_START_NUMBER("keywordStartNumber", "Start %number%"),
	
	HELP_SETSPECTATE("helpSpectate", "Sets the spectate point of the arena"),
	
	EDIT_SPECTATE("editSpectate", "&aSpectate point for the arena %arena% was set successfully"),
	
	START_REMOVE("startRemove", "&aStart point %number% successfully removed from the arena %arena%."),
	
	START_ADD("startAdd", "&aStart point %number% successfully added to the arena %arena%."),
	
	HELP_SETMINPLAYERS("helpMinplayers", "Sets the minimum amount of players of the arena"),
	
	AMOUNTPLAYERS_NO_NUMBER("amountPlayersNoNumber", "You must specify a valid integer amount"),
	
	EDIT_MINPLAYERS("editMinplayers", "&aMinimum amount of players for the arena %arena% was set successfully."),
	
	HELP_SETMAXPLAYERS("helpMaxplayers", "Sets the maximum amount of players of the arena"),
	
	EDIT_MAXPLAYERS("editMaxplayers", "&aMaximum amount of players for the arena %arena% was set successfully."),
	
	HELP_COLORS("helpColors", "Edit the theme colors of the arena."),
	
	EDIT_COLORS_GUI_TITLE("editColorsGuiTitle", "&6Theme colors"),
	
	COLORS_GUI_INFO("colorsGuiInfo", "&eClick to toggle the color from\n&ethis arena's theme colors."),
	
	EDIT_COLOR_ERROR("editColorError", "You can't edit the game's theme colors while there are players in the game");

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
