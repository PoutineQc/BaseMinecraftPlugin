package ca.sebastienchagnon.minecraft.prolib.commands;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.sebastienchagnon.minecraft.prolib.Library;
import ca.sebastienchagnon.minecraft.prolib.PPlugin;
import ca.sebastienchagnon.minecraft.prolib.instantiable.PPlayer;
import ca.sebastienchagnon.minecraft.prolib.lang.Language;
import ca.sebastienchagnon.minecraft.prolib.lang.Message;
import ca.sebastienchagnon.minecraft.prolib.lang.PMessages;
import ca.sebastienchagnon.minecraft.prolib.utils.Utils;
import ca.sebastienchagnon.minecraft.prolib.utils.Verify;
import ca.sebastienchagnon.minecraft.prolib.utils.Verify.VerifyException;

public enum PCommand implements Command {

	HELP("help", PMessages.HELP_HELP, "/%command% help [category] [page]", CommandType.GENERAL) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) {

			Language responseLanguage = Library.getLanguage(commandSender);

			String header = Utils.color("&8&m" + StringUtils.repeat(" ", 15) + "&r&8| " + plugin.getPrimaryColor()
					+ plugin.getDescription().getName() + " " + plugin.getSecondaryColor()
					+ ChatColor.stripColor(responseLanguage.get(PMessages.KEYWORD_HELP)) + "&8|&m"
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
					+ ChatColor.stripColor(responseLanguage.get(PMessages.KEYWORD_HELP_CATEGORY)) + ": &7"
					+ (commandType == null ? "ALL" : commandType.toString()) + ", " + plugin.getPrimaryColor()
					+ ChatColor.stripColor(responseLanguage.get(PMessages.KEYWORD_HELP_PAGE)) + ": &7"
					+ String.valueOf(pageNumber) + "&8/&7" + (int) (Math.ceil((double) requestedCommands.size() / 3))));

			if (pageNumber == 0) {
				commandSender.sendMessage(
						ChatColor.RED + ChatColor.stripColor(responseLanguage.get(PMessages.HELP_NO_PERMISSIONS)));
				commandSender.sendMessage("\n");
				return;
			}

			for (int i = 3 * (pageNumber - 1); i < requestedCommands.size() && i < (3 * (pageNumber - 1)) + 3; i++) {
				commandSender.sendMessage(
						plugin.getPrimaryColor() + requestedCommands.get(i).getUsage().replace("%command%", cmdValue));
				commandSender.sendMessage(Utils.color(" &8- &7"
						+ ChatColor.stripColor(responseLanguage.get(requestedCommands.get(i).getHelpMessage()))));
			}

			commandSender.sendMessage("\n");

		}

		private void sendGeneralHelp(PPlugin plugin, CommandSender commandSender, Language responseLanguage,
				String cmdValue, String header) {
			commandSender.sendMessage(Utils.color(header));
			commandSender.sendMessage(Utils.color(plugin.getPrimaryColor() + "/" + cmdValue + " help general &8- "
					+ ChatColor.stripColor(responseLanguage.get(PMessages.HELP_DESCRIPTION_GENERAL))));
			commandSender.sendMessage(Utils.color(plugin.getPrimaryColor() + "/" + cmdValue + " help player &8- "
					+ ChatColor.stripColor(responseLanguage.get(PMessages.HELP_DESCRIPTION_PLAYER))));
			commandSender.sendMessage(Utils.color(plugin.getPrimaryColor() + "/" + cmdValue + " help setup &8- "
					+ ChatColor.stripColor(responseLanguage.get(PMessages.HELP_DESCRIPTION_SETUP))));
			commandSender.sendMessage(Utils.color(plugin.getPrimaryColor() + "/" + cmdValue + " help admin &8- "
					+ ChatColor.stripColor(responseLanguage.get(PMessages.HELP_DESCRIPTION_ADMIN))));
			commandSender.sendMessage(Utils.color(plugin.getPrimaryColor() + "/" + cmdValue + " help all &8- "
					+ ChatColor.stripColor(responseLanguage.get(PMessages.HELP_DESCRIPTION_ALL))));
			commandSender.sendMessage("\n");
			return;
		}

		@Override
		public void complete(PPlugin plugin, List<String> tabCompletion, String[] args) {
			if (args.length == 2)
				for (CommandType category : CommandType.values())
					if (category.name().toLowerCase().startsWith(args[1].toLowerCase()))
						tabCompletion.add(category.name().toLowerCase());
		}
	},
	LANGUAGE("language", PMessages.HELP_LANGUAGE, "%plugin%.player.language", "/%command% language <language>",
			CommandType.GENERAL) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) throws VerifyException {
			Verify.isPlayer(commandSender, "You cannot use this command from here!");

			Player player = (Player) commandSender;
			Language responseLanguage = Library.getLanguage(player.getUniqueId());

			if (args.length == 1) {
				responseLanguage.sendMessage(plugin, player, PMessages.LANGUAGE_LIST);
				for (Entry<String, Language> language : Library.getLanguageManager().entrySet())
					player.sendMessage(plugin.getSecondaryColor() + "- " + language.getValue().getLanguageName());
				return;
			}

			Language language = Library.getLanguageManager().getLanguage(args[1]);
			Verify.isInstantiated(language, responseLanguage.get(PMessages.LANGUAGE_NOT_FOUND).replace("%cmd%", cmdValue));

			PPlayer basePlayer = Library.getPPlayer(player.getUniqueId());
			if (basePlayer == null) {
				basePlayer = Library.newPPlayer(player);
			}

			basePlayer.setLanguage(language);
			basePlayer.sendMessage(plugin, PMessages.LANGUAGE_CHANGED);
		}

		@Override
		public void complete(PPlugin plugin, List<String> tabCompletion, String[] args) {
			if (args.length == 2)
				for (Entry<String, Language> lang : Library.getLanguageManager().entrySet())
					if (lang.getValue().getLanguageName().toLowerCase().startsWith(args[1].toLowerCase()))
						tabCompletion.add(lang.getValue().getLanguageName());
		}
	},
	LAG("reducelag", PMessages.HELP_LAG, "%plugin%.player.lag", "/%command% reducelag",
			CommandType.GENERAL) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) throws VerifyException {
			Verify.isPlayer(commandSender, "You cannot use this command from here!");

			Player player = (Player) commandSender;
			PPlayer basePlayer = Library.getPPlayer(player.getUniqueId());
			if (basePlayer == null)
				basePlayer = Library.newPPlayer(player);
		}

		@Override
		public void complete(PPlugin plugin, List<String> tabCompletion, String[] args) {
		}
	},

	RELOAD("reload", PMessages.HELP_RELOAD, "%plugin%.admin.reload", "/%command% reload", CommandType.ADMIN) {
		@Override
		public void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) {
			plugin.reload();

			Language responseLanguage = Library.getLanguage(commandSender);
			commandSender.sendMessage(responseLanguage.get(PMessages.RELOAD));
		}

		@Override
		public void complete(PPlugin plugin, List<String> tabCompletion, String[] args) {
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
	public String getName() {
		return cmdChoice;
	}

	@Override
	public boolean hasPermission(PPlugin plugin, CommandSender commandSender) {
		return (permission != null) ? commandSender
				.hasPermission(permission.replace("%plugin%", plugin.getDescription().getName().toLowerCase())) : true;
	}

	@Override
	public Message getHelpMessage() {
		return helpMessage;
	}

	@Override
	public abstract void execute(PPlugin plugin, CommandSender commandSender, String cmdValue, String[] args) throws VerifyException;

	@Override
	public abstract void complete(PPlugin plugin, List<String> tabCompletion, String[] args);

}
