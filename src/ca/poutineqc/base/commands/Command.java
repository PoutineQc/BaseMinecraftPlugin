package ca.poutineqc.base.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.plugin.PPlugin;

/**
 * A Command is something that can be executed by players
 * @author Sébastien Chagnon
 *
 */
public interface Command {

	/**
	 * Represents the different type of commands that can be used.
	 * 
	 * @author Sébastien Chagnon
	 *
	 */
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
