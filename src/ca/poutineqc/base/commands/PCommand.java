package ca.poutineqc.base.commands;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Utils;

public enum PCommand implements Command {

	HELP("help", PMessages.HELP_HELP, "/%command% help [category] [page]", CommandType.GENERAL) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) {

			Language responseLanguage = Library.getLanguage(commandSender);

			String header = Utils.color("&8&m" + StringUtils.repeat(" ", 15) + "&r&8| " + plugin.getPrimaryColor()
					+ plugin.getDescription().getName() + " " + plugin.getSecondaryColor()
					+ ChatColor.stripColor(responseLanguage.getMessage(plugin, PMessages.KEYWORD_HELP)) + "&8|&m"
					+ StringUtils.repeat(" ", 35));

			if (args.length == 1) {
				sendGeneralHelp(plugin, commandSender, responseLanguage, cmdValue, header);
				return;
			}

			int pageNumber = 1;
			CommandType commandType = null;
			List<Command> requestedCommands;

			try {
				pageNumber = Integer.parseInt(args[1]);
				if (pageNumber < 1)
					pageNumber = 1;

				requestedCommands = plugin.getCommandManager().getRequiredCommands(plugin, commandSender, commandType);
				if (pageNumber > Math.ceil((double) requestedCommands.size() / 3))
					pageNumber = (int) Math.ceil((double) requestedCommands.size() / 3);

			} catch (NumberFormatException e) {
				switch (args[1].toLowerCase()) {
				case "player":
					commandType = CommandType.PLAYER;
					break;
				case "setup":
					commandType = CommandType.SETUP;
					break;
				case "admin":
					commandType = CommandType.ADMIN;
					break;
				case "general":
					commandType = CommandType.GENERAL;
					break;
				}

				requestedCommands = plugin.getCommandManager().getRequiredCommands(plugin, commandSender, commandType);

				if (args.length > 2) {
					try {
						pageNumber = Integer.parseInt(args[2]);
						if (pageNumber < 1)
							pageNumber = 1;

						if (pageNumber > Math.ceil((double) requestedCommands.size() / 3))
							pageNumber = (int) Math.ceil((double) requestedCommands.size() / 3);

					} catch (NumberFormatException ex) {
					}
				}
			}

			if (requestedCommands.size() == 0)
				pageNumber = 0;

			commandSender.sendMessage(Utils.color(header));
			commandSender.sendMessage(Utils.color(plugin.getPrimaryColor()
					+ ChatColor.stripColor(responseLanguage.getMessage(plugin, PMessages.KEYWORD_HELP_CATEGORY))
					+ ": &7" + (commandType == null ? "ALL" : commandType.toString()) + ", " + plugin.getPrimaryColor()
					+ ChatColor.stripColor(responseLanguage.getMessage(plugin, PMessages.KEYWORD_HELP_PAGE)) + ": &7"
					+ String.valueOf(pageNumber) + "&8/&7" + (int) (Math.ceil((double) requestedCommands.size() / 3))));

			if (pageNumber == 0) {
				commandSender.sendMessage(ChatColor.RED
						+ ChatColor.stripColor(responseLanguage.getMessage(plugin, PMessages.HELP_NO_PERMISSIONS)));
				commandSender.sendMessage("\n");
				return;
			}

			for (int i = 3 * (pageNumber - 1); i < requestedCommands.size() && i < (3 * (pageNumber - 1)) + 3; i++) {
				commandSender.sendMessage(
						plugin.getPrimaryColor() + requestedCommands.get(i).getUsage().replace("%command%", cmdValue));
				commandSender.sendMessage(Utils.color(" &8- &7" + ChatColor
						.stripColor(responseLanguage.getMessage(plugin, requestedCommands.get(i).getHelpMessage()))));
			}

			commandSender.sendMessage("\n");

		}

		private void sendGeneralHelp(PPlugin plugin, CommandSender commandSender, Language responseLanguage,
				String cmdValue, String header) {
			commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', header));
			commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrimaryColor() + "/" + cmdValue
					+ " help general &8- " + responseLanguage.getMessage(plugin, PMessages.HELP_DESCRIPTION_GENERAL)));
			commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrimaryColor() + "/" + cmdValue + " help player &8- "
					+ responseLanguage.getMessage(plugin, PMessages.HELP_DESCRIPTION_PLAYER)));
			commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrimaryColor() + "/" + cmdValue + " help setup &8- "
					+ responseLanguage.getMessage(plugin, PMessages.HELP_DESCRIPTION_SETUP)));
			commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrimaryColor() + "/" + cmdValue + " help admin &8- "
					+ responseLanguage.getMessage(plugin, PMessages.HELP_DESCRIPTION_ADMIN)));
			commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrimaryColor() + "/" + cmdValue + " help all &8- "
					+ responseLanguage.getMessage(plugin, PMessages.HELP_DESCRIPTION_ALL)));
			commandSender.sendMessage("\n");
			return;
		}

		@Override
		public void complete(List<String> tabCompletion, String[] args) {
			if (args.length == 2)
				for (CommandType category : CommandType.values())
					if (category.name().toLowerCase().startsWith(args[1].toLowerCase()))
						tabCompletion.add(category.name().toLowerCase());
		}
	},
	LANGUAGE("language", PMessages.HELP_LANGUAGE, "%plugin%.player.language", "/%command% language <language>",
			CommandType.GENERAL) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) {
			if (!(commandSender instanceof Player)) {
				commandSender.sendMessage("You cannot use this command from here!");
				return;
			}

			Player player = (Player) commandSender;
			Language responseLanguage = Library.getLanguage(player.getUniqueId());

			if (args.length == 1) {
				player.sendMessage(Utils.color(
						plugin.getPrimaryColor() + responseLanguage.getMessage(plugin, PMessages.LANGUAGE_LIST)));
				for (Entry<String, Language> language : Library.getLanguageManager().entrySet())
					player.sendMessage(plugin.getSecondaryColor() + "- " + language.getValue().getLanguageName());
				return;
			}

			Language language = Library.getLanguageManager().getLanguage(args[1]);
			if (language == null) {
				player.sendMessage(Utils.color(
						responseLanguage.getMessage(plugin, PMessages.LANGUAGE_NOT_FOUND).replace("%cmd%", cmdValue)));
				return;
			}

			PPlayer basePlayer = Library.getPPlayer(player.getUniqueId());
			if (basePlayer == null) {
				basePlayer = Library.newPPlayer(player);
			}
			
			basePlayer.setLanguage(language);
			basePlayer.sendMessage(plugin, PMessages.LANGUAGE_CHANGED);
		}

		@Override
		public void complete(List<String> tabCompletion, String[] args) {
			if (args.length == 2)
				for (Entry<String, Language> lang : Library.getLanguageManager().entrySet())
					if (lang.getValue().getLanguageName().toLowerCase().startsWith(args[1].toLowerCase()))
						tabCompletion.add(lang.getValue().getLanguageName());
		}
	},

	RELOAD("reload", PMessages.HELP_RELOAD, "%plugin%.admin.reload", "/%command% reload", CommandType.ADMIN) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) {

			plugin.reload();

			Language responseLanguage = (commandSender instanceof Player)
					? Library.getLanguage(((Player) commandSender).getUniqueId())
					: Library.getLanguageManager().getDefault();

			commandSender.sendMessage(responseLanguage.getMessage(plugin, PMessages.RELOAD));
		}

		@Override
		public void complete(List<String> tabCompletion, String[] args) {
			return;
		}
	};

	private CommandType type;
	private String cmdChoice;
	private String usage;
	private String permission;
	private Message helpMessage;

	private PCommand(String cmdChoice, Message helpMessage, String permission, String usage, CommandType type) {
		this.cmdChoice = cmdChoice;
		this.usage = usage;
		this.permission = permission;
		this.helpMessage = helpMessage;
		this.type = type;
	}

	private PCommand(String cmdChoice, Message helpMessage, String usage, CommandType type) {
		this.cmdChoice = cmdChoice;
		this.usage = usage;
		this.permission = null;
		this.helpMessage = helpMessage;
		this.type = type;
	}

	@Override
	public boolean isOfType(CommandType type) {
		return (type == null) ? true : this.type.equals(type);
	}

	@Override
	public boolean equals(String cmdChoice) {
		return this.cmdChoice.equalsIgnoreCase(cmdChoice);
	}

	@Override
	public String getUsage() {
		return usage;
	}

	@Override
	public boolean hasPermission(PPlugin plugin, CommandSender commandSender) {
		return (permission != null)
				? commandSender.hasPermission(permission.replace("%plugin%", plugin.getDescription().getName().toLowerCase())) : true;
	}

	@Override
	public Message getHelpMessage() {
		return helpMessage;
	}

	public abstract void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args);

	@Override
	public abstract void complete(List<String> tabCompletion, String[] args);

}
