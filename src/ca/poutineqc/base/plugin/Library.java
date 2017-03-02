package ca.poutineqc.base.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import ca.poutineqc.base.instantiable.Arena;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.SavableManager;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.LanguagesManager;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.lang.PMessages;

public final class Library extends JavaPlugin implements Listener, PPlugin {

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

//		try {
//			ca.poutineqc.base.Main.main(null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		SavableManager<Arena> arenas = new SavableManager<Arena>() {

			@Override
			public Collection<SUUID> getAllSavedUUIDs() {
				return Arena.getAllIdentifications(plugin);
			}
		};
		
		for (SUUID a : arenas.getSaved())
			System.out.println(a);
		
		arenas.add(new Arena(this, "wwdwdwd", Bukkit.getWorld("world")));
		
		commands = newCommandManager();

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
		if (players.isSaved(uuid))
			players.add(new PPlayer(plugin, uuid));
	}

	@EventHandler
	public void onPlayerDisconnectEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PPlayer pPlayer = Library.getPPlayer(player.getUniqueId());
		players.remove(pPlayer);
	}

	public SavableManager<PPlayer> newPlayerManager() {
		return new SavableManager<PPlayer>() {

			@Override
			public Collection<SUUID> getAllSavedUUIDs() {
				return PPlayer.getAllIdentifications(plugin);
			}
		};
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
				Collection<Message> messages = new ArrayList<Message>();
				for (Message m : PMessages.values())
					messages.add(m);

				return messages;
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

	public static PPlayer newPPlayer(UUID uniqueId) {
		PPlayer pplayer = new PPlayer(plugin, uniqueId);
		plugin.players.add(pplayer);
		return pplayer;
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
		return (plugin.players.isSaved(uuid)) ? plugin.players.get(uuid).getLanguage() : plugin.languages.getDefault();
	}

	public static LanguagesManager getLanguageManager() {
		return plugin.languages;
	}

}
