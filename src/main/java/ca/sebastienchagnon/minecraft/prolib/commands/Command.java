package ca.sebastienchagnon.minecraft.prolib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import ca.sebastienchagnon.minecraft.prolib.PPlugin;
import ca.sebastienchagnon.minecraft.prolib.lang.Message;
import ca.sebastienchagnon.minecraft.prolib.utils.Verify.VerifyException;

/**
 * A Command is something that can be executed by players
 * @author Sebastien Chagnon
 *
 */
public interface Command {

	/**
	 * Represents the different type of commands that can be used.
	 * 
	 * @author Sebastien Chagnon
	 *
	 */
	public enum CommandType {
		GENERAL, SETUP, ADMIN, PLAYER,
	}

	void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) throws VerifyException;

	void complete(PPlugin plugin, List<String> tabCompletion, String[] args);

	String getName();
	
	Message getHelpMessage();

	String getUsage();

	boolean equals(String cmdChoice);

	boolean isOfType(CommandType commandType);

	boolean hasPermission(PPlugin plugin, CommandSender commandSender);


}
