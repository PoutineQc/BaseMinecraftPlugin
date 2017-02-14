package ca.poutineqc.base.utils;

import java.util.List;

import org.bukkit.command.CommandSender;

import ca.poutineqc.base.plugin.PoutinePlugin;

public abstract class InGameCommand {

	private CommandType type;
	private String text;
	private String messageKey;
	private String permission;
	private String helpMessage;

	public InGameCommand(String text, String messageKey, String permission, String helpMessage, CommandType type) {
		this.text = text;
		this.messageKey = messageKey;
		this.permission = permission;
		this.helpMessage = helpMessage;
		this.type = type;
	}
	
	public abstract void execute (PoutinePlugin plugin, CommandSender player, String[] args, Object... extra);
	public abstract void complete (List<String> tabCompletion, String[] args);

	public CommandType getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getPermission() {
		return permission;
	}

	public String getHelpMessage() {
		return helpMessage;
	}

	public enum CommandType {
		GENERAL, SETUP, ADMIN, PLAYER, 
	}

}
