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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.poutineqc.base.commands.CommandListener;
import ca.poutineqc.base.commands.CommandManager;
import ca.poutineqc.base.commands.PCommand;
import ca.poutineqc.base.data.DataStorage;
import ca.poutineqc.base.data.values.SPlayer;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.SavableManager;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.LanguagesManager;
import ca.poutineqc.base.lang.Message;
import ca.poutineqc.base.lang.PMessages;

public final class Library extends JavaPlugin implements Listener, PPlugin {

	private static final String[] BUILT_IN = new String[] { "en", "fr" };
	private static Library plugin;

	static final String ESSENTIALS_NAME = "Essentials";
	private boolean essentialsEnabled = false;
	
	private List<PPlugin> observers;
	private LanguagesManager languages;
	private CommandManager commands;
	private SavableManager<PPlayer> players;

	@Override
	public void onEnable() {
		Library.plugin = this;
		saveDefaultConfig();

		this.languages = newLanguageManager();
		this.commands = newCommandManager();
		this.players = newPlayerManager();

		getCommand("poulib").setExecutor(new CommandListener(this));
		

		essentialsEnabled = Bukkit.getPluginManager().isPluginEnabled(ESSENTIALS_NAME);
		

		observers = new ArrayList<PPlugin>();
		getServer().getPluginManager().registerEvents(this, this);

//		ItemStack item = new ItemStack(Material.COMMAND);
//
//		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
//		CommandBlock state = (CommandBlock) meta.getBlockState();
//
//		System.out.println("1:'" + state.getCommand() + "'");
//
//		state.setCommand("minecraft:give @p minecraft:dirt");
//		state.update();
//
//		System.out.println("2:'" + state.getCommand() + "'");
//
//		meta.setBlockState(state);
//		meta.getBlockState().update();
//
//		System.out.println("3:'" + ((CommandBlock) meta.getBlockState()).getCommand() + "'");
//
//		item.setItemMeta(meta);
//
//		Bukkit.getPlayer("PoutineQc").getInventory().addItem(item);
//		Bukkit.getPlayer("PoutineQc").updateInventory();
		
		
		new SPlayer(Bukkit.getPlayer("PoutineQc")).apply(Bukkit.getPlayer("PoutineQc"));
		
		
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

	public static PPlayer newPPlayer(Player player) {
		PPlayer pplayer = new PPlayer(plugin, plugin.players.getDataStorage(), player);
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
	
	public static boolean isEssentialsEnabled() {
		return plugin.essentialsEnabled;
	}

}
