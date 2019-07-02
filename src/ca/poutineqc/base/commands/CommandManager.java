package ca.poutineqc.base.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import ca.poutineqc.base.PPlugin;
import ca.poutineqc.base.commands.Command.CommandType;

public class CommandManager {

	Collection<Command> commands;
	
	public CommandManager() {
		this.commands = new ArrayList<Command>();
	}

	public void addCommand(Command command) {
		commands.add(command);
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

	public List<Command> getRequiredCommands(PPlugin plugin, CommandSender commandSender, CommandType commandType) {
		List<Command> commands = new ArrayList<Command>();
		
		for (Command command : this.commands) {
			if (command.isOfType(commandType) && command.hasPermission(plugin, commandSender))
				commands.add(command);
		}
		
		return commands;
	}
	
}
