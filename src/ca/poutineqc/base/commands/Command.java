package ca.poutineqc.base.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.plugin.PPlugin;

public interface Command {

	public enum CommandType {
		GENERAL, SETUP, ADMIN, PLAYER,
	}

	void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args, Object... extra);

	void complete(List<String> tabCompletion, String[] args);

	Message getHelpMessage();

	String getUsage();

	boolean equals(String cmdChoice);

	boolean isOfType(CommandType commandType);

	boolean hasPermission(CommandSender commandSender);

}
