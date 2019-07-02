package ca.poutineqc.base.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import ca.poutineqc.base.Library;
import ca.poutineqc.base.PPlugin;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.utils.Utils;
import ca.poutineqc.base.utils.Verify.VerifyException;

public class CommandListener implements CommandExecutor, TabCompleter {

	private PPlugin plugin;

	public CommandListener(PPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String cmdValue, String[] args) {

		Language local = Library.getLanguage(sender);

		if (args.length == 0) {
			sender.sendMessage(Utils.color("&8&m" + StringUtils.repeat(" ", 15) + "&r&8| " + plugin.getPrimaryColor()
					+ plugin.getDescription().getName() + " " + "&8|&m" + StringUtils.repeat(" ", 40)));
			sender.sendMessage(local.get(PMessages.DEVELOPPER).replace("%developper%",
					plugin.getDescription().getAuthors().toString()));
			sender.sendMessage(local.get(PMessages.VERSION).replace("%version%", plugin.getDescription().getVersion()));
			sender.sendMessage(local.get(PMessages.HELP).replace("%cmd%", cmdValue));
			sender.sendMessage("\n");
			return true;
		}

		Command crCommand = plugin.getCommandManager().getCommand(args[0]);
		if (crCommand == null) {
			local.sendError(plugin, sender, local.get(PMessages.ERROR_COMMAND).replace("%cmd%", cmdValue));
			return true;
		}

		if (!crCommand.hasPermission(plugin, sender)) {
			local.sendError(plugin, sender, local.get(PMessages.COMMAND_NO_PERMISSION));
			return true;
		}

		try {
			crCommand.execute(plugin, sender, cmdValue, args);
		} catch (VerifyException e) {
			local.sendError(plugin, sender, e.getMessage());
		}
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		List<String> tabCompletion = new ArrayList<String>();
		
		if (args.length == 1) {
			for (Command command : plugin.getCommandManager().getRequiredCommands(plugin, sender, null))
				if (command.getName().startsWith(args[0].toLowerCase()))
					tabCompletion.add(command.getName());
			
			return tabCompletion;
		}

		Command command = plugin.getCommandManager().getCommand(args[0]);
		if (command == null)
			return tabCompletion;
		
		command.complete(plugin, tabCompletion, args);
		return tabCompletion;
	}
	
	

}
