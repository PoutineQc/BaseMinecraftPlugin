package ca.poutineqc.base.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Utils;

public class CommandListener implements CommandExecutor {

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
			sender.sendMessage(Utils.color(local.getMessage(plugin, PMessages.DEVELOPPER).replace("%developper%",
					plugin.getDescription().getAuthors().toString())));
			sender.sendMessage(Utils
					.color(local.getMessage(plugin, PMessages.VERSION).replace("%version%", plugin.getDescription().getVersion())));
			sender.sendMessage(Utils.color(local.getMessage(plugin, PMessages.HELP).replace("%cmd%", cmdValue)));
			sender.sendMessage("\n");
			return true;
		}

		Command crCommand = plugin.getCommandManager().getCommand(args[0]);
		if (crCommand == null) {
			sender.sendMessage(Utils.color(local.getMessage(plugin, PMessages.ERROR_COMMAND).replace("%cmd%", cmdValue)));
			return true;
		}

		if (!crCommand.hasPermission(plugin, sender)) {
			sender.sendMessage(ChatColor.RED + ChatColor.stripColor(local.getMessage(plugin, PMessages.COMMAND_NO_PERMISSION)));
			return true;
		}

		crCommand.execute(plugin, sender, cmdValue, args);
		return true;
	}

}
