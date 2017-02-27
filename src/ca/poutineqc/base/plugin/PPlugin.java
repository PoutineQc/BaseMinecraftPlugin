package ca.poutineqc.base.plugin;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.PCommand;
import ca.poutineqc.base.commands.Command;
import ca.poutineqc.base.commands.CommandManager;
import ca.poutineqc.base.instantiable.Arena;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.SavableManager;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.LanguagesManager;
import ca.poutineqc.base.utils.Utils;

public abstract class PPlugin extends JavaPlugin {

	private static PPlugin plugin;
	
	ChatColor primaryColor;
	ChatColor secondaryColor;
	
	private LanguagesManager languages;
	private CommandManager commands;
	private SavableManager<? extends PPlayer> players;
	private SavableManager<? extends Arena> arenas;


	@Override
	public void onEnable() {
		PPlugin.plugin = this;
		saveDefaultConfig();

		try {
			this.primaryColor = Utils.getColor(this.getConfig().getString(PConfigKey.PRIMARY_COLOR.getKey()));
			this.secondaryColor = Utils.getColor(this.getConfig().getString(PConfigKey.SECONDARY_COLOR.getKey()));
		} catch (InvalidParameterException ex) {
			this.getLogger().warning("Could not understand the primary and secondary colors in the config file.");
			this.getLogger().warning("Make sure the parameter is as follow: \"&a\" (must respect the following regex : &([0-9]|[a-f]) )");
			primaryColor = ChatColor.DARK_PURPLE;
			primaryColor = ChatColor.LIGHT_PURPLE;
		}
		
	}

	@Override
	public void onDisable() {

	}

	public abstract SavableManager<? extends PPlayer> getNewPlayerManager();

	public abstract SavableManager<? extends Arena> getNewArenaManager();

	public abstract LanguagesManager getNewLanguageManager();

	public static PPlugin get() {
		return plugin;
	}

	public LanguagesManager getLanguageManager() {
		if (languages == null)
			languages = getNewLanguageManager();

		return languages;
	}

	public CommandManager getCommandManager() {
		if (commands == null) {
			commands = new CommandManager();
			commands.addCommands(getCommands());
		}

		return commands;
	}

	abstract Collection<Command> getCommands();

	public SavableManager<? extends PPlayer> getPlayerManager() {
		if (players == null)
			players = getNewPlayerManager();

		return players;
	}

	public SavableManager<? extends Arena> getArenaManager() {
		if (arenas == null)
			arenas = getNewArenaManager();

		return arenas;
	}

	public Language getLanguage(CommandSender commandSender) {
		return (commandSender instanceof Player) ? this.getLanguage(((Player) commandSender).getUniqueId())
				: this.getLanguageManager().getDefault();
	}

	public Language getLanguage(UUID uuid) {
		return (this.getPlayerManager().isSaved(uuid)) ? this.getPlayerManager().get(uuid).getLanguage()
				: this.getLanguageManager().getDefault();
	}

	public String getPrimaryColor() {
		return this.getConfig().getString(PConfigKey.PRIMARY_COLOR.getKey());
	}

	public String getSecondaryColor() {
		return this.getConfig().getString(PConfigKey.SECONDARY_COLOR.getKey());
	}

	public void reload() {
		languages = getNewLanguageManager();
		reload();
	}
	
	abstract void reloadChild();

	public abstract <T extends PPlayer> T newPlayer(Player player);
}
