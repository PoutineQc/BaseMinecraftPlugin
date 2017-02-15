package ca.poutineqc.base.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import ca.poutineqc.base.commands.Command.CommandType;

public class CommandManager {

	Collection<Command> commands;
	
	public CommandManager(PCommand[] baseCommands) {
		
	}
	
	public void addCommands(Collection<Command> commands) {
		commands.addAll(commands);
	}
	
	public Command getCommand(String cmdChoice) {
		for (Command command : this.commands) {
			if (command.equals(cmdChoice))
				return command;
		}
		return null;
	}

	public List<Command> getRequiredCommands(CommandSender commandSender, CommandType commandType) {
		List<Command> commands = new ArrayList<Command>();
		
		for (Command command : this.commands) {
			if (command.isOfType(commandType) && command.hasPermission(commandSender))
				commands.add(command);
		}
		
		return commands;
	}
	
}
