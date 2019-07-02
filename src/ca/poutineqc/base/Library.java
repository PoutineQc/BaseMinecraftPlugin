package ca.poutineqc.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.CommandListener;
import ca.poutineqc.base.commands.CommandManager;
import ca.poutineqc.base.commands.PCommand;
import ca.poutineqc.base.datastorage.DataStorage;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.SavableManager;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.LanguagesManager;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.listeners.InventoryEvents;

public final class Library extends JavaPlugin implements PPlugin {

	private static final String[] BUILT_IN = new String[] { "en", "fr" };
	private static Library plugin;

	private List<PPlugin> observers;
	private LanguagesManager languages;
	private CommandManager commands;
	private SavableManager<PPlayer> players;

	@Override
	public void onEnable() {
		Library.plugin = this;
		saveDefaultConfig();

		observers = new ArrayList<PPlugin>();
		this.languages = newLanguageManager();
		this.commands = newCommandManager();
		this.players = newPlayerManager();
		for (Player player : this.getServer().getOnlinePlayers())
			players.loadIfSaved(player.getUniqueId());

		registerEvents();
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryEvents(), this);
		pm.registerEvents(new PlayerLoader(this), this);

		CommandListener commandListener = new CommandListener(this);
		PluginCommand command = getCommand("poulib");
		command.setExecutor(commandListener);
		command.setTabCompleter(commandListener);
	}

	@Override
	public void onDisable() {

	}

	public static void addObserver(PPlugin observer) {
		plugin.observers.add(observer);

		plugin.languages.append(observer.getLanguages());
		plugin.getLogger().info("Linked!");
	}

	public SavableManager<PPlayer> newPlayerManager() {
		SavableManager<PPlayer> players = new SavableManager<PPlayer>(this, PPlayer.TABLE_NAME, false) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3773558170843085470L;

			@Override
			public SavableParameter getIdentification() {
				return PPlayer.getIdentification();
			}

			@Override
			public List<SavableParameter> getColumns() {
				return PPlayer.getColumns();
			}

			@Override
			public PPlayer newInstance(JavaPlugin javaPlugin, DataStorage data, UUID uuid) {
				return new PPlayer(plugin, data, uuid);
			}
		};

		for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
			players.loadIfSaved(onlinePlayers.getUniqueId());
		}

		return players;
	}

	@Override
	public Library get() {
		return plugin;
	}

	private LanguagesManager newLanguageManager() {
		return new LanguagesManager(this, BUILT_IN) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2417569037170329095L;

			@Override
			public Collection<Message> getMessages() {
				return new ArrayList<Message>(Arrays.asList(PMessages.values()));
			}
		};
	}

	@Override
	public LanguagesManager getLanguages() {
		return languages;
	}

	public CommandManager newCommandManager() {

		CommandManager commands = new CommandManager();
		commands.addCommand(PCommand.HELP);
		commands.addCommand(PCommand.LANGUAGE);
		commands.addCommand(PCommand.RELOAD);

		return commands;

	}

	@Override
	public CommandManager getCommandManager() {
		return commands;
	}

	@Override
	public ChatColor getPrimaryColor() {
		return ChatColor.GOLD;
	}

	@Override
	public ChatColor getSecondaryColor() {
		return ChatColor.YELLOW;
	}

	@Override
	public boolean reload() {
		for (PPlugin observer : observers)
			if (!observer.canBeReloaded())
				return false;

		languages = newLanguageManager();

		for (PPlugin observer : observers)
			languages.append(observer.getLanguages());

		return true;
	}

	@Override
	public boolean canBeReloaded() {
		return true;
	}

	@Override
	public String getPrefix() {
		return getPrimaryColor() + "[" + getSecondaryColor() + this.getDescription().getPrefix() + getPrimaryColor()
				+ "]";
	}

	public static PPlayer newPPlayer(Player player) {
		PPlayer pplayer = new PPlayer(plugin, plugin.players.getDataStorage(), player);
		plugin.players.add(pplayer);
		return pplayer;
	}

	public static PPlayer getPPlayer(Player player) {
		return getPPlayer(player.getUniqueId());
	}

	public static PPlayer getPPlayer(UUID uuid) {
		return plugin.players.get(uuid);
	}

	public static PPlayer getPPlayer(String name) {
		return plugin.players.get(name);
	}

	public static Language getLanguage(CommandSender commandSender) {
		return (commandSender instanceof Player) ? Library.getLanguage(((Player) commandSender).getUniqueId())
				: plugin.languages.getDefault();
	}

	public static Language getLanguage(UUID uuid) {

		try {
			return (plugin.players.isSaved(uuid)) ? plugin.players.get(uuid).getLanguage()
					: plugin.languages.getDefault();
		} catch (NullPointerException e) {
			return plugin.languages.getDefault();
		}
	}

	public static LanguagesManager getLanguageManager() {
		return plugin.languages;
	}

	SavableManager<PPlayer> getPlayers() {
		return players;
	}

}
