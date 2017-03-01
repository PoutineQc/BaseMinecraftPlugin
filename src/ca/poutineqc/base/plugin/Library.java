package ca.poutineqc.base.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.CommandManager;
import ca.poutineqc.base.commands.PCommand;
import ca.poutineqc.base.data.values.SUUID;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.SavableManager;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.LanguagesManager;

public class Library extends JavaPlugin implements Listener, PPlugin {

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
	}

	@Override
	public void onDisable() {

	}
	
	public static void addObserver(PPlugin observer) {
		plugin.observers.add(observer);
		
		plugin.languages.append(observer.getLanguages());
	}

	@EventHandler
	public void onPlayerLoginEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (plugin.getPlayerManager().isSaved(uuid))
			plugin.getPlayerManager().add(new PPlayer(plugin, uuid));
	}

	@EventHandler
	public void onPlayerDisconnectEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PPlayer pPlayer = plugin.getPPlayer(player.getUniqueId());
		plugin.getPlayerManager().remove(pPlayer);
	}

	public SavableManager<PPlayer> newPlayerManager() {
		return new SavableManager<PPlayer>() {

			@Override
			public Collection<SUUID> getAllSavedUUIDs() {
				return PPlayer.getAllIdentifications(plugin);
			}
		};
	}

	public Library get() {
		return plugin;
	}

	public static Library instance() {
		return plugin;
	}

	public LanguagesManager getLanguageManager() {
		if (languages == null)
			languages = newLanguageManager();

		return languages;
	}

	private LanguagesManager newLanguageManager() {
		// TODO
		return null;
	}

	public CommandManager getCommandManager() {
		if (commands == null)
			commands = newCommandManager();

		return commands;
	}

	public CommandManager newCommandManager() {

		CommandManager commands = new CommandManager();
		commands.addCommand(PCommand.HELP);
		commands.addCommand(PCommand.RELOAD);

		return commands;

	}

	public PPlayer getPPlayer(UUID uuid) {
		return players.get(uuid);
	}

	public PPlayer getPPlayer(String name) {
		return players.get(name);
	}

	public Language getLanguage(CommandSender commandSender) {
		return (commandSender instanceof Player) ? this.getLanguage(((Player) commandSender).getUniqueId())
				: this.getLanguageManager().getDefault();
	}

	public Language getLanguage(UUID uuid) {
		return (this.players.isSaved(uuid)) ? this.players.get(uuid).getLanguage()
				: this.getLanguageManager().getDefault();
	}

	public ChatColor getPrimaryColor() {
		return ChatColor.GOLD;
	}

	public ChatColor getSecondaryColor() {
		return ChatColor.YELLOW;
	}

	public void reload() {
		languages = newLanguageManager();
		reload();
	}

	public SavableManager<PPlayer> getPlayerManager() {
		return players;
	}

	@Override
	public LanguagesManager getLanguages() {
		return languages;
	}

}
