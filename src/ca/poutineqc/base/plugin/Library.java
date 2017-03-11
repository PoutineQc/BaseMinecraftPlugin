package ca.poutineqc.base.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.CommandListener;
import ca.poutineqc.base.commands.CommandManager;
import ca.poutineqc.base.commands.PCommand;
import ca.poutineqc.base.commands.inventories.PInventory;
import ca.poutineqc.base.datastorage.DataStorage;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.Playable;
import ca.poutineqc.base.instantiable.PlayerFallback;
import ca.poutineqc.base.instantiable.SavableManager;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.LanguagesManager;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.lang.PMessages;

public final class Library extends JavaPlugin implements Listener, PPlugin {

	private static final String[] BUILT_IN = new String[] { "en", "fr" };
	private static Library plugin;

	public static final String WORLD_EDIT_NAME = "WorldEdit";
	static final String ESSENTIALS_NAME = "Essentials";
	
	private boolean essentialsEnabled = false;

	private List<PPlugin> observers;
	private LanguagesManager languages;
	private CommandManager commands;
	private SavableManager<PPlayer> players;
	private PlayerFallback playerFallback;

	@Override
	public void onEnable() {
		Library.plugin = this;
		observers = new ArrayList<PPlugin>();
		saveDefaultConfig();

		this.languages = newLanguageManager();
		this.commands = newCommandManager();
		this.players = newPlayerManager();
		this.playerFallback = new PlayerFallback(this);

		essentialsEnabled = Bukkit.getPluginManager().isPluginEnabled(ESSENTIALS_NAME);

		registerEvents();
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(playerFallback, this);

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
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLoginEvent(PlayerJoinEvent event) {
		players.loadIfSaved(event.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDisconnectEvent(PlayerQuitEvent event) {
		players.removeIfHandled(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player))
			return;

		PPlayer pPlayer = getPPlayer((Player) event.getPlayer());
		pPlayer.setCurrentInventory(null);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getAction() == InventoryAction.NOTHING || event.getAction() == InventoryAction.UNKNOWN)
			return;

		if (!(event.getWhoClicked() instanceof Player))
			return;

		ItemStack item = event.getCurrentItem();
		PPlayer player = getPPlayer((Player) event.getWhoClicked());

		PInventory inv = player.getCurrentInventory();
		if (inv == null)
			return;

		event.setCancelled(true);
		inv.update(item, event.getAction());
	}

	public SavableManager<PPlayer> newPlayerManager() {
		SavableManager<PPlayer> players = new SavableManager<PPlayer>(this, PPlayer.TABLE_NAME, false) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3773558170843085470L;

			@Override
			public SavableParameter getIdentification() {
				return PPlayer.getIdentifications();
			}

			@Override
			public List<SavableParameter> getColumns() {
				return PPlayer.getColumns();
			}

			@Override
			public PPlayer newInstance(DataStorage data, UUID uuid) {
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
		commands.addCommand(PCommand.LANGUAGE);
		commands.addCommand(PCommand.RELOAD);

		commands.addCommand(PCommand.JOIN);
		commands.addCommand(PCommand.QUIT);

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
		return "PL";
	}

	@Override
	public Playable getGameFromArenaName(String string) {
		return null;
	}

	@Override
	public void newArena(String string) {
		// Does nothing
	}

	@Override
	public void deleteArena(Playable game) {
		// Does nothing
		
	}

	@Override
	public List<Playable> getGames() {
		return new ArrayList<Playable>();
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
		return (plugin.players.isSaved(uuid)) ? plugin.players.get(uuid).getLanguage() : plugin.languages.getDefault();
	}

	public static LanguagesManager getLanguageManager() {
		return plugin.languages;
	}

	public static boolean isEssentialsEnabled() {
		return plugin.essentialsEnabled;
	}

}
